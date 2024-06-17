package com.linktic.ecommerce.product.service;

import com.linktic.ecommerce.product.dto.ProductListDto;
import org.springframework.http.ResponseEntity;

public interface ProductService {

    ResponseEntity<ProductListDto> getAllProducts();



}
