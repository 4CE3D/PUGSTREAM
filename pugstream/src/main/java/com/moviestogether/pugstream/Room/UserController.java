package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room/{roomId}/users")
public class UserController {

    @Autowired
    UserRepository repository;

    @Autowired
    RoomRepository roomRepository;

    @GetMapping("/")
    public ResponseEntity getAllUsers(@PathVariable Long roomId)
    {
        List<User> userList = repository.findAllInRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getOneUser(@PathVariable Long roomId, @PathVariable Long userId)
    {
        Optional<User> user = repository.findUserInRoom(roomId, userId);
        if(user.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/")
    public ResponseEntity createUserInRoom(@PathVariable Long roomId, @RequestBody User user)
    {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

        user.setRoom(room.get());
        repository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long roomId, @PathVariable Long userId)
    {
        Optional<User>user = repository.findUserInRoom(roomId, userId);
        if(user.isPresent())
        {
            repository.delete(user.get());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");

    }
}
