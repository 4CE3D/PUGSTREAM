package com.moviestogether.pugstream.Room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Queue {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    int id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;
    @OneToMany(
            mappedBy = "queue",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Movie> movies = new ArrayList<>();
}
