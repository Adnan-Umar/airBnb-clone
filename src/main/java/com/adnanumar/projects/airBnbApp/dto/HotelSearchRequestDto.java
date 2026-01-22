package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelSearchRequestDto {

    String city;

    LocalDate startDate;

    LocalDate endDate;

    Integer roomsCount;

    Integer page = 0;

    Integer size = 10;

}
