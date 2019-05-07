package com.app.ws.microservice.service;

import com.app.ws.microservice.shared.dto.UserDto;

public interface UserService {

    UserDto createUser(final UserDto userDto);
}
