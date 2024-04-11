package com.moviestogether.pugstream.Room;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue") // Probably needs room id
public class QueueController {
    private QueueRepository repository;

    @GetMapping("/")
    public String queuePage() {
        return "Add your movie";
    }

    @PostMapping
    public void addToQueue(@RequestBody Movie movie) {
        if(movie.getLink() == null){ // Needs to be checked with db(repository)
            throw new IllegalStateException("No link provided");
        }
        repository.save(movie);
    }

    @DeleteMapping(path = "movieId")
    public void deleteFromQueue(@PathVariable("movieId") Long movieId){
        if(movieId == null) { // Needs to be checked with db(repository)
            throw new IllegalStateException("");
        }
        repository.deleteById(movieId);
    }

}
