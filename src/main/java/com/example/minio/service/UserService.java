package com.example.minio.service;

import com.example.minio.dto.UserCreateDto;
import com.example.minio.dto.UserGetDto;
import com.example.minio.dto.UserUpdateDto;
import com.example.minio.model.User;

import java.util.UUID;

public interface UserService {

    void deleteUserById(UUID userId);
    User fetchUserById(UUID userId);
    UserGetDto getUserById(UUID userId);
    UserGetDto createUser(UserCreateDto userCreateDto);
    UserGetDto updateUser(UserUpdateDto userUpdateDto, UUID userId);

}
