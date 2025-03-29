package com.projectsummit.Carts_service.Service;

import com.projectsummit.Carts_service.DTOs.CartItemDTO;
import com.projectsummit.Carts_service.DTOs.CartItemResponseDTO;
import com.projectsummit.Carts_service.DTOs.CartResponseDTO;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.ExceptionHandling.ResourceNotFoundException;
import com.projectsummit.Carts_service.Mappers.CartItemResponseDTOMapper;
import com.projectsummit.Carts_service.Repository.CartItemRepository;
import com.projectsummit.Carts_service.Repository.CartsRepository;
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
//    private CartResponseDTOMapper cartResponseDTOMapper;
    private CartItemResponseDTOMapper cartItemResponseDTOMapper;

    @Autowired
    public CartsService(CartsRepository cartsRepository,
                        CartItemRepository cartItemRepository) {
        this.cartsRepository = cartsRepository;
        this.cartItemRepository = cartItemRepository;
    }


//    public <List<CartResponseDTO>> getAllCarts() {
//        return cartsRepository.findAll()
//                .stream()
//                .map(cartResponseDTOMapper)
//                .toList();
//    }


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
        cartsRepository.save(cart);
    }




    public void removeItemFromCart(int customerId, int itemId) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findById((long)itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + itemId));

        if (cartItem.getCartId() != (int) cart.getCartId()) {
            throw new IllegalArgumentException("Item does not belong to the customer's active cart.");
        }
        cartItemRepository.delete(cartItem);
    }


    public CartItem updateItemQuantity(int customerId, int itemId, int newQuantity) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findById((long)itemId)
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

}
