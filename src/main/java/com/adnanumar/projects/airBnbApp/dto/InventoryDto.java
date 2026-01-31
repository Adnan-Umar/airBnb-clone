package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryDto {

    Long id;

    LocalDate date;

    Integer bookedCount;

    Integer reservedCount;

    Integer totalCount;

    BigDecimal surgeFactor;

    BigDecimal price;

    Boolean closed;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}
