package com.moviestogether.pugstream.Room;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/room/{roomId}/users")
public class UserController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // You can add more sophisticated parsing of the exception message if needed
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("This name is already in use.");
    }
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
    public ResponseEntity createUserInRoom(@PathVariable Long roomId, @Valid @RequestBody User user)
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
