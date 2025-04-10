package com.projectsummit.Order_service.Controller;

import com.projectsummit.Order_service.DTOs.OrderRequestDTO;
import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
import com.projectsummit.Order_service.DTOs.OrderStatusDTO;
import com.projectsummit.Order_service.ExceptionHandling.ResourceNotFoundException;
import com.projectsummit.Order_service.Service.ImplOrdersService;
//import com.projectsummit.Order_service.Service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(path="/api/v1/orders")
public class OrdersContoller {
    private  final ImplOrdersService ordersService;
    private static final Logger logger = LoggerFactory.getLogger(OrdersContoller.class);


    @Autowired
    public OrdersContoller(ImplOrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(Pageable pageable) {
        logger.info("Received request to fetch all orders with pagination.");
        try{
        Page<OrderResponseDTO> orders = ordersService.getAllOrders(pageable);
            logger.info("Successfully fetched {} orders.", orders.getTotalElements());
        return ResponseEntity.ok(orders);
    }
        catch (Exception e) {
            logger.error("Unexpected error occurred while fetching orders: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Page.empty());
        }}


    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByCustomerId(@PathVariable int customerId) {
        logger.info("Received request to fetch orders for Customer ID: {}", customerId);
        try{
        List<OrderResponseDTO> orderResponseDTOs = ordersService.getOrderByCustomerId(customerId);

        if (orderResponseDTOs != null && !orderResponseDTOs.isEmpty()) {
            logger.info("Orders found for Customer ID: {}", customerId);
            return ResponseEntity.ok(orderResponseDTOs);
        } else {
            logger.warn("No orders found for Customer ID: {}", customerId);
            throw new ResourceNotFoundException("No orders found for Customer ID: " + customerId);

        }} catch (ResourceNotFoundException e) {
            logger.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }}

    @PostMapping("customers/{customerId}")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable int customerId,
            @RequestBody OrderRequestDTO orderRequestDTO) {

            logger.info("Received request to create order for Customer ID: {}", customerId);
    try{
            OrderResponseDTO responseDTO = ordersService.createOrder(
                customerId,
                orderRequestDTO.cartId(),
                orderRequestDTO.paymentMethod(),
                orderRequestDTO.orderStatus(),
                orderRequestDTO.productList()
        );
    if (responseDTO == null) {
        logger.error("Order creation failed for Customer ID: {}", customerId);
        throw new RuntimeException("Order creation failed due to an unexpected error.");
    }

    logger.info("Order successfully created with ID: {} for Customer ID: {}", responseDTO.orderId(), customerId);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

     }catch (ResourceNotFoundException e) {
    logger.error("Error: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}}

    @PatchMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId, @RequestBody OrderStatusDTO orderStatusDTO) {
        logger.info("Received request to cancel order with ID: {}", orderId);

        try {
            boolean isOrderExists = ordersService.isOrderExists(orderId); // Assuming this method exists in the service layer
            if (!isOrderExists) {
                logger.error("Order with ID: {} not found.", orderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            ordersService.cancelOrder(orderId);
            logger.info("Order with ID: {} successfully canceled. Status updated to: {}", orderId, orderStatusDTO.status());
            return ResponseEntity.status(HttpStatus.OK).body("Order status updated successfully to " + orderStatusDTO.status());
        }
        catch (IllegalArgumentException ex) {
            logger.error("Invalid input while canceling order with ID: {}. Error: {}", orderId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    }

}
