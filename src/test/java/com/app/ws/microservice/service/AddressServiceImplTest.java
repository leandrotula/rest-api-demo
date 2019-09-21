package com.app.ws.microservice.service;

import com.app.ws.microservice.exceptions.UserException;
import com.app.ws.microservice.io.entity.AddressEntity;
import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.AddressRepository;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.AddressDto;
import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class AddressServiceImplTest {

    private AddressServiceImpl addressService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeEach
    void setup() {

        addressService = new AddressServiceImpl();
        addressService.setUserRepository(userRepository);
        addressService.setAddressRepository(addressRepository);
        addressService.setModelMapper(new ModelMapper());
    }

    @Test
    @DisplayName("Should Get empty address because we don't have an existent user yet")
    void shouldGetEmptyAddressDueUnExistentUser() {

        when(userRepository.findByUserId(anyString())).thenReturn(null);
        final List<AddressDto> addresses = addressService.findAddresses("idusertest");
        assertThat(addresses.isEmpty()).isTrue();
        verify(userRepository, times(1)).findByUserId(anyString());
    }

    @Test
    @DisplayName("We should get an exception because we don't have a associate address for the user")
    void shouldThrowErrorDueNonAddressForUser() {

        UserEntity user = new UserEntity();
        when(userRepository.findByUserId(anyString())).thenReturn(user);
        List<AddressEntity> addresses = Collections.emptyList();
        when(addressRepository.findAllByUserDto(any(UserEntity.class))).thenReturn(addresses);
        Assertions.assertThrows(UserException.class, () -> addressService.findAddresses(anyString()));
        verify(userRepository, times(1)).findByUserId(anyString());
        verify(addressRepository, times(1)).findAllByUserDto(any(UserEntity.class));

    }

    @Test
    @DisplayName("Should get Associated address for an existent user")
    void shouldAddressesAssociatedToAnExistentUser() {

        List<AddressEntity> addresses = retrieveAddresses();
        when(addressRepository.findAllByUserDto(any(UserEntity.class))).thenReturn(addresses);
        final List<AddressDto> userAddresses = addressService.findAddresses("idusertest");
        assertThat(userAddresses.isEmpty()).isFalse();
        verify(userRepository, times(1)).findByUserId(anyString());
        verify(addressRepository, times(1)).findAllByUserDto(any(UserEntity.class));

    }

    @Test
    @DisplayName("We should retrieve error due non existent user for a given id as a parameter")
    void shouldThrowErrorDueUnExistentUserForGivenId() {

        when(addressRepository.findByAddressId(anyString())).thenReturn(null);
        Assertions.assertThrows(UserException.class, () -> addressService.findByAddressId(anyString()));
        verify(addressRepository, times(1)).findByAddressId(anyString());
    }

    @Test
    @DisplayName("We should successfully retrieve the address for an existent user id")
    void shouldReturnAddressForExistentUserId() {

        final AddressEntity addressEntity = getAddressEntity();
        when(addressRepository.findByAddressId(anyString())).thenReturn(addressEntity);
        final AddressDto addressDto = addressService.findByAddressId(anyString());
        assertThat(addressDto).isNotNull();
        verify(addressRepository, times(1)).findByAddressId(anyString());

    }

    @NotNull
    private AddressEntity getAddressEntity() {
        final AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCountry("Argentina");
        addressEntity.setCity("Cordoba");
        addressEntity.setPostalCode("5000");
        addressEntity.setStreetName("CpazStreet");
        addressEntity.setType("Main Address");
        return addressEntity;
    }

    private List<AddressEntity> retrieveAddresses() {

        UserEntity user = new UserEntity();
        user.setLastName("userlastnametest");
        user.setFirstName("userfirstnametest");
        user.setEmail("test@testemail.com");
        when(userRepository.findByUserId(anyString())).thenReturn(user);
        AddressEntity addressUserOne = getAddressEntity();
        return Stream.of(addressUserOne).collect(Collectors.toList());
    }


}
