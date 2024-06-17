package com.linktic.ecommerce.order.service;

import com.linktic.ecommerce.order.dto.OrderRequestDto;
import com.linktic.ecommerce.order.dto.OrderResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    ResponseEntity<OrderResponseDto> saveOrder(OrderRequestDto orderRequest);

    ResponseEntity<List<OrderResponseDto>> getAllOrders();

    ResponseEntity<OrderResponseDto> getActiveOrder();

}
