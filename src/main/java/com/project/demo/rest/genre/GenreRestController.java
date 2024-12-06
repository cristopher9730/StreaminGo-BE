package com.project.demo.rest.genre;
import com.project.demo.logic.entity.genre.Genre;
import com.project.demo.logic.entity.genre.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie-genres")
public class GenreRestController {

    @Autowired
    private GenreRepository GenreRepository;
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Genre> getAllGenres() {
        return GenreRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Genre addGenre(@RequestBody Genre genre) {
        return GenreRepository.save(genre);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return GenreRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/filterByName/{name}")
    public List<Genre> getGenreById(@PathVariable String name) {
        return GenreRepository.findGenreWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Genre updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        return GenreRepository.findById(id)
                .map(existingGenre -> {
                    existingGenre.setName(genre.getName());
                    return GenreRepository.save(existingGenre);
                })
                .orElseGet(() -> {
                    genre.setId(id);
                    return GenreRepository.save(genre);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteGenre(@PathVariable Long id) {
        GenreRepository.deleteById(id);
    }
}
