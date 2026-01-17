package com.adnanumar.projects.airBnbApp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "hotel")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    String city;

    @Column(columnDefinition = "TEXT[]")
    String[] photos;

    @Column(columnDefinition = "TEXT[]")
    String[] amenities;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Embedded
    HotelContactInfo contactInfo;

    @Column(nullable = false)
    Boolean active;

}
