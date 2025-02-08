package com.projectsummit.Order_service.Repository;

import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
import com.projectsummit.Order_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository
        extends JpaRepository<Order, Long> {


}
