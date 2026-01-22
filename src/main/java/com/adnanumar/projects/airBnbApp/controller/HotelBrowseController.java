package com.adnanumar.projects.airBnbApp.controller;

import com.adnanumar.projects.airBnbApp.dto.HotelDto;
import com.adnanumar.projects.airBnbApp.dto.HotelSearchRequestDto;
import com.adnanumar.projects.airBnbApp.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelBrowseController {

    final InventoryService inventoryService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequestDto hotelSearchRequest) {
        Page<HotelDto> page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

}
