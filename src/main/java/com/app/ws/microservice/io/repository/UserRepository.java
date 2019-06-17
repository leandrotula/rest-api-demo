package com.app.ws.microservice.io.repository;

import com.app.ws.microservice.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(final String email);
    UserEntity findByUserId(final String id);

    @Query(value = "select * from users u where u.email_verification_status = '1'",
            countQuery = "select count(*) from users u where u.email_verification_status = '1'",
            nativeQuery = true)
    Page<UserEntity> retrieveAllUsersWithConfirmedEmail(final Pageable request);


}
