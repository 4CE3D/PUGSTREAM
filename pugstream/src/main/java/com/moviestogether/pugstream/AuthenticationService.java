package com.moviestogether.pugstream;

import com.moviestogether.pugstream.Room.Room;
import com.moviestogether.pugstream.Room.RoomRepository;
import com.moviestogether.pugstream.Room.User;
import com.moviestogether.pugstream.Room.UserRepository;
import com.moviestogether.pugstream.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.moviestogether.pugstream.Room.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse enterRoom(String name, long roomId, String password) {

        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty())
            return new AuthenticationResponse();

        if(password.equals(room.get().getPassword()))
        {
            User user = new User();
            user.setName(name);
            user.setRole(Role.USER);
            user.setRoom(room.get());
            userRepository.save(user);

            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .room(room.get())
                    .build();
        }

        return new AuthenticationResponse();
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
