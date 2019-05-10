package com.app.ws.microservice.service;

import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        final UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        final UserDto savedUserData = modelMapper.map(userEntity, UserDto.class);
        return savedUserData;
    }

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
