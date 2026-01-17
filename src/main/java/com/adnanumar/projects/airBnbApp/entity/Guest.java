package com.adnanumar.projects.airBnbApp.entity;

import com.adnanumar.projects.airBnbApp.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    Gender gender;

    Integer age;

}
