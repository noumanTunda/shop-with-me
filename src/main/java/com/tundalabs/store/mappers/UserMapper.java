package com.tundalabs.store.mappers;

import com.tundalabs.store.auth.RegisterUserRequest;
import com.tundalabs.store.dtos.UpdateUSerRequest;
import com.tundalabs.store.dtos.UserDto;
import com.tundalabs.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);

    void update(UpdateUSerRequest request, @MappingTarget User user);
}
