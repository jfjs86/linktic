package com.linktic.ecommerce.product.service.impl;

import com.linktic.ecommerce.product.dto.ProductListDto;
import com.linktic.ecommerce.product.dto.ProductResponseDto;
import com.linktic.ecommerce.product.model.Product;
import com.linktic.ecommerce.product.repository.ProductRepository;
import com.linktic.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseEntity<ProductListDto> getAllProducts() {

        try{
            List<Product> products = productRepository.findAll();

            if(products == null || products.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found");
            }

            List<ProductResponseDto> productResponseDtoList =  new ArrayList<>();
            for(Product product: products){
                ProductResponseDto pr = new ProductResponseDto();
                pr.setProductId(product.getId());
                pr.setProductName(product.getName());
                pr.setProductDescription(product.getDescription());
                pr.setProductPrice(product.getPrice());
                pr.setProductAvailable(product.getAvailable());
                pr.setProductImageUrl(product.getImagePath());

                productResponseDtoList.add(pr);

            }

            ProductListDto productListDto = new ProductListDto();
            productListDto.setProductList(productResponseDtoList);
            return ResponseEntity.ok(productListDto);

        }catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
