package com.projectsummit.Order_service.Repository;

//import com.projectsummit.Order_service.Entity.Order;
import com.projectsummit.Order_service.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(int orderId);
}
