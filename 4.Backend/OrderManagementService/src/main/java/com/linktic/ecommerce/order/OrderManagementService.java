package com.linktic.ecommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.linktic.ecommerce.order"})
public class OrderManagementService
{
    public static void main( String[] args )
    {
        SpringApplication.run(OrderManagementService.class, args);
    }
}
