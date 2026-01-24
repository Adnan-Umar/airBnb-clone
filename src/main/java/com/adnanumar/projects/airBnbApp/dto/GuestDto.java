package com.adnanumar.projects.airBnbApp.dto;

import com.adnanumar.projects.airBnbApp.entity.User;
import com.adnanumar.projects.airBnbApp.entity.enums.Gender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuestDto {

    Long id;

    User user;

    String name;

    Gender gender;

    Integer age;

}
