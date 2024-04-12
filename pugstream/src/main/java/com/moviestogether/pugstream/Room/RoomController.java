package com.moviestogether.pugstream.Room;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/room")
public class RoomController {
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
@Autowired
RoomRepository repository;
    @GetMapping("/")
    public ResponseEntity index() {
        List<Room> room=repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(room);
    }
    @PostMapping("/")
    public ResponseEntity AddRoom(@Valid @RequestBody Room room) {

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


