//package com.projectsummit.Carts_service.Mappers;
//
//import com.projectsummit.Carts_service.DTOs.CartItemResponseDTO;
//import com.projectsummit.Carts_service.DTOs.CartResponseDTO;
//import com.projectsummit.Carts_service.Entity.Cart;
//import com.projectsummit.Carts_service.Entity.CartItem;
//import com.projectsummit.Carts_service.Repository.CartsRepository;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Service
//public class CartResponseDTOMapper implements Function<Cart, CartResponseDTO> {
//
//    private final CartsRepository cartsRepository;
//    private final CartItemResponseDTOMapper cartItemResponseDTOMapper;
//    public CartResponseDTOMapper(CartsRepository cartsRepository,CartItemResponseDTOMapper cartItemResponseDTOMapper) {
//        this.cartsRepository = cartsRepository;
//        this.cartItemResponseDTOMapper = cartItemResponseDTOMapper;
//    }
//
//    @Override
//    public CartResponseDTO apply(Cart cart) {
//        List<CartItemResponseDTO> cartItems = cartsRepository.findByCartId(cart.getCartId())
//                .stream()
//                .map(cartItem -> cartItemResponseDTOMapper.apply((CartItem) cartItem))
//                .toList();
//
//        return new CartResponseDTO(
//                cart.getCartId(),
//                cart.getCustomerId(),
//                cartItems
//        );
//    }
//
//}
////@Component
////public class CartResponseDTOMapper {
////
////    public CartResponseDTO mapToCartResponseDTO(Cart cart, List<CartItem> cartItems) {
////        List<CartItemResponseDTO> cartItemDTOs = cartItems.stream()
////                .map(item -> new CartItemResponseDTO(item.getCartItemId(), item.getProdName(), item.getPrice(), item.getQuantity(), item.getStatus()))
////                .collect(Collectors.toList());
////
////        return new CartResponseDTO(
////                cart.getCartId(),
////                cart.getCustomerId(),
////                cart.getStatus(),
////                cartItemDTOs
////        );
////    }
////}
