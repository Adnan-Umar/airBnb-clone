package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelInfoDto {

    HotelDto hotel;

    List<RoomDto> rooms;

}
