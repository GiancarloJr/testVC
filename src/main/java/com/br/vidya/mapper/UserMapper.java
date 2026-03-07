package com.br.vidya.mapper;

import com.br.vidya.model.User;
import com.br.vidya.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
