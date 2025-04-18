package com.projectsummit.Carts_service.Controller;

import com.projectsummit.Carts_service.DTOs.*;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.ExceptionHandling.CartNotFoundException;
import com.projectsummit.Carts_service.ExceptionHandling.ResourceNotFoundException;
import com.projectsummit.Carts_service.Service.ImplCartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(path="/api/v1/carts")
public class CartsController {
    private final ImplCartsService cartsService;
    private static final Logger logger = LoggerFactory.getLogger(ImplCartsService.class);
    @Autowired
    public CartsController(ImplCartsService cartsService) {
        this.cartsService = cartsService;
    }

    //    Get all carts
    @GetMapping
    public ResponseEntity<Page<CartResponseDTO>> getAllCarts(Pageable pageable) {
        logger.info("Received request to fetch all carts with pagination: {}", pageable);
        try {
            Page<CartResponseDTO> carts = cartsService.getAllCarts(pageable);
            if (carts.isEmpty()) {
                logger.warn("No carts found in the database.");
            } else {
                logger.info("Fetched {} carts successfully.", carts.getTotalElements());
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            logger.error("Unexpected server error while fetching carts: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
        }
    }


    // Get the cart by cart ID
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable int cartId) {
        logger.info("Received request to fetch cart with ID: {}", cartId);
        try {
            CartResponseDTO cartResponseDTO = cartsService.getCartById(cartId);

            if (cartResponseDTO == null) {
                logger.warn("Cart with ID {} not found.", cartId);
                throw new CartNotFoundException("Cart with ID " + cartId + " not found.");
            }

            logger.info("Successfully retrieved cart with ID: {}", cartId);
            return ResponseEntity.ok(cartResponseDTO);
        }
        catch (CartNotFoundException e) {
            logger.error("Cart not found error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);}}

    //    Get the customer's cart
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CartResponseDTO> getCartByCustomerId(@PathVariable int customerId) {
        CartResponseDTO cartResponse = cartsService.getCartByCustomerId(customerId);
        if (cartResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cartResponse);
    }

    //    Add an item to cart
    @PostMapping("/customers/{customerId}/items")
    public ResponseEntity<String> addItemToCart(@RequestBody CartItemRequestDTO cartItemRequestDTO, @PathVariable int customerId) {
        logger.info("Received request to add item to cart for customer ID: {}. Item details: {}", customerId, cartItemRequestDTO);

        cartsService.addItemToCart(cartItemRequestDTO, customerId);
        logger.info("Item successfully added to cart for customer ID: {}", customerId);
        return ResponseEntity.ok("Item added to cart successfully");

    }

    //    Remove an item from the cart
    @DeleteMapping("/customers/{customerId}/items/{prodId}")
    public ResponseEntity<String> removeItemFromCart(
            @PathVariable int customerId,
            @PathVariable int prodId) {

        logger.info("Received request to remove item with Product ID: {} for Customer ID: {}", prodId, customerId);
        try{
            cartsService.removeItemFromCart(customerId, prodId);


                logger.info("Item with Product ID: {} successfully removed from cart for Customer ID: {}", prodId, customerId);
                return ResponseEntity.ok("Item removed from cart successfully");
        }catch (ResourceNotFoundException e) {
            // Log and handle item or cart not foundlogger.error("Item with Product ID: {} not found in cart for Customer ID: {}", prodId, customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart");


        }catch (Exception e) {
            logger.error("Unexpected error occurred while removing item with Product ID: {} from cart for Customer ID: {}. Error: {}", prodId, customerId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing item from cart");
        }}

    //    Increase or decrease the quantity of an item
    @PatchMapping("/customers/{customerId}/items/{prodId}")
    public ResponseEntity<CartItem> updateItemQuantity(
            @PathVariable int customerId,
            @PathVariable int prodId,
            @RequestBody CartItemQuantityDTO quantityDTO) {

        logger.info("Received request to update quantity of Product ID: {} for Customer ID: {} with new quantity: {}", prodId, customerId, quantityDTO.quantity());
try {
    int newQuantity = quantityDTO.quantity();
    CartItem updatedCartItem = cartsService.updateItemQuantity(customerId, prodId, newQuantity);
    if (updatedCartItem != null) {
        logger.info("Product ID: {} quantity updated successfully for Customer ID: {}. New quantity: {}", prodId, customerId, quantityDTO.quantity());
        return ResponseEntity.ok(updatedCartItem);
    } else {
        logger.error("Product ID: {} not found in cart for Customer ID: {}", prodId, customerId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
} catch (Exception e) {
    logger.error("Unexpected error occurred while updating quantity of Product ID: {} for Customer ID: {}. Error: {}", prodId, customerId, e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
}
    }

//    Empty the cart
    @DeleteMapping("/customers/{customerId}/empty")
    public ResponseEntity<String> emptyCart(@PathVariable int customerId) {
        logger.info("Received request to empty the cart for Customer ID: {}", customerId);
        try{
            boolean isCartExist = cartsService.isCartExist(customerId);  // Assume this method checks if the cart exists

            if (!isCartExist) {
                logger.error("Cart not found for Customer ID: {}", customerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for Customer ID: " + customerId);
            }
            cartsService.emptyCart(customerId);
            logger.info("Cart for Customer ID: {} successfully emptied.", customerId);
            return ResponseEntity.ok("Cart emptied successfully.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while emptying the cart for Customer ID: {}. Error: {}", customerId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error emptying cart");
        }
    }


//    @PutMapping("/{cartId}/status")
//    public ResponseEntity<String> updateCartAndItemsStatus(@PathVariable int cartId) {
//        try {
//            cartsService.deactivateCartAndItems(cartId);
//            return ResponseEntity.ok("Cart and cart items status updated successfully.");
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
//        }
//    }
@PutMapping("/{cartId}/status")
public ResponseEntity<String> updateCartAndItemsStatus(
        @PathVariable int cartId,
        @RequestBody UpdateCartStatusDTO updateCartStatusDTO) {
    try {
        cartsService.updateCartAndItemsStatus(
                cartId,
                updateCartStatusDTO.Status(),
                updateCartStatusDTO.ProductIdsInOrder()
        );
        return ResponseEntity.ok("Cart and cart items status updated successfully.");
    } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
    }
}


}

