package com.projectsummit.Order_service.Service;

import com.projectsummit.Order_service.DTOs.OrderItemDTO;
import com.projectsummit.Order_service.DTOs.OrderResponseDTO;
import com.projectsummit.Order_service.DTOs.OrderStatusDTO;
import com.projectsummit.Order_service.DTOs.ProductDTOforOrder;
import com.projectsummit.Order_service.Entity.Orders;
import com.projectsummit.Order_service.Entity.OrderItem;
import com.projectsummit.Order_service.Entity.Orders;
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

    private OrderResponseDTO mapToOrderResponseDTO(Orders order, List<OrderItem> orderItems) {
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
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);

        return ordersPage.map(order -> {
            List<OrderItem> orderItems = orderItemsRepository.findByOrderId(order.getOrderId());
            return mapToOrderResponseDTO(order, orderItems);
        });
    }

    public OrderResponseDTO createOrder(int customerId, int cartId, String paymentMethod, String orderStatus, List<ProductDTOforOrder> productList) {
        if (productList == null || productList.isEmpty()) {
            throw new IllegalArgumentException("Product list cannot be empty");
        }


        float totalPrice = (float) productList.stream()
                .mapToDouble(product -> product.price() * product.quantity())
                .sum();

        int numOfItems = productList.stream()
                .mapToInt(ProductDTOforOrder::quantity)
                .sum();

        Orders order = new Orders();
        order.setCustomerId(customerId);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus(orderStatus);
        order.setTotalPrice(totalPrice);
        order.setNumOfItems(numOfItems);
        order.setCartId(cartId);

        Orders savedOrder = ordersRepository.save(order);

        List<OrderItem> orderItems = productList.stream()
                .map(product -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(savedOrder.getOrderId());
                    orderItem.setProdName(product.prodName());
                    orderItem.setPrice(product.price());
                    orderItem.setQuantity(product.quantity());
                    orderItem.setProdID(product.prodId());
                    return orderItem;
                })
                .collect(Collectors.toList());

        orderItemsRepository.saveAll(orderItems);

        return mapToOrderResponseDTO(savedOrder, orderItems);
    }


//    public void cancelOrder(int orderId) {
//        Orders order = ordersRepository.findById((long)orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//
//        order.setOrderStatus("CANCELLED");
//        ordersRepository.save(order);
//    }
public void cancelOrder(int orderId, OrderStatusDTO statusDTO) {
    Orders order = ordersRepository.findById((long) orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    order.setOrderStatus(statusDTO.status());
    ordersRepository.save(order);
}


    public Orders findOrderById(int orderId) {
        Optional<Orders> orderOptional = ordersRepository.findById((long)orderId);

        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
        }
    }

    public List<OrderResponseDTO> getOrderByCustomerId(int customerId) {
        List<Orders> orders = ordersRepository.findAllByCustomerId(customerId);

        return orders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemsRepository.findByOrderId(order.getOrderId());
                    return mapToOrderResponseDTO(order, orderItems);
                })
                .collect(Collectors.toList());
    }
    public boolean isOrderExists(int orderId) {
        return ordersRepository.existsById((long)orderId); // Assuming `ordersRepository` is your JPA repository
    }


}
