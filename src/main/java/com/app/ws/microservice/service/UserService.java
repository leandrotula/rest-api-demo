package com.app.ws.microservice.service;

import com.app.ws.microservice.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(final UserDto userDto);
    UserDto getUser(final String email);
    UserDto findUserById(final String id);
    UserDto updateUser(final String userId, final UserDto userDto);
    void deleteUser(final String id);
    List<UserDto> retrieveAllUsers(final int page, final int limit);
}
