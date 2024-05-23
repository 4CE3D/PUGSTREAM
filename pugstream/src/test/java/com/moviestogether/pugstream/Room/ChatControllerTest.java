package com.moviestogether.pugstream.Room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
})
public class ChatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ChatController chatController;

    private Room validRoom;
    private User adminUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
        validRoom = new Room(1, "Room", "private", "password", null, null, null);
        adminUser = new User(1, "user", Role.ADMIN, validRoom);

      lenient().when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));
       lenient().when(roomRepository.findById(-1L)).thenReturn(Optional.empty());

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Mockito.when(authentication.getPrincipal()).thenReturn(adminUser);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser(username="user", authorities={"ROLE_ADMIN"})
    void getChatMessages_Admin() throws Exception {
        List<Chat> expectedChats = Arrays.asList(new Chat(1, "user", "Hello", validRoom));
       lenient().when(chatRepository.findByRoomId(1L)).thenReturn(expectedChats);

        mockMvc.perform(get("/room/1/chat/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Hello"));
    }
    @Test
    @WithMockUser(username="user", authorities={"ROLE_ADMIN"})
    void getChatMessages_Authuser() throws Exception {
        List<Chat> expectedChats = Arrays.asList(new Chat(1, "user", "Hello", validRoom));
        lenient().when(chatRepository.findByRoomId(1L)).thenReturn(expectedChats);

        mockMvc.perform(get("/room/1/chat/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Hello"));
    }
    @Test
    @WithMockUser(username="user", authorities={"ROLE_ADMIN"})  // Using authorities with the ROLE_ prefix
    void sendUserMessage_AuthorizedUser_Success() throws Exception {
        Chat newChat = new Chat(1, "user", "New message", validRoom);
        when(chatRepository.save(any(Chat.class))).thenReturn(newChat);

        mockMvc.perform(post("/room/1/chat/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"message\":\"New message\"}")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New message"));
    }
}