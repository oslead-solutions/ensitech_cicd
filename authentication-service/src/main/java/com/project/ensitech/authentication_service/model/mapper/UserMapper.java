package com.project.ensitech.authentication_service.model.mapper;


import com.project.ensitech.authentication_service.model.dto.AuthenticationResponseDto;
import com.project.ensitech.authentication_service.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AuthenticationResponseDto toUserDto(User user);
    User toUserEntity(AuthenticationResponseDto user);
}