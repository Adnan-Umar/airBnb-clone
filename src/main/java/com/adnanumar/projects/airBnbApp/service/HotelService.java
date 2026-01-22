package com.adnanumar.projects.airBnbApp.service;

import com.adnanumar.projects.airBnbApp.dto.HotelDto;
import com.adnanumar.projects.airBnbApp.dto.HotelInfoDto;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id,HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotelById(Long id);

    HotelInfoDto getHotelInfoById(Long hotelId);

}
