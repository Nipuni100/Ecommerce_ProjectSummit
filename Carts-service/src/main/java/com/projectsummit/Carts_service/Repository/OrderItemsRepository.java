package com.projectsummit.Carts_service.Repository;
import com.projectsummit.Carts_service.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(int orderId);

}