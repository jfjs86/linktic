package com.linktic.ecommerce.product.controller;

import com.linktic.ecommerce.product.dto.ProductListDto;
import com.linktic.ecommerce.product.resources.ControllerResource;
import com.linktic.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerResource.ROOT)
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(ControllerResource.HEALTHCHECK)
    public ResponseEntity<Integer> healthcheck() {
        return ResponseEntity.ok(200);
    }

    @GetMapping(ControllerResource.GET_PRODUCTS)
    public ResponseEntity<ProductListDto> getProducts() {
        return productService.getAllProducts();
    }

}
