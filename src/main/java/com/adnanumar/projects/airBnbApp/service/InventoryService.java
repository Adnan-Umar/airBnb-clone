package com.adnanumar.projects.airBnbApp.service;

import com.adnanumar.projects.airBnbApp.dto.*;
import com.adnanumar.projects.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    void initializeRoomForAYear(Room Room);

    void deleteAllInventories(Room Room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequestDto hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);

}
