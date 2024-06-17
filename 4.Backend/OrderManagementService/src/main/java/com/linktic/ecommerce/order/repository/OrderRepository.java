package com.linktic.ecommerce.order.repository;

import com.linktic.ecommerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.active = false WHERE o.id <> ?1")
    int inactiveOrder(int id);

    @Query("SELECT o FROM Order o WHERE o.active = true")
    Optional<Order> getActiveOrder();

    @Query("SELECT o FROM Order o WHERE o.active = true AND o.id <> ?1")
    Optional<List<Order>> getNotCurrentOrders(int id);

}
