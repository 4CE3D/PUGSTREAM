//package com.moviestogether.pugstream.Room;
//
//import com.moviestogether.pugstream.AuthenticationService;
//import com.moviestogether.pugstream.JwtService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.lenient;
//
//@ActiveProfiles
//@Import(JwtService.class)
//@ExtendWith(MockitoExtension.class)
//@Component
//class UserControllerTest {
//
//    @Mock
//    private RoomRepository roomRepository;
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AuthenticationService authenticationService;
//
//    @InjectMocks
//    private RoomController roomController;
//
//    private Room validRoom;
//    private Room invalidRoom;
//    private User adminUser;
//    @BeforeEach
//    void setUp() {
//        validRoom = new Room(1,"Room","private","password",null, null, null);
//        invalidRoom = null;
//        adminUser = new User(1, "user",Role.ADMIN,validRoom);
//
//        lenient().when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));
//        lenient().when(roomRepository.findById(-1L)).thenReturn(Optional.empty());
////        when(roomRepository.findAll()).thenReturn(Arrays.asList(validRoom));
//    }
//
//    @Test
//    void getAllUsers() {
//        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
//        ResponseEntity<List<User>> response = controller.getAllUsers(1L);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//    }
//
//    @Test
//    void getOneUser() {
//        User user = new User();
//        when(userRepository.findUserInRoom(anyLong(), anyLong())).thenReturn(Optional.of(user));
//        ResponseEntity<User> response = controller.getOneUser(1L, 1L);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertSame(user, response.getBody());
//    }
//
//    @Test
//    void createUserInRoom() {
//
//        when(roomRepository.findById(2L)).thenReturn(Optional.of(validRoom));
//        when(userRepository.save(any(User.class))).thenReturn(adminUser);
//        ResponseEntity<User> response = controller.createUserInRoom(2L, adminUser);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertSame(adminUser, response.getBody());
//    }
//
//    @Test
//    void deleteUser() {
//        User user = new User();
//        when(userRepository.findUserInRoom(anyLong(), anyLong())).thenReturn(Optional.of(user));
//        doNothing().when(userRepository).delete(any(User.class));
//        ResponseEntity<String> response = controller.deleteUser(1L, 1L);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//    }
//}
