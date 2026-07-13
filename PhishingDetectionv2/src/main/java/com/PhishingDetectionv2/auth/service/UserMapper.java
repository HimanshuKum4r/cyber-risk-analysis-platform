package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.dto.response.UserResponse;
import com.PhishingDetectionv2.auth.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

}