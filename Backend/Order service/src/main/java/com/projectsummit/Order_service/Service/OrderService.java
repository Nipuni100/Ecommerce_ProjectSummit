package com.projectsummit.Order_service.Service;

import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
//import com.projectsummit.Order_service.Entity.Order;
import com.projectsummit.Order_service.DTOs.OrderStatusDTO;
import com.projectsummit.Order_service.Entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    Page<OrderResponseDTO> getAllOrders(Pageable pageable);
    void cancelOrder(int orderId, OrderStatusDTO statusDTO);
//    void cancelOrder(int orderId);

    Orders findOrderById(int orderId);

//    OrderResponseDTO getOrderByCustomerId(int customerId);
//    List<Orders> findAllByCustomerId(int customerId);
    List<OrderResponseDTO> getOrderByCustomerId(int customerId);

    // OrderResponseDTO createOrder(int cartId, String paymentMethod, String orderStatus, List<Integer> cartItemIds);
}
