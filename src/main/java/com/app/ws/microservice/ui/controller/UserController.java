package com.app.ws.microservice.ui.controller;

import com.app.ws.microservice.service.UserService;
import com.app.ws.microservice.shared.dto.UserDto;
import com.app.ws.microservice.ui.model.request.UserDetailsRequestModel;
import com.app.ws.microservice.ui.model.response.UserRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private ModelMapper modelMapper;
    private UserService userservice;


    @PostMapping
    public ResponseEntity<UserRest> createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        final UserDto userDto = modelMapper.map(userDetailsRequestModel, UserDto.class);
        final UserDto createUser = userservice.createUser(userDto);
        UserRest userRest = modelMapper.map(createUser, UserRest.class);

        return ResponseEntity.ok(userRest);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<UserRest> retrieveUserMessage(@PathVariable String id) {
        UserDto userDto = userservice.findUserById(id);

        return ResponseEntity.ok(modelMapper.map(userDto, UserRest.class));
    }

    @Autowired
    public void setModelMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setUserservice(final UserService userservice) {
        this.userservice = userservice;
    }
}
