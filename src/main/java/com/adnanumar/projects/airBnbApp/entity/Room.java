package com.adnanumar.projects.airBnbApp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @Column(nullable = false)
    String type;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal basePrice;

    @Column(columnDefinition = "TEXT[]")
    String[] photos;

    @Column(columnDefinition = "TEXT[]")
    String[] amenities;

    @Column(nullable = false)
    Integer totalCount;

    @Column(nullable = false)
    Integer capacity;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

}
