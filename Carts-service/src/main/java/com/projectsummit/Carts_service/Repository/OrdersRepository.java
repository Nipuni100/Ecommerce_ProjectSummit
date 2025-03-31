package com.projectsummit.Carts_service.Repository;

import com.projectsummit.Carts_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository
        extends JpaRepository<Order, Long> {


    Optional<Order> findByCustomerId(int customerId);
}
