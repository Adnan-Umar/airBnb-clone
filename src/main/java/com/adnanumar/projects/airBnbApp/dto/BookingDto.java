package com.adnanumar.projects.airBnbApp.dto;

import com.adnanumar.projects.airBnbApp.entity.User;
import com.adnanumar.projects.airBnbApp.entity.enums.BookingStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    Long id;

    Integer roomsCount;

    LocalDate checkInDate;

    LocalDate checkOutDate;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    BookingStatus bookingStatus;

    Set<GuestDto> guests;

}
