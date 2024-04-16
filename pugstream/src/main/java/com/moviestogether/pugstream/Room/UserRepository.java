package com.moviestogether.pugstream.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.room.id = :roomId")
    List<User> findAllInRoom(@Param("roomId") Long roomId);

    @Query("SELECT u FROM User u WHERE u.room.id = :roomId AND u.id = :userId")
    Optional<User> findUserInRoom(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.name = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
