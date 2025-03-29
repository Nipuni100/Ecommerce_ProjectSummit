package com.projectsummit.Carts_service.Repository;


import com.projectsummit.Carts_service.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CartsRepository
        extends JpaRepository<Cart, Long> {

    Collection<Object> findByCartId(int cartId);
//    Cart findByCustomerIdAndStatus(int customerId, String status);
    Optional<Cart> findByCustomerIdAndStatus(int customerId, String status);
}


