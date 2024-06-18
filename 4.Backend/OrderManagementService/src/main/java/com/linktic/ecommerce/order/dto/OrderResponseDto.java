package com.linktic.ecommerce.order.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponseDto {

    private int orderId;

    private List<ProductRequestDto> products;

    private BigDecimal totalPrice;

    private Boolean isActive;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<ProductRequestDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequestDto> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
