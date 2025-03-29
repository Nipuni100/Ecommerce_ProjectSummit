package com.projectsummit.Carts_service.Controller;


import com.projectsummit.Carts_service.DTOs.CartItemQuantityDTO;
import com.projectsummit.Carts_service.DTOs.CartResponseDTO;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.Service.CartsService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="/api/v1/carts")
public class CartsController {
    private final CartsService cartsService;

    @Autowired
    public CartsController(CartsService cartsService) {
        this.cartsService = cartsService;
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> getAllCarts() {
        List<CartResponseDTO> carts = cartsService.getAllCarts();
        return ResponseEntity.ok(carts);
    }


    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable int cartId) {
        return ResponseEntity.ok(cartsService.getCartById(cartId));
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CartResponseDTO> getCartByCustomerId(@PathVariable int customerId) {
        CartResponseDTO cartResponse = cartsService.getCartByCustomerId(customerId);
        if (cartResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cartResponse);
    }


    @PostMapping("/customers/{customerId}/items")
    public ResponseEntity<String> addItemToCart(@RequestBody CartItem cartItem, @PathVariable int customerId) {
        cartsService.addItemToCart(cartItem, customerId);
        return ResponseEntity.ok("Item added to cart successfully");
    }

    @DeleteMapping("/customers/{customerId}/items/{itemId}")
    public ResponseEntity<String> removeItemFromCart(
            @PathVariable int customerId,
            @PathVariable int itemId) {
        cartsService.removeItemFromCart(customerId, itemId);
        return ResponseEntity.ok("Item removed from cart successfully");
    }

    @PatchMapping("/customers/{customerId}/items/{itemId}")
    public ResponseEntity<CartItem> updateItemQuantity(
            @PathVariable int customerId,
            @PathVariable int itemId,
            @RequestBody CartItemQuantityDTO quantityDTO) {

        int newQuantity = quantityDTO.quantity();
        CartItem updatedCartItem = cartsService.updateItemQuantity(customerId, itemId, newQuantity);

        return updatedCartItem != null
                ? ResponseEntity.ok(updatedCartItem)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/customers/{customerId}/empty")
    public ResponseEntity<String> emptyCart(@PathVariable int customerId) {
        cartsService.emptyCart(customerId);
        return ResponseEntity.ok("Cart emptied successfully.");
    }







}
