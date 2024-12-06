package com.project.demo.logic.entity.actor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("SELECT u FROM Actor u WHERE LOWER(u.name) LIKE %?1%")
    List<Actor> findActorsWithCharacterInName(String character);

    @Query("SELECT u FROM Actor u WHERE u.name = ?1")
    Optional<Actor> findByName(String name);

    @Query("SELECT u FROM Actor u WHERE u.lastname = ?1")
    Optional<Actor> findByLastName(String lastname);

    Optional<Actor> findByNationality(String nationality);


}
