
package com.moviestogether.pugstream.Room;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.moviestogether.pugstream.AuthenticationService;
import com.moviestogether.pugstream.auth.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private RoomController roomController;

    private Room validRoom;
    private Room invalidRoom;
    private User adminUser;

    @BeforeEach
    void setUp() {
        validRoom = new Room(1,"Room","private","password",null, null, null);
        invalidRoom = null;
        adminUser = new User(1, "user",Role.ADMIN,validRoom);

        lenient().when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));
        lenient().when(roomRepository.findById(-1L)).thenReturn(Optional.empty());
        lenient().when(roomRepository.findAll()).thenReturn(Arrays.asList(validRoom));
    }

    @Test
    void testGetRoomInfo_validId() {
        ResponseEntity<Room> response = roomController.getRoomInfo(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validRoom, response.getBody());
    }

    @Test
    void testGetRoomInfo_invalidId() {
        ResponseEntity<Room> response = roomController.getRoomInfo(-1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testIndex() {
        ResponseEntity response = roomController.index();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
    }

    @Test
    @WithMockUser(username="user", authorities={"ROLE_ADMIN"})
    void testEnterPrivateRoom_Success() {
        // Arrange
        Room room = new Room();
        room.setId(1);
        room.setType("private");
        room.setPassword(new BCryptPasswordEncoder().encode("password"));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Room");
        requestBody.put("password", "password");

        AuthenticationResponse expectedResponse = new AuthenticationResponse("ValidToken",validRoom);
        when(authenticationService.enterRoom("Room", 1L, "password")).thenReturn(expectedResponse);

        ResponseEntity<?> response = roomController.enterRoom(1L, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "The response status should be 200 OK.");
        assertNotNull(response.getBody(), "The response body should not be null.");
        assertTrue(response.getBody() instanceof AuthenticationResponse, "The body should be an instance of AuthenticationResponse.");
        AuthenticationResponse responseBody = (AuthenticationResponse) response.getBody();
        assertEquals("ValidToken", responseBody.getToken(), "The token should match the expected.");
    }
    @Test
    @WithMockUser(username="user", authorities={"ROLE_ADMIN"})
    void testEnterPrivateRoom_WrongPassword() {
        // Arrange
        Room room = new Room();
        room.setId(1);
        room.setType("private");

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        room.setPassword(bcrypt.encode("correctPassword"));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Room");
        requestBody.put("password", "wrongPassword");

        ResponseEntity<?> response = roomController.enterRoom(1L, requestBody);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "The response status should be 401 UNAUTHORIZED.");
        assertTrue(response.getBody() instanceof String, "The response body should be a String.");
        assertEquals("Invalid password.", response.getBody(), "The error message should indicate that the password is invalid.");
    }
    @Test
    @WithMockUser(username="user", authorities={"ROLE_USER"})
    void testEnterPublicRoom_OnlyUsernameRequired() {
        // Arrange
        Room room = new Room();
        room.setId(1);
        room.setType("public");
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Room");

        AuthenticationResponse expectedResponse = new AuthenticationResponse("AccessGranted",validRoom);
        when(authenticationService.enterRoom("Room", 1L, null)).thenReturn(expectedResponse);

        ResponseEntity<?> response = roomController.enterRoom(1L, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "The response status should be 200 OK.");
        assertNotNull(response.getBody(), "The response body should not be null.");
        assertTrue(response.getBody() instanceof AuthenticationResponse, "The body should be an instance of AuthenticationResponse.");
        AuthenticationResponse responseBody = (AuthenticationResponse) response.getBody();
        assertEquals("AccessGranted", responseBody.getToken(), "The token should indicate access is granted.");

        // verify no password needed
        verify(authenticationService).enterRoom("Room", 1L, null);
    }


    @Test
    @WithMockUser(username="user", authorities={"ROLE_ADMIN"})
    void testEnterRoom_RoomNotFound() {

        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Room");
        requestBody.put("password", "password");

        ResponseEntity<?> response = roomController.enterRoom(999L, requestBody);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response status should be 404.");
        assertTrue(response.getBody() instanceof String, "Response body should be string.");
        assertEquals("Room not found.", response.getBody(), "Room was not found.");
    }

    @Test
    @WithMockUser(username="user", authorities={"ROLE_ADMIN"})
    void testEnterRoom_invalidCredentials() {

    }

    @Test
    void testAddRoom_validData() {
        when(roomRepository.save(any(Room.class))).thenReturn(validRoom);
        when(authenticationService.createRoom(validRoom)).thenReturn(new AuthenticationResponse());
        ResponseEntity<AuthenticationResponse> response = roomController.AddRoom(validRoom);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteRoom_authorizedUser() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(adminUser, null, Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))));
        ResponseEntity<Room> response = roomController.DeleteRoom(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomRepository, times(1)).delete(validRoom);
    }

    @Test
    void testDeleteRoom_unauthorizedUser() {
        Room validRoom = new Room(2,"Room","private", "password", null, null, null);
        when(roomRepository.findById(2L)).thenReturn(Optional.of(validRoom));
        // have a user join the room
        User unauthorizedUser = new User();
        unauthorizedUser.setRoom(validRoom);
        Authentication authentication = new UsernamePasswordAuthenticationToken(unauthorizedUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // attempt to delete an existing room with an unauthorized user
        ResponseEntity<Room> response = roomController.DeleteRoom(2L);

        // make sure status code is unauthorized
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // verify delete method is not available
        verify(roomRepository, never()).delete(validRoom);
    }

}
