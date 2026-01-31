package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class HotelReportDto {

    Long bookingCount;

    BigDecimal totalRevenue;

    BigDecimal avgRevenue;

}
