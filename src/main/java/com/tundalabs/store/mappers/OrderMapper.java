package com.tundalabs.store.mappers;

import com.tundalabs.store.dtos.OrderDto;
import com.tundalabs.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
