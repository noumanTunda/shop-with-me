package com.tundalabs.store.services;

import com.tundalabs.store.auth.AuthService;
import com.tundalabs.store.dtos.OrderDto;
import com.tundalabs.store.exceptions.OrderNotFoundException;
import com.tundalabs.store.mappers.OrderMapper;
import com.tundalabs.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(){
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException();
        }

        var user = authService.getCurrentUser();
        if(!order.isPlacedBy(user)){
            throw new AccessDeniedException("You Dont have access to this Order");
        }

        return orderMapper.toDto(order);

    }
}
