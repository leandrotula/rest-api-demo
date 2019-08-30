package com.app.ws.microservice.service;

import com.app.ws.microservice.exceptions.UserException;
import com.app.ws.microservice.io.entity.AddressEntity;
import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.AddressRepository;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.AddressDto;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceImplTest {

    private AddressServiceImpl addressService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {

        addressService = new AddressServiceImpl();
        addressService.setUserRepository(userRepository);
        addressService.setAddressRepository(addressRepository);
        addressService.setModelMapper(new ModelMapper());
    }

    @Test
    public void shouldGetEmptyAddressDueUnExistentUser() {

        when(userRepository.findByUserId(anyString())).thenReturn(null);
        final List<AddressDto> addresses = addressService.findAddresses("idusertest");
        assertThat(addresses.isEmpty()).isTrue();
        verify(userRepository, times(1)).findByUserId(anyString());
    }

    @Test
    public void shouldThrowErrorDueNonAddressForUser() {

        UserEntity user = new UserEntity();
        when(userRepository.findByUserId(anyString())).thenReturn(user);
        List<AddressEntity> addresses = Collections.emptyList();
        when(addressRepository.findAllByUserDto(any(UserEntity.class))).thenReturn(addresses);
        expectedException.expect(UserException.class);
        addressService.findAddresses(anyString());
        verify(userRepository, times(1)).findByUserId(anyString());
        verify(addressRepository, times(1)).findAllByUserDto(any(UserEntity.class));

    }

    @Test
    public void shouldAddressesAssociatedToAnExistentUser() {

        List<AddressEntity> addresses = retrieveAddresses();
        when(addressRepository.findAllByUserDto(any(UserEntity.class))).thenReturn(addresses);
        final List<AddressDto> userAddresses = addressService.findAddresses("idusertest");
        assertThat(userAddresses.isEmpty()).isFalse();
        verify(userRepository, times(1)).findByUserId(anyString());
        verify(addressRepository, times(1)).findAllByUserDto(any(UserEntity.class));

    }

    @Test
    public void shouldThrowErrorDueUnExistentUserForGivenId() {

        when(addressRepository.findByAddressId(anyString())).thenReturn(null);
        expectedException.expect(UserException.class);
        addressService.findByAddressId(anyString());
        verify(addressRepository, times(1)).findByAddressId(anyString());
    }

    @Test
    public void shouldReturnAddressForExistentUserId() {

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
