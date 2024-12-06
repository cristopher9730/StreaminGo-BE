package com.project.demo.logic.entity.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT u FROM Movie u WHERE LOWER(u.name) LIKE %?1%")
    List<Movie> findMoviesWithCharacterInName(String character);

    @Query("SELECT u FROM Movie u WHERE LOWER(u.name) LIKE %?1%")
    List<Movie> findMoviesByGenre(String genre);

    @Query("SELECT u FROM Movie u WHERE u.name = ?1")
    Optional<Movie> findByName(String name);

    Optional<Movie> findByDescription(String description);

}
