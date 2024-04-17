package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/room/{id}/queue")
public class QueueController {
    @Autowired
    private QueueRepository repository;
    @Autowired
    private RoomRepository roomRepository;

    private boolean getUserAuthority(long roomId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User principal =(User)authentication.getPrincipal();
        if(principal.getRoom().getId() == roomId && authentication.getAuthorities().toString().equals("[ADMIN]"))
        {
            return true;
        }
        return false;
    }

    private boolean getUserRoom(long roomId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User principal =(User)authentication.getPrincipal();
        if(principal.getRoom().getId() == roomId)
        {
            return true;
        }
        return false;
    }

    @GetMapping("/")
    public ResponseEntity<?> queuePage(@PathVariable Long id) {
        if(getUserRoom(id))
        {
            List<Movie> movies = repository.findByRoomId(id);
            return ResponseEntity.status(HttpStatus.OK).body(movies);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @PostMapping("/")
    public ResponseEntity<?> addToQueue(@PathVariable Long id,@RequestBody Movie movie) {
        if(movie.getLink() == null){ // Needs to be checked with db(repository)
            throw new IllegalStateException("No link provided");
        };
        if(getUserAuthority(id))
        {
            Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalStateException("Room not exists"));
            movie.setRoom(room);
            repository.save(movie);
            return ResponseEntity.status(HttpStatus.OK).body(movie);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteFromQueue(@PathVariable("id") Long id, @PathVariable("movieId") Long movieId){
        if(!repository.existsById(movieId)) { // Needs to be checked with db(repository)
            throw new IllegalStateException("No movie found");
        }
        if(getUserAuthority(id))
        {
            Optional<Movie> deletedMovie = repository.findById(movieId);
            repository.deleteById(movieId);
            return ResponseEntity.status(HttpStatus.OK).body(deletedMovie);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");

    }

}