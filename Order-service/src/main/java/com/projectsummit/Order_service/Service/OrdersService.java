package com.projectsummit.Order_service.Service;

import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
import com.projectsummit.Order_service.Entity.Order;
import com.projectsummit.Order_service.ExceptionHandling.OrderNotFoundException;
import com.projectsummit.Order_service.Mappers.OrderResponseMapper;
import com.projectsummit.Order_service.Repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
//    private final OrderResponseMapper orderResponseMapper;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
//        this.orderResponseMapper = orderResponseMapper;
    }


//    public List<OrderResponseDTO> getAllOrders() {
//        return ordersRepository.findAll()
//                .stream()
//                .map(orderResponseMapper)
//                .collect(Collectors.toList());
//    }

    public List<OrderResponseDTO> getAllOrders() {
        return ordersRepository.findAll()
                .stream()
                .map(order-> new OrderResponseDTO(
                        order.getOrderId(),
                        order.getCustomId(),
                        order.getPaymentMethod(),
                        order.getNumOfItems(),
                        order.getTotalPrice(),
                        order.getOrderStatus())
                )
                .collect(Collectors.toList());

    }

    public ResponseEntity<Order> getOrderById(Long customId) {
        Optional<Order> order = ordersRepository.findById(customId);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.notFound().build();
    }

    public Order addOrder(Order order) {
        ordersRepository.save(order);
        return order;
    }

    public boolean cancelOrder(Long orderId) {
        Optional<Order> order = ordersRepository.findById(orderId);
        if (order.isPresent()) {
            ordersRepository.delete(order.get());
            return true;
        }
        return false;
    }

    public Order findOrderById(Long orderId) {
        Optional<Order> orderOptional = ordersRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
        }
    }


}
