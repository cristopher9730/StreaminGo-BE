package com.project.demo.logic.entity.passwordResetRequests;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetRequestRepository extends CrudRepository<PasswordResetRequest, Integer> {

    @Query("SELECT u FROM PasswordResetRequest u WHERE u.resetCode = ?1")
    Optional<PasswordResetRequest> findByResetCode(String code);

    @Query("SELECT pr FROM PasswordResetRequest  pr WHERE pr.user.id = ?1")
    List<PasswordResetRequest> findByUserId(Long userId);

}
