package com.project.demo.rest.movie;

import com.project.demo.logic.entity.movie.Movie;
import com.project.demo.logic.entity.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieRestController {
    @Autowired
    private MovieRepository MovieRepository;

    @GetMapping
    public List<Movie> getAllMovies() {
        return MovieRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Movie addMovie(@RequestBody Movie movie) {
        return MovieRepository.save(movie);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return MovieRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/filterByName/{name}")
    public List<Movie> getMovieById(@PathVariable String name) {
        return MovieRepository.findMoviesWithCharacterInName(name);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/filterByGenre/{genre}")
    public List<Movie> getMovieByGenre(@PathVariable String genre) {
        return MovieRepository.findMoviesByGenre(genre);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return MovieRepository.findById(id)
                .map(existingMovie -> {
                    existingMovie.setName(movie.getName());
                    existingMovie.setDescription(movie.getDescription());
                    existingMovie.setGenre(movie.getGenre());
                    existingMovie.setImageCover(movie.getImageCover());
                    existingMovie.setVideo(movie.getVideo());
                    existingMovie.setRealesedYear(movie.getRealesedYear());
                    existingMovie.setDuration(movie.getDuration());
                    /**existingMovie.setCast(movie.getCast());*/
                    return MovieRepository.save(existingMovie);
                })
                .orElseGet(() -> {
                    movie.setId(id);
                    return MovieRepository.save(movie);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteMovie(@PathVariable Long id) {
        MovieRepository.deleteById(id);
    }
}
