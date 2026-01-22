package com.adnanumar.projects.airBnbApp.service;

import com.adnanumar.projects.airBnbApp.dto.HotelDto;
import com.adnanumar.projects.airBnbApp.dto.HotelSearchRequestDto;
import com.adnanumar.projects.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room Room);

    void deleteAllInventories(Room Room);

    Page<HotelDto> searchHotels(HotelSearchRequestDto hotelSearchRequest);

}
