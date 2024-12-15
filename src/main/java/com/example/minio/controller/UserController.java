package com.example.minio.controller;

import com.example.minio.dto.UserCreateDto;
import com.example.minio.dto.UserGetDto;
import com.example.minio.dto.UserUpdateDto;
import com.example.minio.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    //POST Mapping-Create User
    @PostMapping("/register")
    public ResponseEntity<UserGetDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto){
        logger.info("Received request to create user with email : {}", userCreateDto.getEmail());
        UserGetDto createdUser = this.userService.createUser(userCreateDto);
        logger.info("Returning response for user creation with email : {}", createdUser.getEmail());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //GET Mapping-Get User By ID
    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable("id") UUID userId){
        logger.info("Received request to get user with ID : {}",userId);
        UserGetDto userGetDto = userService.getUserById(userId);
        logger.info("Returning response for get user with ID : {}",userGetDto.getId());
        return new ResponseEntity<>(userGetDto,HttpStatus.OK);
    }

    //PUT Mapping-Update User
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Jwt Token Authentication")
    public ResponseEntity<UserGetDto> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable("id") UUID userId){
        logger.info("Received request to update user with ID : {}",userId);
        UserGetDto updatedUser = userService.updateUser(userUpdateDto,userId);
        logger.info("Returning response for updated user with ID : {}",userId);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    //DELETE Mapping-Delete User
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Jwt Token Authentication")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID userId){
        logger.info("Received request to delete user with ID : {}",userId);
        userService.deleteUserById(userId);
        logger.info("Returning response for delete user with ID : {}",userId);
        return new ResponseEntity<>("User Deleted Successfully",HttpStatus.OK);
    }

}
