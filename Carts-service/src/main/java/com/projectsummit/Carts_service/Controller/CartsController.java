package com.projectsummit.Carts_service.Controller;


import com.projectsummit.Carts_service.DTOs.CartResponseDTO;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.Service.CartsService;
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

//    @GetMapping
//    public ResponseEntity<List<CartResponseDTO>> getAllCarts() {
//        return ResponseEntity.ok(cartsService.getAllCarts());
//    }

//    @GetMapping("/{cartId}")
//    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable int cartId) {
//        return ResponseEntity.ok(cartsService.getCartById(cartId));
//    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponseDTO> getCartByCustomerId(@PathVariable int customerId) {
        return ResponseEntity.ok(cartsService.getCartByCustomerId(customerId));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<String> addItemToCart(@RequestBody CartItem cartItem, @RequestParam int customerId) {
        cartsService.addItemToCart(cartItem, customerId);
        return ResponseEntity.ok("Item added to cart successfully");
    }




//    @PostMapping("{/cartId}/Items")
//    public ResponseEntity<CartItem> addItemToCart(@PathVariable Long cartId, @RequestBody CartItem cartItem) {
//        CartItem savedcartItem = cartsService.addcartItem(cartItem);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedcartItem);
//    }


}
