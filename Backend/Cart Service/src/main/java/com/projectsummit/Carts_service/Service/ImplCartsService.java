package com.projectsummit.Carts_service.Service;

import com.projectsummit.Carts_service.DTOs.*;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.ExceptionHandling.ResourceNotFoundException;
//import com.projectsummit.Carts_service.Mappers.CartItemResponseDTOMapper;
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
//    private CartItemResponseDTOMapper cartItemResponseDTOMapper;
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
        logger.info("Fetching active cart for customer ID: {}", customerId);

        try{
            Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                    .orElseThrow(() -> {
                        logger.warn("No active cart found for customer ID: {}", customerId);
                        return new ResourceNotFoundException("Active cart not found for customer ID: " + customerId);
                    });
            logger.info("Active cart found for customer ID: {}. Cart ID: {}", customerId, cart.getCartId());

            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
            logger.info("Retrieved {} items for cart ID: {}", cartItems.size(), cart.getCartId());
            return mapToCartResponseDTO(cart, cartItems);
    }
        catch (ResourceNotFoundException e) {
            logger.error("Error while fetching cart for customer ID {}: {}", customerId, e.getMessage());
            throw e;
        }}


    public void addItemToCart(CartItemRequestDTO cartItemRequestDTO, int customerId) {
        logger.info("Adding item to cart for customer ID: {}. Item details: {}", customerId, cartItemRequestDTO);

        try{
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseGet(() -> {
                            logger.info("No active cart found for customer ID: {}. Creating a new cart.", customerId);
                    Cart newCart = new Cart(customerId);
                    newCart.setStatus("ACTIVE");
                    Cart savedCart = cartsRepository.save(newCart);
                    logger.info("New active cart created with ID: {}", savedCart.getCartId());
                    return savedCart;
                });
            if (cartItemRequestDTO.prodId() == 0) {
                logger.error("Product ID is null in the request for customer ID: {}", customerId);
                throw new IllegalArgumentException("Product ID cannot be null");
            }
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

            logger.info("Item successfully added to cart with ID: {} for customer ID: {}", cart.getCartId(), customerId);
    }
        catch (Exception e) {
            logger.error("Unexpected error while adding item to cart for customer ID {}: {}", customerId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while adding item to cart.", e);
        }

    }

    public void removeItemFromCart(int customerId, int prodId) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProdId(cart.getCartId(), prodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item with product ID " + prodId + " not found in customer's active cart."));

        cartItemRepository.delete(cartItem);
    }

    public CartItem updateItemQuantity(int customerId, int prodId, int newQuantity) {
        Cart cart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for customer ID: " + customerId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProdId(cart.getCartId(), prodId)
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

    public boolean isCartExist(int customerId) {
        return cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE").isPresent();
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
                .map(item -> new CartItemDTO(
                        item.getCartItemId(),
                        item.getProdName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getProdId()))
                .collect(Collectors.toList());

        return new CartResponseDTO(
                cart.getCartId(),
                cart.getCustomerId(),
                cartItemDTOs
        );
     }

//    public void deactivateCartAndItems(int cartId) {
//        updateCartStatus(cartId, "DEACTIVE");
//        updateCartItemsStatusByCartId(cartId, "IN_ORDER");
//
//    }
//
//    public void updateCartStatus(int cartId, String status) {
//        Optional<Cart> cartOptional = cartsRepository.findById((long)cartId);
//
//        if (!cartOptional.isPresent()) {
//            throw new ResourceNotFoundException("Cart with ID " + cartId + " not found.");
//        }
//
//        Cart cart = cartOptional.get();
//        cart.setStatus(status);
//        cartsRepository.save(cart);}
//
//    public void updateCartItemsStatusByCartId(int cartId, String status) {
//        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
//
//        if (cartItems.isEmpty()) {
//            throw new ResourceNotFoundException("No items found in the cart with ID " + cartId);
//        }
//
//        for (CartItem cartItem : cartItems) {
//            cartItem.setStatus(status);
//        }
//
//        cartItemRepository.saveAll(cartItems);
//    }
public void updateCartAndItemsStatus(int cartId, String status, List<Integer> productIds) {
    Optional<Cart> cartOptional = cartsRepository.findById((long) cartId);

    if (!cartOptional.isPresent()) {
        throw new ResourceNotFoundException("Cart with ID " + cartId + " not found.");
    }

    Cart cart = cartOptional.get();

    // Update the statuses of the specified cart items
    List<CartItem> cartItemsToUpdate = cartItemRepository.findByCartIdAndProdIdIn(cartId, productIds);
    if (cartItemsToUpdate.isEmpty()) {
        throw new ResourceNotFoundException("No items found in the cart for the specified products.");
    }
    for (CartItem cartItem : cartItemsToUpdate) {
        cartItem.setStatus("IN_ORDER");
    }
    cartItemRepository.saveAll(cartItemsToUpdate);

    // Create a new cart for remaining items
    List<CartItem> remainingCartItems = cartItemRepository.findByCartIdAndProdIdNotIn(cartId, productIds);
    if (!remainingCartItems.isEmpty()) {
        Cart newCart = new Cart();
        newCart.setCustomerId(cart.getCustomerId());
        newCart.setStatus("ACTIVE");
        cartsRepository.save(newCart);

        for (CartItem remainingItem : remainingCartItems) {
            remainingItem.setCartId(newCart.getCartId());
        }
        cartItemRepository.saveAll(remainingCartItems);
    }

    // Deactivate the original cart
    cart.setStatus(status);
    cartsRepository.save(cart);
}


//    public void updateCartStatus(int cartId, String status) {
//        Optional<Cart> cartOptional = cartsRepository.findById((long) cartId);
//
//        if (!cartOptional.isPresent()) {
//            throw new ResourceNotFoundException("Cart with ID " + cartId + " not found.");
//        }
//
//        Cart cart = cartOptional.get();
//        cart.setStatus(status);
//        cartsRepository.save(cart);
//    }
//
//    private Cart createNewCart() {
//        Cart newCart = new Cart();
//        newCart.setStatus("ACTIVE");
//        return cartsRepository.save(newCart);
//    }

}


