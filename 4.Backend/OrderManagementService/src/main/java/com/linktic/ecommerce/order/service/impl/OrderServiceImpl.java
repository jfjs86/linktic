package com.linktic.ecommerce.order.service.impl;

import com.linktic.ecommerce.order.dto.OrderRequestDto;
import com.linktic.ecommerce.order.dto.OrderResponseDto;
import com.linktic.ecommerce.order.dto.ProductRequestDto;
import com.linktic.ecommerce.order.dto.ProductResponseDto;
import com.linktic.ecommerce.order.model.Order;
import com.linktic.ecommerce.order.model.OrderProduct;
import com.linktic.ecommerce.order.model.OrderProductId;
import com.linktic.ecommerce.order.model.Product;
import com.linktic.ecommerce.order.repository.OrderProductRepository;
import com.linktic.ecommerce.order.repository.OrderRepository;
import com.linktic.ecommerce.order.repository.ProductRepository;
import com.linktic.ecommerce.order.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
//    @Transactional
    public ResponseEntity<OrderResponseDto> saveOrder(OrderRequestDto orderRequest) {
//        int inactiveUpdate = orderRepository.inactiveOrder(orderRequest.getOrderId());
        Order order = createOrUpdateOrder(orderRequest);
        order = orderRepository.save(order);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        inactiveOrders(orderRequest);
        List<Product> products = getProductPerOrder(order);

        if (products == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getId());
        orderResponseDto.setTotalPrice(order.getTotalPrice());

        List<ProductResponseDto> productResponseList = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductId(product.getId());
            productResponseDto.setProductName(product.getName());
            productResponseDto.setProductDescription(product.getDescription());
            productResponseDto.setProductPrice(product.getPrice());

            productResponseList.add(productResponseDto);
        }

        orderResponseDto.setProducts(productResponseList);
        validateAvalaible(products);

        return ResponseEntity.ok(orderResponseDto);
    }
    @Override
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        if(orders == null || orders.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No orders found");
        }

        try {
            List<OrderResponseDto> orderResponseList = new ArrayList<>();

            for(Order or : orders){
                OrderResponseDto orderResponseDto = new OrderResponseDto();

                orderResponseDto.setOrderId(or.getId());
                orderResponseDto.setTotalPrice(or.getTotalPrice());

                List<ProductResponseDto> productResponseList = new ArrayList<>();

                List<Product> products = getProductPerOrder(or);

                for(Product pr : products){
                    ProductResponseDto productResponseDto = new ProductResponseDto();
                    productResponseDto.setProductId(pr.getId());
                    productResponseDto.setProductName(pr.getName());
                    productResponseDto.setProductPrice(pr.getPrice());
                    productResponseDto.setProductDescription(pr.getDescription());

                    productResponseList.add(productResponseDto);

                }

                orderResponseDto.setProducts(productResponseList);
                orderResponseList.add(orderResponseDto);

            }

            return ResponseEntity.ok(orderResponseList);

        }catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<OrderResponseDto> getActiveOrder() {

        if(!orderRepository.getActiveOrder().isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No order found");
        }else{
            Order order = orderRepository.getActiveOrder().get();


            try{
                OrderResponseDto orderResponseDto = new OrderResponseDto();
                List<ProductResponseDto> productList = new ArrayList<>();
                List<Product> products = getProductPerOrder(order);

                orderResponseDto.setOrderId(order.getId());

                for(Product product : products){
                    ProductResponseDto productResponseDto = new ProductResponseDto();
                    productResponseDto.setProductId(product.getId());
                    productResponseDto.setProductAvailable(product.getAvailable());
                    productResponseDto.setProductPrice(product.getPrice());
                    productResponseDto.setProductName(product.getName());
                    productResponseDto.setProductDescription(product.getDescription());
                    productList.add(productResponseDto);
                }

                orderResponseDto.setProducts(productList);
                orderResponseDto.setTotalPrice(order.getTotalPrice());

                return ResponseEntity.ok(orderResponseDto);

            }catch (ResponseStatusException e){
                return ResponseEntity.status(e.getStatus()).body(null);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
    }

    private void validateAvalaible(List<Product> products){

        List<Product> updateProducts = new ArrayList();

        for(Product product : products){
            int avaliable = product.getAvailable();
            product.setAvailable(avaliable);
        }

        productRepository.saveAll(updateProducts);

    }
    private Order createOrUpdateOrder(OrderRequestDto orderRequest) {
        Order order = new Order();
        order.setId(orderRequest.getOrderId());
        order.setActive(true);
        order.setTotalPrice(orderRequest.getTotalPrice());

        List<Integer> ids = new ArrayList<>();
        for (ProductRequestDto pDto : orderRequest.getProducts()) {
            ids.add(pDto.getProductId());
        }

        List<Product> products = productRepository.findAllById(ids);
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (Product product : products) {

            OrderProductId orderProductId = new OrderProductId(order.getId(), product.getId());
            OrderProduct orderProduct = orderProductRepository.findById(orderProductId.getOrderId(),orderProductId.getProductId())
                    .orElse(new OrderProduct());


            orderProduct.setOrder(order);
            orderProduct.setProduct(product);


            for (ProductRequestDto productRequestDto : orderRequest.getProducts()) {
                if (product.getId() == productRequestDto.getProductId()) {
                    orderProduct.setProductQuantity(productRequestDto.getProductQuantity());
                    orderProduct.setProductTotal(productRequestDto.getProductTotalPrice());
                }
            }

            orderProductList.add(orderProduct);
        }

        order.setOrderProducts(orderProductList);
        return order;
    }


    private List<Product> getProductPerOrder(Order order){

        List<Integer> productsId = new ArrayList<>();

        for(OrderProduct orderProduct : order.getOrderProducts()){
            int id = orderProduct.getProduct().getId();
            productsId.add(id);
        }

        if(!productsId.isEmpty()){
            return productRepository.findAllById(productsId);
        }else{
            return null;
        }

    }

    private void inactiveOrders(OrderRequestDto orderRequest){

        if(orderRequest.getOrderId() !=0){
            List<Order> inactiveOrders = orderRepository.getNotCurrentOrders(orderRequest.getOrderId()).get();

            for(Order order : inactiveOrders){
                order.setActive(false);
            }
            orderRepository.saveAll(inactiveOrders);
        }
    }
}
