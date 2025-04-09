package com.projectsummit.Carts_service.Service;

import com.projectsummit.Carts_service.DTOs.*;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.ExceptionHandling.ResourceNotFoundException;
import com.projectsummit.Carts_service.Mappers.CartItemResponseDTOMapper;
import com.projectsummit.Carts_service.Repository.CartItemRepository;
import com.projectsummit.Carts_service.Repository.CartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ImplCartsService implements CartsService {
    private final CartItemRepository cartItemRepository;
    private CartsRepository cartsRepository;
    private CartItemResponseDTOMapper cartItemResponseDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(ImplCartsService.class);

    @Autowired
    public ImplCartsService(CartsRepository cartsRepository,
                        CartItemRepository cartItemRepository) {
        this.cartsRepository = cartsRepository;
        this.cartItemRepository = cartItemRepository;

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


    public void addItemToCart(CartItemRequestDTO cartItemRequestDTO, int customerId) {
        // Find or create an active cart
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseGet(() -> {
                    Cart newCart = new Cart(customerId);
                    newCart.setStatus("ACTIVE");
                    return cartsRepository.save(newCart);
                });

        // Convert DTO to entity
        CartItem cartItem = new CartItem();
        cartItem.setCartId(cart.getCartId());
        cartItem.setProdName(cartItemRequestDTO.prodName());
        cartItem.setPrice(cartItemRequestDTO.price());
        cartItem.setBrand(cartItemRequestDTO.brand());
        cartItem.setQuantity(cartItemRequestDTO.quantity());
        cartItem.setStatus(cartItemRequestDTO.status());
        cartItem.setProdId(cartItemRequestDTO.prodId());

        // Save item and update cart
        cart.getItems().add(cartItem);
        cartItemRepository.save(cartItem);
        cartsRepository.save(cart);
    }



    public void removeItemFromCart(int customerId, int prodId) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findById((long) prodId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + prodId));

        if (cartItem.getCartId() != (int) cart.getCartId()) {
            throw new IllegalArgumentException("Item does not belong to the customer's active cart.");
        }
        cartItemRepository.delete(cartItem);
    }


    public CartItem updateItemQuantity(int customerId, int prodId, int newQuantity) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findById((long) prodId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + prodId));

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

    public Page<CartResponseDTO> getAllCarts(Pageable pageable) {
        Page<Cart> cartsPage = cartsRepository.findAll(pageable);

        return cartsPage.map(cart -> {
            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
            return mapToCartResponseDTO(cart, cartItems);
        });
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

    }


