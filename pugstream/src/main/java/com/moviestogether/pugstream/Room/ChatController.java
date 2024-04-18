package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/room/{id}/chat")
public class ChatController {
    @Autowired
    private ChatRepository repository;
    @Autowired
    private RoomRepository roomRepository;

    private boolean getUserAuthority(long roomId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User)authentication.getPrincipal();

        return principal.getRoom().getId() == roomId && authentication.getAuthorities().toString().equals("[ADMIN]");
    }

    private boolean getUserRoom(long roomId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User)authentication.getPrincipal();

        return principal.getRoom().getId() == roomId;
    }

    @GetMapping("/")
    public ResponseEntity<?> getChatMessages(@PathVariable Long id) {
        if(getUserRoom(id)) {
            List<Chat> chat = repository.findByRoomId(id);
            return ResponseEntity.status(HttpStatus.OK).body(chat);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @PostMapping("/")
    public ResponseEntity<?> sendUserMessage(@PathVariable Long id, @RequestBody Chat chat) {
        if(chat.getUsername().isEmpty()) {
            throw new IllegalStateException("No user found");
        }
        if(getUserAuthority(id)) {
            Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalStateException("Room not exists"));
            chat.setRoom(room);
            repository.save(chat);
            return ResponseEntity.status(HttpStatus.OK).body(chat);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }
}