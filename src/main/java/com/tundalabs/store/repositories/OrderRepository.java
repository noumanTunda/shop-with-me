package com.tundalabs.store.repositories;

import com.tundalabs.store.entities.Order;
import com.tundalabs.store.entities.User;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(User customer);
}