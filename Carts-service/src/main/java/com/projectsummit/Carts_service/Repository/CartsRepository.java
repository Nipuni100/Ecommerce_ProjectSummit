package com.projectsummit.Carts_service.Repository;


import com.projectsummit.Carts_service.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsRepository
        extends JpaRepository<Cart, Long> {

}


