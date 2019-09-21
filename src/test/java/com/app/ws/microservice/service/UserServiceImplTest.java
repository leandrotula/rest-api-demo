package com.app.ws.microservice.service;

import com.app.ws.microservice.exceptions.UserException;
import com.app.ws.microservice.io.entity.AddressEntity;
import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.AddressDto;
import com.app.ws.microservice.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setup() {

        userService = new UserServiceImpl();
        userService.setUserRepository(userRepository);
        userService.setModelMapper(new ModelMapper());
        userService.setbCryptPasswordEncoder(bCryptPasswordEncoder);
    }

    @Test
    @DisplayName("If no email exists, during user creation then we should get an exception")
    void shouldThrowExceptionDueNonExistentEmail() {

        UserEntity userInformation = new UserEntity();
        userInformation.setEmail("testemail@mail.com");
        when(userRepository.findByEmail(anyString())).thenReturn(userInformation);
        UserDto userDto = new UserDto();
        userDto.setEmail("testemail@mail.com");
        Assertions.assertThrows(UserException.class, () -> userService.createUser(userDto));
        ArgumentCaptor<String> emailArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(emailArgCaptor.capture());
        assertThat(emailArgCaptor.getValue()).isEqualTo("testemail@mail.com");
    }

    @Test
    @DisplayName("a user should be successfully save if it does not exists previously")
    void shouldSuccessfullySaveUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        UserDto userDto = retrieveUserDto();
        userService.createUser(userDto);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("While trying get an user, if the emails does not exist, then we should get an exception")
    void shouldGetExceptionDueNonExistentEmail() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.getUser("test@email.com"));
    }

    @Test
    @DisplayName("if user exists, with email, then we should be able to retrieve it successfully")
    void shouldRetrieveFoundUserByEmail() {

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@emailtest.com");
        userEntity.setFirstName("testname");
        userEntity.setLastName("testlastname");
        AddressEntity addressOne = new AddressEntity();
        addressOne.setCity("Cba");
        addressOne.setCountry("Argentina");
        userEntity.setAddresses(Stream.of(addressOne).collect(Collectors.toList()));
        when(userRepository.findByEmail("test@emailtest.com")).thenReturn(userEntity);
        final UserDto user = userService.getUser("test@emailtest.com");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@emailtest.com");
        final ArgumentCaptor<String> emailArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(emailArgCaptor.capture());
        assertThat("test@emailtest.com").isEqualTo(emailArgCaptor.getValue());
    }

    @Test
    void shouldDeleteFoundUser() {

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@emailtest.com");
        userEntity.setFirstName("testname");
        userEntity.setLastName("testlastname");
        AddressEntity addressOne = new AddressEntity();
        addressOne.setCity("Cba");
        addressOne.setCountry("Argentina");
        userEntity.setAddresses(Stream.of(addressOne).collect(Collectors.toList()));
        when(userRepository.findByUserId("iduser")).thenReturn(userEntity);
        userService.deleteUser("iduser");
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    @DisplayName("If the user does not exists, then we should get an exception while trying to perform deletion")
    void shouldThrowErrorDueDeletionOfNonExistentUser() {

        when(userRepository.findByUserId(anyString())).thenReturn(null);
        Assertions.assertThrows(UserException.class, () -> userService.deleteUser("userid"));
        verify(userRepository, times(1)).findByUserId(anyString());
        verify(userRepository, times(1)).findByUserId(anyString());
    }

    @Test
    @DisplayName("We should retrieve paginated users")
    void shouldRetrieveAllPaginatedUsers() {

        Pageable pageReq = PageRequest.of(1, 1);
        UserEntity userOne = new UserEntity();
        userOne.setFirstName("testusername");
        userOne.setLastName("testlastname");
        userOne.setEmail("test@email.com");
        userOne.setEmailVerificationStatus(true);
        List<UserEntity> usersEntities = Stream.of(userOne).collect(Collectors.toList());
        Page<UserEntity> result = new PageImpl<>(usersEntities);
        when(userRepository.findAll(pageReq)).thenReturn(result);
        final List<UserDto> foundUsers = userService.retrieveAllUsers(1, 1);
        assertThat(!foundUsers.isEmpty()).isTrue();
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    private UserDto retrieveUserDto() {

        UserDto userDto = new UserDto();
        userDto.setFirstName("testname");
        userDto.setLastName("testlastname");
        userDto.setEmail("testemail@email.com");
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCity("Cordoba Capital");
        firstAddress.setCountry("Argentina");
        firstAddress.setStreetName("Cpaz street");
        firstAddress.setPostalCode("5014");
        AddressDto secondAddress = new AddressDto();
        secondAddress.setCity("Chubut");
        secondAddress.setCountry("Argentina");
        secondAddress.setStreetName("sluis street");
        secondAddress.setPostalCode("2589");
        List<AddressDto> addresses = Stream.of(firstAddress, secondAddress).collect(Collectors.toList());
        userDto.setAddresses(addresses);
        return userDto;
    }

}
