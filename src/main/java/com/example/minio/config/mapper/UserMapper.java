package com.example.minio.config.mapper;

import com.example.minio.dto.UserCreateDto;
import com.example.minio.dto.UserGetDto;
import com.example.minio.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public User toEntity(UserCreateDto userCreateDto){
        return modelMapper.map(userCreateDto,User.class);
    }

    public User toEntity(UserGetDto userGetDto){
        return modelMapper.map(userGetDto,User.class);
    }

    public UserGetDto toUserGetDto(User user){
        return modelMapper.map(user,UserGetDto.class);
    }

}
