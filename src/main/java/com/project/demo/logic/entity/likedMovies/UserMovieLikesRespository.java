package com.project.demo.logic.entity.likedMovies;

import com.project.demo.logic.entity.movie.Movie;
import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserMovieLikesRespository extends JpaRepository<UserMovieLikes, Long> {
    Optional<UserMovieLikes> findByUserAndMovie(User user, Movie movie);
    Long countByMovie(Movie movie);
    List<UserMovieLikes> findByUser(User user);
    boolean existsByUserAndMovie(User user, Movie movie);

    @Query("SELECT u.movie, COUNT(u.movie) as likeCount " +
            "FROM UserMovieLikes u " +
            "GROUP BY u.movie " +
            "ORDER BY likeCount DESC")
    List<Movie> findTop5MoviesWithMostLikes(Pageable pageable);

}
