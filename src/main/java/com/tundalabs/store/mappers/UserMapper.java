package com.tundalabs.store.mappers;

import com.tundalabs.store.dtos.UserDto;
import com.tundalabs.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
}
