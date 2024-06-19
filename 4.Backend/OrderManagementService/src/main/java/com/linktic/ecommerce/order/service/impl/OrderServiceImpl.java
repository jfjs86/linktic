package com.linktic.ecommerce.order.service.impl;

import com.linktic.ecommerce.order.dto.OrderRequestDto;
import com.linktic.ecommerce.order.dto.OrderResponseDto;
import com.linktic.ecommerce.order.dto.ProductRequestDto;
import com.linktic.ecommerce.order.model.Order;
import com.linktic.ecommerce.order.model.OrderProduct;
import com.linktic.ecommerce.order.model.OrderProductId;
import com.linktic.ecommerce.order.model.Product;
import com.linktic.ecommerce.order.repository.OrderProductRepository;
import com.linktic.ecommerce.order.repository.OrderRepository;
import com.linktic.ecommerce.order.repository.ProductRepository;
import com.linktic.ecommerce.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public ResponseEntity<OrderResponseDto> saveOrder(OrderRequestDto orderRequest) {
        Order order = createOrUpdateOrder(orderRequest);
        order = orderRepository.save(order);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        inactiveOrders(order);
        List<Product> products = getProductPerOrder(order);

        if (products == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getId());
        orderResponseDto.setProducts(generateProductResponse(order));
        orderResponseDto.setTotalPrice(order.getTotalPrice());
        orderResponseDto.setActive(order.getActive());
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
                orderResponseDto.setProducts(generateProductResponse(or));
                orderResponseDto.setTotalPrice(or.getTotalPrice());
                orderResponseDto.setActive(or.getActive());
                orderResponseList.add(orderResponseDto);

            }

            Collections.sort(orderResponseList, Comparator.comparing(OrderResponseDto::getActive).reversed());

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

                orderResponseDto.setOrderId(order.getId());
                orderResponseDto.setProducts(generateProductResponse(order));
                orderResponseDto.setTotalPrice(order.getTotalPrice());
                orderResponseDto.setActive(order.getActive());

                return ResponseEntity.ok(orderResponseDto);

            }catch (ResponseStatusException e){
                return ResponseEntity.status(e.getStatus()).body(null);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
    }

    @Override
    public ResponseEntity<OrderResponseDto> paidOrder(OrderRequestDto orderRequest) {

        Optional<Order> orderOptional = orderRepository.findById(orderRequest.getOrderId());

        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            order.setActive(Boolean.FALSE);
            order = orderRepository.save(order);

            if(order != null){

                OrderResponseDto orderResponseDto = new OrderResponseDto();
                orderResponseDto.setOrderId(order.getId());
                orderResponseDto.setProducts(generateProductResponse(order));
                orderResponseDto.setTotalPrice(order.getTotalPrice());
                orderResponseDto.setActive(order.getActive());

                return ResponseEntity.ok(orderResponseDto);

            }else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Error");
            }


        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not order found");
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

        if(orderRequest.getProducts().size() !=0 ){

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

        }else{
            order.setOrderProducts(new ArrayList<>());
        }

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

    private void inactiveOrders(Order savedOrder){

        Optional<List<Order>> inactiveOtional = orderRepository.getNotCurrentOrders(savedOrder.getId());

        if(inactiveOtional.isPresent()){
            List<Order> inactiveOrders = inactiveOtional.get();

            for(Order order : inactiveOrders){
                order.setActive(Boolean.FALSE);
            }
            orderRepository.saveAll(inactiveOrders);
        }
    }

    private List<ProductRequestDto> generateProductResponse(Order order){
        List<ProductRequestDto> productRequestList = new ArrayList<>();
        List<Product> products = getProductPerOrder(order);

        for(Product product : products){
            ProductRequestDto productRequestDto = new ProductRequestDto();
            productRequestDto.setProductId(product.getId());
            productRequestDto.setProductName(product.getName());
            productRequestDto.setProductPrice(product.getPrice());
            productRequestDto.setProductDescription(product.getDescription());
            productRequestDto.setProductImageUrl(product.getImagePath());

            for(OrderProduct orderProduct : order.getOrderProducts()){
                if(product.getId() == orderProduct.getProduct().getId()){
                    productRequestDto.setProductQuantity(orderProduct.getProductQuantity());
                    productRequestDto.setProductTotalPrice(orderProduct.getProductTotal());
                }
            }

            productRequestList.add(productRequestDto);
        }

        return productRequestList;
    }
}
