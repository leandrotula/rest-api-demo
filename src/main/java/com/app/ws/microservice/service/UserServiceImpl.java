package com.app.ws.microservice.service;

import com.app.ws.microservice.exceptions.UserException;
import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.AddressDto;
import com.app.ws.microservice.shared.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(final UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserException(String.format("User with email %s already exists", userDto.getEmail()));
        }
        for(int i=0; i<userDto.getAddresses().size(); i++) {
          AddressDto addressDto = userDto.getAddresses().get(i);
          addressDto.setAddressId(RandomStringUtils.randomAlphabetic(8));
          addressDto.setUserDto(userDto);
          userDto.getAddresses().set(i, addressDto);
        }
        final UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEmailVerificationToken(UUID.randomUUID().toString());
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(RandomStringUtils.randomAlphabetic(8));
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }


    @Override
    public UserDto updateUser(final String id, final UserDto userDto) {

        if (userDto == null) {
            throw new UserException(String.format("UserDto cannot be process for id %s", id));
        }
        UserEntity byUserId = userRepository.findByUserId(id);
        byUserId.setFirstName(userDto.getFirstName());
        byUserId.setFirstName(userDto.getLastName());
        userRepository.save(byUserId);
        return modelMapper.map(byUserId, UserDto.class);
    }

    @Override
    public void deleteUser(final String id) {
        log.debug("Deleting user with id {} ", id);
        UserEntity user = userRepository.findByUserId(id);
        if (user == null) {
            throw new UserException(String.format("There wasn't a user with id %s to delete ", id));
        }
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> retrieveAllUsers(int page, int limit) {

        final Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> allUsers = userRepository.findAll(pageableRequest);
        List<UserEntity> content = allUsers.getContent();

        return content
                .stream()
                .map(user -> this.modelMapper.map(content, UserDto.class))
                .collect(Collectors.toList());
    }

    @Autowired
    public void setbCryptPasswordEncoder(final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
