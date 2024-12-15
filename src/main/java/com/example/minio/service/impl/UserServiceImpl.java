package com.example.minio.service.impl;

import com.example.minio.config.mapper.UserMapper;
import com.example.minio.dto.UserCreateDto;
import com.example.minio.dto.UserGetDto;
import com.example.minio.dto.UserUpdateDto;
import com.example.minio.exception.ResourceNotFoundException;
import com.example.minio.model.User;
import com.example.minio.repository.UserRepository;
import com.example.minio.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper){
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public UserGetDto createUser(UserCreateDto userCreateDto) {

        try {
            User user = userMapper.toEntity(userCreateDto);

            User savedUser = userRepository.save(user);
            if(savedUser.getId() != null){
                System.out.println(savedUser);
                logger.info("User created successfully with email : {}",savedUser.getEmail());
            }
            return userMapper.toUserGetDto(savedUser);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while creating user: {}", exception.getMessage(), exception);
            throw exception;
        }
    }

    @Override
    public UserGetDto updateUser(UserUpdateDto userUpdateDto, UUID userId) {
        logger.info("Updating user with ID : {}",userId);

        try{
            User updatedUser = userRepository.findById(userId).map(user -> {
                user.setName(userUpdateDto.getName());
                user.setEmail(userUpdateDto.getEmail());
                user.setAbout(userUpdateDto.getAbout());
                user.setUserName(userUpdateDto.getUserName());
                user.setPhoneNumber(userUpdateDto.getPhoneNumber());
                User savedUser = userRepository.save(user);

                logger.info("User with ID {} updated successfully",userId);
                return savedUser;
            }).orElseThrow(() -> {
                logger.warn("User with ID {} not found, Updated user operation not performed", userId);
                return new ResourceNotFoundException("User","ID",String.valueOf(userId),"Update User operation not performed");
            });
            return userMapper.toUserGetDto(updatedUser);
        } catch (Exception exception){
            logger.error("Error occurred while updating user, Error: {}", exception.getMessage(), exception);
            throw exception;
        }
    }


    @Override
    @Transactional
    public void deleteUserById(UUID userId){
        logger.info("Deleting user with ID : {}",userId);

       try{
           User user = this.fetchUserById(userId);
           userRepository.delete(user);
           logger.info("User with ID {} deleted successfully", user.getId());
       } catch (Exception exception) {
           logger.error("Unexpected error occurred while deleting user. User ID: {}, Error: {}", userId, exception.getMessage(), exception);
           throw exception;
       }
    }


    @Override
    public User fetchUserById(UUID userId) {
        logger.info("Fetching user with ID : {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User with ID {} not found, Get user operation not performed", userId);
            return new ResourceNotFoundException("User","ID",String.valueOf(userId),"Get User operation not performed");
        });
        logger.info("User found with ID : {}",user.getId());
        return user;
    }

    @Override
    public UserGetDto getUserById(UUID userId) {
        User user = this.fetchUserById(userId);
        return userMapper.toUserGetDto(user);
    }


}
