package com.moviestogether.pugstream.Room;

import com.moviestogether.pugstream.auth.AuthenticationResponse;
import com.moviestogether.pugstream.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Validated
@RequestMapping("/room")
@CrossOrigin(maxAge = 3600)
@AllArgsConstructor
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
    private final AuthenticationService service;
    @GetMapping("/info/{roomId}")
    public ResponseEntity<Room> getRoomInfo(@PathVariable Long roomId) {
        return repository.findById(roomId)
                .map(room -> ResponseEntity.ok(room))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/")
    public ResponseEntity index() {
        List<Room> room=repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(room);
    }
    @PostMapping("/{roomId}")
    public ResponseEntity<?> enterRoom(@PathVariable long roomId, @RequestBody Map<String, Object> requestBody) {
        String name = (String) requestBody.get("name");  // allow null values, check for empty later
        String password = (String) requestBody.get("password");  // allows null for public rooms


        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required.");
        }

        Optional<Room> roomOptional = repository.findById(roomId);
        if (!roomOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found.");
        }

        Room room = roomOptional.get();

        // check password for private rooms
        if ("private".equals(room.getType())) {
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required for private rooms.");
            }
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            if (!bcrypt.matches(password, room.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
        } else if ("public".equals(room.getType())) {
            // for public rooms ensure no password is provided
        }


        AuthenticationResponse response = service.enterRoom(name, roomId, password);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity AddRoom(@Valid @RequestBody Room room) {
        BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder();
        String encryptedPassword=bcrypt.encode(room.getPassword());
        room.setPassword(encryptedPassword);
        repository.save(room);
        AuthenticationResponse response = service.createRoom(room);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity DeleteRoom(@PathVariable long id ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        repository.findById(id);
        Optional<Room> room=repository.findById(id);
        if(room.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).body("String not found");}
        System.out.println(authentication.getPrincipal());
        User principal =(User)authentication.getPrincipal();
        if(principal.getRoom().getId() == room.get().getId() && authentication.getAuthorities().toString().equals("[ADMIN]"))
        {
            repository.delete(room.get());
            return ResponseEntity.status(HttpStatus.OK).body(room);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");

    }

}


