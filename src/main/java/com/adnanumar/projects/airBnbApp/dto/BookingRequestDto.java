package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDto {

    Long hotelId;

    Long roomId;

    LocalDate checkInDate;

    LocalDate checkOutDate;

    Integer roomsCount;

}
