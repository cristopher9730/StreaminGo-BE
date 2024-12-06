package com.project.demo.logic.entity.likedMovies;

import com.project.demo.logic.entity.movie.Movie;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "likes")
public class UserMovieLikes {

    @Id
    @Column(nullable = false, name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @CreationTimestamp
    @Column(updatable = false, name = "liked_at")
    private Date likedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User usuario) {
        this.user = usuario;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie pelicula) {
        this.movie = pelicula;
    }

    public Date getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(Date likedAt) {
        this.likedAt = likedAt;
    }
}
