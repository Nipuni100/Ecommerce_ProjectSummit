package com.projectsummit.Carts_service.Service;

import com.projectsummit.Carts_service.DTOs.CartItemResponseDTO;
import com.projectsummit.Carts_service.DTOs.CartResponseDTO;
import com.projectsummit.Carts_service.Entity.CartItem;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.Mappers.CartItemResponseDTOMapper;
import com.projectsummit.Carts_service.Mappers.CartResponseDTOMapper;
import com.projectsummit.Carts_service.Repository.CartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CartsService {
    private CartsRepository cartsRepository;
    private CartResponseDTOMapper cartResponseDTOMapper;
    private CartItemResponseDTOMapper cartItemResponseDTOMapper;

    @Autowired
    public CartsService(CartsRepository cartsRepository) {
        this.cartsRepository = cartsRepository;
    }


//    public List<CartResponseDTO> getAllCarts() {
//        return cartsRepository.findAll()
//                .stream()
//                .map(cartResponseDTOMapper)
//                .toList();
//    }

    public CartResponseDTO getCartById(int cartId) {
        return cartsRepository.findById((long)cartId)
                .map(cartResponseDTOMapper)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
    }

    public CartResponseDTO getCartByCustomerId(int customerId) {
        return cartsRepository.findByCustomerIdAndStatus(customerId, "Active")
                .map(cartResponseDTOMapper)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active cart not found for this customer"));

    }

    public void addItemToCart(CartItem cartItem, int customerId) {
        Optional<Cart> activeCart = cartsRepository.findByCustomerIdAndStatus(customerId, "ACTIVE");
        Cart cart;

        if (activeCart.isPresent()) {
            cart = activeCart.get();
        } else {
            cart = new Cart(customerId);
            cart.setStatus("ACTIVE");
            cart = cartsRepository.save(cart);
        }

        cartItem.setCartId(cart.getCartId());
        cart.getItems().add(cartItem);
        cartsRepository.save(cart);
    }
}
