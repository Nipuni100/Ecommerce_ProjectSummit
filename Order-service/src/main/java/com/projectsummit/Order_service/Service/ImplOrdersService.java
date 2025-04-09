package com.projectsummit.Order_service.Service;

import com.projectsummit.Order_service.DTOs.OrderItemDTO;
import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
import com.projectsummit.Order_service.Entity.Order;
import com.projectsummit.Order_service.Entity.OrderItem;
import com.projectsummit.Order_service.ExceptionHandling.OrderNotFoundException;
import com.projectsummit.Order_service.ExceptionHandling.ResourceNotFoundException;
import com.projectsummit.Order_service.Repository.OrderItemsRepository;
import com.projectsummit.Order_service.Repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImplOrdersService implements OrderService{
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public ImplOrdersService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order, List<OrderItem> orderItems) {
        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                .map(item -> new OrderItemDTO(item.getOrderItemId(), item.getProdName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getOrderId(),
                order.getCustomerId(),
                order.getPaymentMethod(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                orderItemDTOs
        );
    }

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        Page<Order> ordersPage = ordersRepository.findAll(pageable);

        return ordersPage.map(order -> {
            List<OrderItem> orderItems = orderItemsRepository.findByOrderId(order.getOrderId());
            return mapToOrderResponseDTO(order, orderItems);
        });
    }


//    public ResponseEntity<Order> getOrderById(int customId) {
//        Optional<Order> order = ordersRepository.findById((long)customId);
//        if (order.isPresent()) {
//            return ResponseEntity.ok(order.get());
//        }
//        return ResponseEntity.notFound().build();
//    }

//    public OrderResponseDTO createOrder(int cartId, String paymentMethod, String orderStatus, List<Integer> cartItemIds) {
//
//        Cart cart = cartsRepository.findById((long)cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));
//
//        if (!"ACTIVE".equalsIgnoreCase(cart.getStatus())) {
//            throw new IllegalArgumentException("Cart is not active");
//        }
//
//        List<CartItem> selectedItems = cart.getItems().stream()
//                .filter(item -> cartItemIds.contains(item.getCartItemId()))
//                .collect(Collectors.toList());
//
//        if (selectedItems.isEmpty()) {
//            throw new IllegalArgumentException("No valid cart items selected for the order");
//        }
//
//        Order orderDetails = new Order();
//        orderDetails.setCustomerId(cart.getCustomerId());
//        orderDetails.setCartId(cartId); // Associate cart with the order
//        orderDetails.setOrderStatus(orderStatus);
//        orderDetails.setPaymentMethod(paymentMethod);
//        orderDetails.setTotalPrice((float)selectedItems.stream()
//                .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                .sum());
//        orderDetails.setNumOfItems(selectedItems.stream()
//                .mapToInt(CartItem::getQuantity)
//                .sum());
//
//        Order savedOrder = ordersRepository.save(orderDetails);
//
//        List<OrderItem> orderItems = selectedItems.stream()
//                .map(cartItem -> {
//                    OrderItem orderItem = new OrderItem();
//                    orderItem.setOrderId(savedOrder.getOrderId());
//                    orderItem.setProdName(cartItem.getProdName());
//                    orderItem.setPrice(cartItem.getPrice());
//                    orderItem.setQuantity(cartItem.getQuantity());
//                    return orderItem;
//                })
//                .collect(Collectors.toList());
//        orderItemsRepository.saveAll(orderItems);
//
//        selectedItems.forEach(item -> item.setStatus("IN_ORDER"));
//        cart.setStatus("DEACTIVE");
//        cartItemRepository.saveAll(selectedItems);
//        cartsRepository.save(cart);
//
//        List<CartItem> remainingItems = cart.getItems().stream()
//                .filter(item -> !"IN_ORDER".equalsIgnoreCase(item.getStatus()))
//                .collect(Collectors.toList());
//
//        if (!remainingItems.isEmpty()) {
//            Cart newCart = new Cart(cart.getCustomerId());
//            newCart.setStatus("ACTIVE");
//            Cart savedNewCart = cartsRepository.save(newCart);
//
//            remainingItems.forEach(item -> item.setCartId(savedNewCart.getCartId()));
//            cartItemRepository.saveAll(remainingItems);
//        }
//
//        return mapToOrderResponseDTO(savedOrder, orderItems);
//    }

    public void cancelOrder(int orderId) {
        Order order = ordersRepository.findById((long)orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setOrderStatus("CANCELLED");

        ordersRepository.save(order);
    }

    public Order findOrderById(int orderId) {
        Optional<Order> orderOptional = ordersRepository.findById((long)orderId);

        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
        }
    }

    public OrderResponseDTO getOrderByCustomerId(int customerId) {

        Optional<Order> orderOptional = ordersRepository.findByCustomerId(customerId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderItem> orderItems = orderItemsRepository.findByOrderId(order.getOrderId());
            return mapToOrderResponseDTO(order, orderItems);
        }

        return null;
    }


}
