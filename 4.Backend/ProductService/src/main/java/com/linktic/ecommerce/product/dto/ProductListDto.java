package com.linktic.ecommerce.product.dto;

import java.util.List;

public class ProductListDto {

    List<ProductResponseDto> productList;

    public List<ProductResponseDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductResponseDto> productList) {
        this.productList = productList;
    }
}
