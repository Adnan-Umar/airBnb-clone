package com.adnanumar.projects.airBnbApp.dto;

import com.adnanumar.projects.airBnbApp.entity.Hotel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {

    Hotel hotel;

    Double price;

}
