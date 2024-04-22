package com.moviestogether.pugstream.websocket;

import com.moviestogether.pugstream.JwtService;
import com.moviestogether.pugstream.Room.Role;
import com.moviestogether.pugstream.Room.User;
import com.moviestogether.pugstream.Room.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.HtmlUtils;

import java.util.Optional;

@Controller
@CrossOrigin(maxAge = 3600)
@AllArgsConstructor
public class MessageController {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDetailsService userDetailsService;

    @Autowired
    private final UserRepository userRepository;

    @MessageMapping("/controls/{roomId}")
    @SendTo("/topic/commands/{roomId}")
    public Message greet(@DestinationVariable String roomId, Request message) {



        String username = jwtService.extractUsername(message.getToken());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if(jwtService.isTokenValid(message.getToken(), userDetails))
        {
            Optional<User> user = userRepository.findUserInRoom(Long.parseLong(roomId), Long.parseLong(username));
            if(user.isPresent())
            {
                if(user.get().getRole() == Role.ADMIN)
                {
                    System.out.println(username);
                    // Process the message and return a response
                    return new Message(HtmlUtils.htmlEscape(message.getName()));
                }

            }
        }

        return null;
    }
}
