package com.projectsummit.Order_service.Controller;

import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
import com.projectsummit.Order_service.Entity.Order;
import com.projectsummit.Order_service.Service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/v1/orders")
public class OrdersContoller {
    private  final OrdersService ordersService;

    @Autowired
    public OrdersContoller(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<List <OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int customerId) {
        return ordersService.getOrderById((long)customerId);

    }

    @PostMapping("/{customerId}")
    public ResponseEntity<Order> createOrder(@PathVariable int customerId, @RequestBody Order order) {
        Order savedOrder = ordersService.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);

    }

    @DeleteMapping("/{customerId}/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int customerId, @PathVariable Long orderId) {
        Order order = ordersService.findOrderById(orderId);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        if (order.getCustomerId() != customerId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This order does not belong to the customer");
        }

        ordersService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Order canceled successfully");
    }

}
