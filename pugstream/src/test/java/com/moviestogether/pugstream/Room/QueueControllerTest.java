package com.moviestogether.pugstream.Room;

import com.moviestogether.pugstream.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private QueueRepository queueRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private QueueController queueController;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private RoomController roomController;

    private Room validRoom;
    private Room invalidRoom;
    private User adminUser;
    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        when(authentication.getPrincipal()).thenReturn(new User(1,"user",Role.ADMIN, validRoom)); // Adjust as per your user details

        // Set the mock authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        validRoom = new Room(1,"Room","private","password",null, null, null);
        adminUser = new User(1, "user",Role.ADMIN,validRoom);

        lenient().when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));
        lenient().when(roomRepository.findById(-1L)).thenReturn(Optional.empty());
    }
    @AfterEach
    void tearDown() {
        // Clear the security context after each test to avoid side effects
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    public void testQueuePage_Authorized() throws Exception {
        when(queueRepository.findByRoomId(1L)).thenReturn(Arrays.asList(new Movie(1, "https://www.youtube.com/watch?v=dQw4w9WgXcQ", validRoom)));
        mockMvc.perform(get("/room/1/queue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Movie"));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void testAddToQueue_Valid() throws Exception {
        Movie newMovie = new Movie(2, "https://www.youtube.com/watch?v=dQw4w9WgXcQ", validRoom);
        when(queueRepository.save(any(Movie.class))).thenReturn(newMovie);
        mockMvc.perform(post("/room/1/queue/")
                        .contentType(MediaType.APPLICATION_JSON)
                       // .content("{\"title\":\"Another Movie\",\"link\":\"https://www.youtube.com/watch?v=dQw4w9WgXcQ\"}"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Another Movie"));
    }
    @Test
    void testAddToQueue_NoLinkProvided() {
        // Arrange
        Movie movie = new Movie();
        movie.setLink(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            queueController.addToQueue(1L, movie);
        });

        assertEquals("No link provided", exception.getMessage());
    }

    @Test
    void testAddToQueue_RoomNotExists() {
        // Arrange
        Movie movie = new Movie();
        movie.setLink("http://example.com/movie");

        when(queueController.getUserAuthority(1L)).thenReturn(true);
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            queueController.addToQueue(1L, movie);
        });

        assertEquals("Room not exists", exception.getMessage());
    }

    @Test
    void testAddToQueue_UnauthorizedUser() {
        // Arrange
        Movie movie = new Movie();
        movie.setLink("http://example.com/movie");

        when(queueController.getUserAuthority(1L)).thenReturn(false);

        // Act
        ResponseEntity<?> response = queueController.addToQueue(1L, movie);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testAddToQueue_Success() {
        // Arrange
        Movie movie = new Movie();
        movie.setLink("http://example.com/movie");
        Room room = new Room();
        room.setId(1);

        when(queueController.getUserAuthority(1L)).thenReturn(true);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(queueRepository.save(movie)).thenReturn(movie);

        // Act
        ResponseEntity<?> response = queueController.addToQueue(1L, movie);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(movie, response.getBody());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void testDeleteFromQueue_Valid() throws Exception {
        when(queueRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/room/1/queue/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testQueuePage_Unauthorized() throws Exception {
        mockMvc.perform(get("/room/2/queue"))
                .andExpect(status().isUnauthorized());
    }
}
