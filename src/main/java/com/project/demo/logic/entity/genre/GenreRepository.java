package com.project.demo.logic.entity.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT u FROM Genre u WHERE LOWER(u.name) LIKE %?1%")
    List<Genre> findGenreWithCharacterInName(String character);

    @Query("SELECT u FROM Genre u WHERE u.name = ?1")
    Optional<Genre> findByName(String name);

}
