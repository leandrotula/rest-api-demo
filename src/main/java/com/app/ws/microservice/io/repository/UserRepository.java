package com.app.ws.microservice.io.repository;

import com.app.ws.microservice.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(final String email);
    UserEntity findByUserId(final String id);

    @Query(value = "select * from users u where u.email_verification_status = '1'",
            countQuery = "select count(*) from users u where u.email_verification_status = '1'",
            nativeQuery = true)
    Page<UserEntity> retrieveAllUsersWithConfirmedEmail(final Pageable request);

    @Query(value = "select * from users u where u.first_name=:firstName", nativeQuery = true)
    List<UserEntity> findByUserFirstName(@Param("firstName") String firstName);

    @Query(value = "select * from users u where u.last_name=:lastName", nativeQuery = true)
    List<UserEntity> findByUserLastName(@Param("lastName") String lastName);

    @Query(value = "select * from users u where u.first_name LIKE %:keyword%", nativeQuery = true)
    List<UserEntity> findUserByFirstNameAndKeyword(@Param("keyword") String keyword);

    @Query(value = "select u.first_name, u.last_name from users u where u.first_name =: firstName", nativeQuery = true)
    List<Object[]> findOnlyByUserName(@Param("firstName") String firstName);

    @Transactional
    @Modifying
    @Query(value = "update users u set u.email_verification_status=:emailStatus where u.user_id=:userId", nativeQuery = true)
    void updateUserWithNewEmailStatus(@Param("emailStatus") int status, @Param("userId") String userId);

    /**
     * JPQL version
     * @param id
     * @return {@link UserEntity}
     */
    @Query(value = "select user from UserEntity user where user.userId = :id")
    UserEntity retrieveByUserId(@Param("id") String id);

}
