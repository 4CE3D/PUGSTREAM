package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/")
    public ResponseEntity getAllUsers(@PathVariable Long roomId)
    {
        if(getUserRoom(roomId))
        {
            List<User> userList = repository.findAllInRoom(roomId);
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @GetMapping("/{userId}")
    public ResponseEntity getOneUser(@PathVariable Long roomId, @PathVariable Long userId)
    {
        if(getUserRoom(roomId))
        {
            Optional<User> user = repository.findUserInRoom(roomId, userId);
            if(user.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @PostMapping("/")
    public ResponseEntity createUserInRoom(@PathVariable Long roomId, @RequestBody User user)
    {
        if(getUserAuthority(roomId))
        {
            Optional<Room> room = roomRepository.findById(roomId);
            if(room.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

            user.setRoom(room.get());
            repository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long roomId, @PathVariable Long userId)
    {

        if(getUserAuthority(roomId))
        {
            Optional<User>user = repository.findUserInRoom(roomId, userId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User principal =(User)authentication.getPrincipal();
            if(user.isPresent())
            {
                if(principal.getRoom().getId() == roomId && authentication.getAuthorities().toString().equals("[ADMIN]"))
                {
                    repository.delete(user.get());

                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }
}
