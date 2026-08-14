package com.tundalabs.store.mappers;

import com.tundalabs.store.dtos.CartDto;
import com.tundalabs.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
