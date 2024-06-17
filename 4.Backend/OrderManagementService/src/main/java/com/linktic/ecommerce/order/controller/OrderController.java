package com.linktic.ecommerce.order.controller;

import com.linktic.ecommerce.order.dto.OrderRequestDto;
import com.linktic.ecommerce.order.dto.OrderResponseDto;
import com.linktic.ecommerce.order.resources.ControllerResource;
import com.linktic.ecommerce.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ControllerResource.ROOT)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(ControllerResource.HEALTHCHECK)
    public ResponseEntity<Integer> healthcheck() {
        return ResponseEntity.ok(200);
    }

    @PostMapping(ControllerResource.SAVE_ORDER)
    public ResponseEntity<OrderResponseDto> saveOrder(@RequestBody OrderRequestDto orderRequestDto){
        return orderService.saveOrder(orderRequestDto);
    }

    @GetMapping(ControllerResource.GET_ORDERS)
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping(ControllerResource.GET_ACTIVE_ORDER)
    public ResponseEntity<OrderResponseDto> getActiveOrder(){
        return orderService.getActiveOrder();
    }

}
