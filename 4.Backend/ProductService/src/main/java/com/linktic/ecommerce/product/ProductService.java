package com.linktic.ecommerce.product;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.linktic.ecommerce.product"})
public class ProductService
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProductService.class, args);
    }
}
