package com.app.ws.microservice.io.repository;

import com.app.ws.microservice.io.entity.AddressEntity;
import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.shared.dto.UserDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUserDto(final UserEntity userEntity);
    AddressEntity findByAddressId(final String addressId);
}
