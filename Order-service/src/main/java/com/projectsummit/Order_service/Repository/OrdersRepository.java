package com.projectsummit.Order_service.Repository;

import com.projectsummit.Order_service.Entity.Orders;
import com.projectsummit.Order_service.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository
        extends JpaRepository<Orders, Long> {


    Optional<Orders> findByCustomerId(int customerId);
    List<Orders> findAllByCustomerId(int customerId);

}
