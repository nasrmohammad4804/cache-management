package com.nasr.caffeinecache.domain;


import jakarta.persistence.*;
import lombok.*;

import static com.nasr.caffeinecache.domain.User.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TABLE_NAME)
public class User {

    public static final String TABLE_NAME="user_table";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    @Builder
    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
