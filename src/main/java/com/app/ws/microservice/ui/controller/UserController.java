package com.app.ws.microservice.ui.controller;

import com.app.ws.microservice.exceptions.ExceptionMessages;
import com.app.ws.microservice.exceptions.UserException;
import com.app.ws.microservice.service.UserService;
import com.app.ws.microservice.shared.dto.UserDto;
import com.app.ws.microservice.ui.model.request.UserDetailsRequestModel;
import com.app.ws.microservice.ui.model.response.UserRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {

    private ModelMapper modelMapper;
    private UserService userservice;


    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> createUser(@RequestBody @Valid UserDetailsRequestModel userDetailsRequestModel) {

        final UserDto userDto = modelMapper.map(userDetailsRequestModel, UserDto.class);
        final UserDto createUser = userservice.createUser(userDto);
        UserRest userRest = modelMapper.map(createUser, UserRest.class);

        return ResponseEntity.ok(userRest);
    }

    @GetMapping(path = "{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> retrieveUserMessage(@PathVariable String id) {
        UserDto userDto = userservice.findUserById(id);

        return ResponseEntity.ok(modelMapper.map(userDto, UserRest.class));
    }

    @PutMapping(path = "{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> updateUser(@PathVariable String id,
                                               @RequestBody @Valid UserDetailsRequestModel requestModel) {


        final UserDto userDto = modelMapper.map(requestModel, UserDto.class);
        final UserDto createUser = userservice.updateUser(id, userDto);
        UserRest userRest = modelMapper.map(createUser, UserRest.class);

        return ResponseEntity.ok(userRest);

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
