package com.moviestogether.pugstream.Room;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {
@Autowired
RoomRepository repository;
    @GetMapping("/")
    public ResponseEntity index() {
        List<Room> room=repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(room);
    }
    @PostMapping("/")
    public ResponseEntity AddRoom(@RequestBody Room room) {

        repository.save(room);
        return ResponseEntity.status(HttpStatus.OK).body(room);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity DeleteRoom(@PathVariable long id ) {
        repository.findById(id);
        Optional<Room> room=repository.findById(id);
        if(room.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).body("String not found");}
        repository.delete(room.get());
        return ResponseEntity.status(HttpStatus.OK).body(room);

    }

}


