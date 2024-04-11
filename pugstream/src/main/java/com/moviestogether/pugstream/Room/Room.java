package com.moviestogether.pugstream.Room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue (strategy = GenerationType.TABLE)
    private int id;
    private String name;
    @OneToOne(mappedBy = "room")
    private Queue queue;

}
