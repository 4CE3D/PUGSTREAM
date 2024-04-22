package com.moviestogether.pugstream;

import com.moviestogether.pugstream.Room.Room;
import com.moviestogether.pugstream.Room.RoomRepository;
import com.moviestogether.pugstream.Room.User;
import com.moviestogether.pugstream.Room.UserRepository;
import com.moviestogether.pugstream.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.moviestogether.pugstream.Room.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse enterRoom(String name, long roomId, String password) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (!roomOptional.isPresent()) {
            return new AuthenticationResponse();
        }

        Room room = roomOptional.get();

        // check if room is private and password is correct
        if ("private".equals(room.getType())) {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            if (!bcrypt.matches(password, room.getPassword())) {
                return new AuthenticationResponse();
            }

            // creating user session and generating token
            User user = new User();
            user.setName(name);
            user.setRole(Role.USER);
            user.setRoom(room);
            userRepository.save(user);

            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .room(room)
                    .build();
        }

        // room is public (no password check needed)
        if ("public".equals(room.getType())) {
            User user = new User();
            user.setName(name);
            user.setRole(Role.USER);
            user.setRoom(room);
            userRepository.save(user);

            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .room(room)
                    .build();
        }

        return new AuthenticationResponse();  // if room type isn't 'private' or 'public'
    }

    public AuthenticationResponse createRoom(Room room) {
        User user = new User();
        user.setRoom(room);
        user.setName("PUG");
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(Role.USER))
                .build();

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .room(room)
                .build();
    }
}
