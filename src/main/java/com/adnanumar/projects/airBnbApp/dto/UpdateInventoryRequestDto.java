package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInventoryRequestDto {

    LocalDate startDate;

    LocalDate endDate;

    BigDecimal surgeFactor;

    Boolean closed;

}
