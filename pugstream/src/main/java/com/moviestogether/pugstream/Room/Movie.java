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
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    int id;
    private String link;
    @ManyToOne
    @JoinColumn(name = "fk_queue")
    private Queue queue;
}
