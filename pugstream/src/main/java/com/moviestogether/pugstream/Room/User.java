package com.moviestogether.pugstream.Room;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private Role role;

    @ManyToOne
    @JoinColumn(name = "fk_room")
    private Room room;
}

enum Role {
    USER,
    ADMIN
}
