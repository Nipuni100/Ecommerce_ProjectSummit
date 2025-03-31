package com.projectsummit.Carts_service.Service;

import com.projectsummit.Carts_service.DTOs.*;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.Entity.Order;
import com.projectsummit.Carts_service.Entity.OrderItem;
import com.projectsummit.Carts_service.ExceptionHandling.OrderNotFoundException;
import com.projectsummit.Carts_service.ExceptionHandling.ResourceNotFoundException;
import com.projectsummit.Carts_service.Mappers.CartItemResponseDTOMapper;
import com.projectsummit.Carts_service.Repository.CartItemRepository;
import com.projectsummit.Carts_service.Repository.CartsRepository;
import com.projectsummit.Carts_service.Repository.OrderItemsRepository;
import com.projectsummit.Carts_service.Repository.OrdersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartsService {
    private final CartItemRepository cartItemRepository;
    private CartsRepository cartsRepository;
    private final OrdersRepository ordersRepository;
    private CartItemResponseDTOMapper cartItemResponseDTOMapper;
    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public CartsService(CartsRepository cartsRepository,
                        CartItemRepository cartItemRepository, OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository) {
        this.cartsRepository = cartsRepository;
        this.cartItemRepository = cartItemRepository;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;

    }

    // CARTS METHODS
    public CartResponseDTO getCartById(int cartId) {
        Cart cart = cartsRepository.findById((long) cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for ID: " + cartId));
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        return mapToCartResponseDTO(cart, cartItems);
    }


    public CartResponseDTO getCartByCustomerId(int customerId) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        return mapToCartResponseDTO(cart, cartItems);
    }


    public void addItemToCart(CartItem cartItem, int customerId) {

        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseGet(() -> {
                    Cart newCart = new Cart(customerId);
                    newCart.setStatus("ACTIVE");
                    return cartsRepository.save(newCart);
                });
        cartItem.setCartId(cart.getCartId());

        cart.getItems().add(cartItem);
        cartItemRepository.save(cartItem);

//        List<CartItem> updatedItems = cartItemRepository.findByCartId(cart.getCartId());
//        cart.setItems(updatedItems);

        cartsRepository.save(cart);
    }


    public void removeItemFromCart(int customerId, int itemId) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findById((long) itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + itemId));

        if (cartItem.getCartId() != (int) cart.getCartId()) {
            throw new IllegalArgumentException("Item does not belong to the customer's active cart.");
        }
        cartItemRepository.delete(cartItem);
    }


    public CartItem updateItemQuantity(int customerId, int itemId, int newQuantity) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findById((long) itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + itemId));

        if (cartItem.getCartId() != cart.getCartId()) {
            throw new IllegalArgumentException("Item does not belong to the customer's active cart.");
        }
        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    public void emptyCart(int customerId) {
        Optional<Cart> optionalCart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE");

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().clear();
            cartsRepository.save(cart);
        } else {
            throw new ResourceNotFoundException("Active cart not found for customer ID: " + customerId);
        }
    }

    public List<CartResponseDTO> getAllCarts() {
        List<Cart> carts = cartsRepository.findAll();

        return carts.stream()
                .map(cart -> {
                    List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
                    return mapToCartResponseDTO(cart, cartItems);
                })
                .collect(Collectors.toList());
    }

    //MAPPERS

    private CartResponseDTO mapToCartResponseDTO(Cart cart, List<CartItem> cartItems) {
        List<CartItemDTO> cartItemDTOs = cartItems.stream()
                .map(item -> new CartItemDTO(item.getCartItemId(), item.getProdName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());

        return new CartResponseDTO(
                cart.getCartId(),
                cart.getCustomerId(),
                cartItemDTOs
        );
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


//    ORDER METHODS

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = ordersRepository.findAll();

        return ordersRepository.findAll()
                .stream()
                .map(order -> {
                            List<OrderItem> orderItems = orderItemsRepository.findByOrderId(order.getOrderId());
                            return mapToOrderResponseDTO(order, orderItems);
                        }
                )
                .collect(Collectors.toList());

    }

    public ResponseEntity<Order> getOrderById(int customId) {
        Optional<Order> order = ordersRepository.findById((long)customId);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.notFound().build();
    }


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

    public OrderResponseDTO createOrder(int cartId, String paymentMethod, String orderStatus, List<Integer> cartItemIds) {

        Cart cart = cartsRepository.findById((long)cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

        if (!"ACTIVE".equalsIgnoreCase(cart.getStatus())) {
            throw new IllegalArgumentException("Cart is not active");
        }

        List<CartItem> selectedItems = cart.getItems().stream()
                .filter(item -> cartItemIds.contains(item.getCartItemId()))
                .collect(Collectors.toList());

        if (selectedItems.isEmpty()) {
            throw new IllegalArgumentException("No valid cart items selected for the order");
        }

        Order orderDetails = new Order();
        orderDetails.setCustomerId(cart.getCustomerId());
        orderDetails.setCartId(cartId); // Associate cart with the order
        orderDetails.setOrderStatus(orderStatus);
        orderDetails.setPaymentMethod(paymentMethod);
        orderDetails.setTotalPrice((float)selectedItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum());
        orderDetails.setNumOfItems(selectedItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum());

        Order savedOrder = ordersRepository.save(orderDetails);

        List<OrderItem> orderItems = selectedItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(savedOrder.getOrderId());
                    orderItem.setProdName(cartItem.getProdName());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());
        orderItemsRepository.saveAll(orderItems);

        selectedItems.forEach(item -> item.setStatus("IN_ORDER"));
        cart.setStatus("DEACTIVE");
        cartItemRepository.saveAll(selectedItems);
        cartsRepository.save(cart);

        List<CartItem> remainingItems = cart.getItems().stream()
                .filter(item -> !"IN_ORDER".equalsIgnoreCase(item.getStatus()))
                .collect(Collectors.toList());

        if (!remainingItems.isEmpty()) {
            Cart newCart = new Cart(cart.getCustomerId());
            newCart.setStatus("ACTIVE");
            Cart savedNewCart = cartsRepository.save(newCart);

            remainingItems.forEach(item -> item.setCartId(savedNewCart.getCartId()));
            cartItemRepository.saveAll(remainingItems);
        }

        return mapToOrderResponseDTO(savedOrder, orderItems);
    }

}