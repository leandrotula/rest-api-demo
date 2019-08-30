package com.app.ws.microservice.service;

import com.app.ws.microservice.exceptions.UserException;
import com.app.ws.microservice.io.entity.AddressEntity;
import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.AddressDto;
import com.app.ws.microservice.shared.dto.UserDto;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserServiceImpl userService;

    @Before
    public void setup() {

        userService = new UserServiceImpl();
        userService.setUserRepository(userRepository);
        userService.setModelMapper(new ModelMapper());
        userService.setbCryptPasswordEncoder(bCryptPasswordEncoder);
    }

    @Test
    public void shouldThrowExceptionDueNonExistentEmail() {

        UserEntity userInformation = new UserEntity();
        userInformation.setEmail("testemail@mail.com");
        when(userRepository.findByEmail(anyString())).thenReturn(userInformation);
        expectedException.expect(UserException.class);
        UserDto userDto = new UserDto();
        userDto.setEmail("testemail@mail.com");
        userService.createUser(userDto);
        ArgumentCaptor<String> emailArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(emailArgCaptor.capture());
        assertThat(emailArgCaptor.getValue()).isEqualTo("testemail@mail.com");
    }

    @Test
    public void shouldSuccessfullySaveUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        UserDto userDto = retrieveUserDto();
        userService.createUser(userDto);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void shouldGetExceptionDueNonExistentEmail() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        expectedException.expect(UsernameNotFoundException.class);
        userService.getUser("test@email.com");

    }

    @Test
    public void shouldRetrieveFoundUserByEmail() {

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
    public void shouldDeleteFoundUser() {

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
    public void shouldThrowErrorDueDeletionOfNonExistentUser() {

        when(userRepository.findByUserId(anyString())).thenReturn(null);
        expectedException.expect(UserException.class);
        userService.deleteUser("userid");
        verify(userRepository, times(1)).findByUserId(anyString());
        verify(userRepository, never()).findByUserId(anyString());
    }

    @Test
    public void shouldRetrieveAllPaginatedUsers() {

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
