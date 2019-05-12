package com.app.ws.microservice.service;

import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(final UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException(String.format("User with email %s already exists", userDto.getEmail()));
        }
        final UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEmailVerificationToken(UUID.randomUUID().toString());
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(UUID.randomUUID().toString());
        final UserDto savedUserData = modelMapper.map(userEntity, UserDto.class);
        userRepository.save(userEntity);
        return savedUserData;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto findUserById(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity != null) {
         return modelMapper.map(userEntity, UserDto.class);
        } else {
            throw new UsernameNotFoundException(String.format("User Not found for id %s", id));
        }
    }

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Autowired
    public void setbCryptPasswordEncoder(final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}
