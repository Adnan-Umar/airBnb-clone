package com.adnanumar.projects.airBnbApp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "unique_hotel_room_date",
                columnNames = {"hotel_id", "room_id", "date"}
        ))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    Room room;

    @Column(nullable = false)
    LocalDate date;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    Integer bookedCount;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    Integer reservedCount;

    @Column(nullable = false)
    Integer totalCount;

    @Column(nullable = false, precision = 5, scale = 2)
    BigDecimal surgeFactor;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;   // basePrice * surgeFactor

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    Boolean closed;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

}
