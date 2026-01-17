package com.adnanumar.projects.airBnbApp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelContactInfo {

    String address;

    String phoneNumber;

    String email;

    String location;

}
