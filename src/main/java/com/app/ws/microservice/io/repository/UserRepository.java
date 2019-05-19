package com.app.ws.microservice.io.repository;

import com.app.ws.microservice.io.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(final String email);
    UserEntity findByUserId(final String id);

}
