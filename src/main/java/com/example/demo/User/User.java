package com.example.demo.User;

import lombok.*;
import javax.persistence.*; // JPA

@javax.persistence.Entity
@org.hibernate.annotations.Entity
@javax.persistence.Table
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Getter @Setter int id;
    private @Getter @Setter String name;
    private @Getter @Setter String email;
}
