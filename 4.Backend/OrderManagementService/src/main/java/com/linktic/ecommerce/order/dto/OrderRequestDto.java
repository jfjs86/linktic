package com.linktic.ecommerce.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderRequestDto implements Serializable {

    private int orderId;

    private BigDecimal totalPrice;

    private List<ProductRequestDto> products;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductRequestDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequestDto> products) {
        this.products = products;
    }
}
