package com.adnanumar.projects.airBnbApp.dto;

import com.adnanumar.projects.airBnbApp.entity.enums.Gender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequestDto {

    String name;

    LocalDate dateOfBirth;

    Gender gender;

}
