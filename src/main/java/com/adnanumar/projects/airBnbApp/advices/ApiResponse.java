package com.adnanumar.projects.airBnbApp.advices;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    LocalDateTime timeStamp;

    T data;

    ApiError error;

    public  ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public  ApiResponse(ApiError error) {
        this();
        this.error = error;
    }

}
