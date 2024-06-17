package com.linktic.ecommerce.order.repository;


import com.linktic.ecommerce.order.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
