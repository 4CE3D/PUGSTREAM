package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/queue") // Probably needs room id
public class QueueController {
    @Autowired
    private QueueRepository repository;

    @GetMapping("/")
    public ResponseEntity<?> queuePage() {
        List<Movie> movies = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @PostMapping
    public ResponseEntity<?> addToQueue(@RequestBody Movie movie) {
        if(movie.getLink() == null){ // Needs to be checked with db(repository)
            throw new IllegalStateException("No link provided");
        }
        repository.save(movie);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @DeleteMapping(path = "movieId")
    public ResponseEntity<?> deleteFromQueue(@PathVariable("movieId") Long movieId){
        if(movieId == null) { // Needs to be checked with db(repository)
            throw new IllegalStateException("");
        }
        Optional<Movie> deletedMovie = repository.findById(movieId);
        repository.deleteById(movieId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedMovie);
    }

}
