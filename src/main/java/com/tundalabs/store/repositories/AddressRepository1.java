package com.tundalabs.store.repositories;

import com.tundalabs.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository1 extends JpaRepository<Order, Long> {
}