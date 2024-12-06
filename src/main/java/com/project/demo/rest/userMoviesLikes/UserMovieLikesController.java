package com.project.demo.rest.userMoviesLikes;

import com.project.demo.logic.entity.likedMovies.UserMovieLikes;
import com.project.demo.logic.entity.likedMovies.UserMovieLikesRespository;
import com.project.demo.logic.entity.movie.Movie;
import com.project.demo.logic.entity.movie.MovieRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/likes")
public class UserMovieLikesController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserMovieLikesRespository userMovieLikesRespository;

    @PostMapping("/user/{userId}/movie/{movieId}")
    public void uploadLike (@PathVariable Long userId, @PathVariable Long movieId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));

        Optional<UserMovieLikes> existingLike = userMovieLikesRespository.findByUserAndMovie(user, movie);

        if (existingLike.isPresent()) {
            throw new RuntimeException("El usuario ya ha dado like a esta película");
        }else {
            UserMovieLikes like = new UserMovieLikes();
            like.setUser(user);
            like.setMovie(movie);

            userMovieLikesRespository.save(like);
        }
    }

    @DeleteMapping("/user/{userId}/movie/{movieId}")
    public void deleteLike (@PathVariable Long userId, @PathVariable Long movieId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));

        Optional<UserMovieLikes> like = userMovieLikesRespository.findByUserAndMovie(user, movie);

        like.ifPresent(userMovieLikesRespository::delete);
    }

    @GetMapping("/count/{movieId}")
    public Long getMovieLikes(@PathVariable Long movieId){
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));

        return userMovieLikesRespository.countByMovie(movie);
    }

    @GetMapping("/user/{userId}/movies")
    public List<Movie> getUserLikes(@PathVariable Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<UserMovieLikes> likes = userMovieLikesRespository.findByUser(user);
        return likes.stream().map(UserMovieLikes::getMovie).collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}/movie/{movieId}/exists")
    public ResponseEntity<Boolean> verificarLike(@PathVariable Long userId, @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));

        boolean exists = userMovieLikesRespository.existsByUserAndMovie(user, movie);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/trending")
    public List<Movie> getTrending(){
        Pageable top5 = PageRequest.of(0, 5);
        return userMovieLikesRespository.findTop5MoviesWithMostLikes(top5);
    }
}
