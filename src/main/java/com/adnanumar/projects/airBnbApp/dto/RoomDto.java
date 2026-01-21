package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDto {

    Long id;

    String type;

    BigDecimal basePrice;

    String[] photos;

    String[] amenities;

    Integer totalCount;

    Integer capacity;

}
