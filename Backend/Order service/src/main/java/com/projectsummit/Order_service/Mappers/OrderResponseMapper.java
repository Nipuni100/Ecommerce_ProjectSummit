//package com.projectsummit.Order_service.Mappers;
//
//import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
//import com.projectsummit.Order_service.Entity.Order;
//import org.springframework.cglib.core.internal.Function;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@Component
//public class OrderResponseMapper implements Function<Order, OrderResponseDTO> {
//    @Override
//    public OrderResponseDTO apply(Order order) {
//        // Transform Order to OrderResponse (this is just an example)
//        return new OrderResponseDTO(
//                order.getOrderId(),
//                order.getCustomerId(),
//                order.getPaymentMethod(),
//                order.getNumOfItems(),
//                order.getTotalPrice(),
//                order.getOrderStatus()
//        );
//    }
//}