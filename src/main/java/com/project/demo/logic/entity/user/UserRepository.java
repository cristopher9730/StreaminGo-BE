package com.project.demo.logic.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE %?1% AND u.role.name = 'USER'")
    List<User> findUsersWithCharacterInName(String character);

    @Query("SELECT u FROM User u WHERE u.role.name = 'USER'")
    List<User> findAllUsersWithUserRole();

    @Query("SELECT u FROM User u WHERE u.name = ?1")
    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    @Query("SELECT YEAR(u.createdAt), MONTH(u.createdAt), COUNT(u) " +
            "FROM User u GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
            "ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)")
    List<Object[]> countUsersByMonth();
}
