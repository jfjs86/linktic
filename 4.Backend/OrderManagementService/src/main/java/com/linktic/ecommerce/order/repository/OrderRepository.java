package com.linktic.ecommerce.order.repository;

import com.linktic.ecommerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT o FROM Order o WHERE o.active = true")
    Optional<Order> getActiveOrder();

    @Query("SELECT o FROM Order o WHERE o.active = true AND o.id <> ?1")
    Optional<List<Order>> getNotCurrentOrders(int id);

}
