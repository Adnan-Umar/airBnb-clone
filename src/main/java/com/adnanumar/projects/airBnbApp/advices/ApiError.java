package com.adnanumar.projects.airBnbApp.advices;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {

    HttpStatus status;

    String message;

    List<String> subErrors;

}
