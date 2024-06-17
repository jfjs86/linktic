package com.linktic.ecommerce.order.repository;

import com.linktic.ecommerce.order.model.OrderProduct;
import com.linktic.ecommerce.order.model.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Integer> {
    @Query("SELECT op FROM OrderProduct op WHERE order.id =?1 and product.id =?2")
    Optional<OrderProduct> findById(int orderId, int productId);
}
