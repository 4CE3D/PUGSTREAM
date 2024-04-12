package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
//    public ResponseEntity DeleteRoom(@RequestBody Room room) {
//        repository.delete(room);
//    }

}


