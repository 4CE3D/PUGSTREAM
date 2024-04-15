package com.moviestogether.pugstream.Room;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull(message="Name is mandatory")
    @NotBlank(message="Name is mandatory")
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
