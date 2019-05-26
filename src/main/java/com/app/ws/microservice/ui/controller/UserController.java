package com.app.ws.microservice.ui.controller;

import com.app.ws.microservice.service.AddressService;
import com.app.ws.microservice.service.UserService;
import com.app.ws.microservice.shared.dto.AddressDto;
import com.app.ws.microservice.shared.dto.UserDto;
import com.app.ws.microservice.ui.model.request.UserDetailsRequestModel;
import com.app.ws.microservice.ui.model.response.AddressRest;
import com.app.ws.microservice.ui.model.response.UserRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {

    private ModelMapper modelMapper;
    private UserService userservice;
    private AddressService addressService;


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

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {

        this.userservice.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserRest>> retrieveAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "limit", defaultValue = "35") int limit) {

        List<UserDto> userDtos = this.userservice.retrieveAllUsers(page, limit);

        List<UserRest> userRests = userDtos.stream()
                .map(inUser -> this.modelMapper.map(inUser, UserRest.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userRests);
    }

    @GetMapping(path = "{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AddressRest>> retrieveAddress(@PathVariable String id) {

        List<AddressRest> addressRests = new LinkedList<>();
        List<AddressDto> addressDtos = addressService.findAddresses(id);

        if(!CollectionUtils.isEmpty(addressDtos)) {

            Type listType = new TypeToken<List<AddressRest>>() {}.getType();
            addressRests = modelMapper.map(addressDtos, listType);
        }

        return ResponseEntity.ok(addressRests);

    }

    @Autowired
    public void setModelMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setUserservice(final UserService userservice) {
        this.userservice = userservice;
    }

    @Autowired
    public void setAddressService(final AddressService addressService) {
        this.addressService = addressService;
    }
}
