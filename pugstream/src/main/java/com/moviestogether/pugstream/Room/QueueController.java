package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room/{id}/queue")
public class QueueController {
    @Autowired
    private QueueRepository repository;
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/")
    public ResponseEntity<?> queuePage(@PathVariable Long id) {
        List<Movie> movies = repository.findByRoomId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @PostMapping("/")
    public ResponseEntity<?> addToQueue(@PathVariable Long id,@RequestBody Movie movie) {
        if(movie.getLink() == null){ // Needs to be checked with db(repository)
            throw new IllegalStateException("No link provided");
        };
        Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalStateException("Room not exists"));
        movie.setRoom(room);
        repository.save(movie);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteFromQueue(@PathVariable("movieId") Long movieId){
        if(!repository.existsById(movieId)) { // Needs to be checked with db(repository)
            throw new IllegalStateException("No movie found");
        }
        Optional<Movie> deletedMovie = repository.findById(movieId);
        repository.deleteById(movieId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedMovie);
    }

}