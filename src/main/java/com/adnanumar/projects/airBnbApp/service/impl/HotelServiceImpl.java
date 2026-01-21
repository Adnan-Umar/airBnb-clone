package com.adnanumar.projects.airBnbApp.service.impl;

import com.adnanumar.projects.airBnbApp.dto.HotelDto;
import com.adnanumar.projects.airBnbApp.entity.Hotel;
import com.adnanumar.projects.airBnbApp.entity.Room;
import com.adnanumar.projects.airBnbApp.exception.ResourceNotFoundException;
import com.adnanumar.projects.airBnbApp.repository.HotelRepository;
import com.adnanumar.projects.airBnbApp.service.HotelService;
import com.adnanumar.projects.airBnbApp.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelServiceImpl implements HotelService {

    final HotelRepository hotelRepository;

    final InventoryService inventoryService;

    final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new Hotel with name {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new Hotel with ID : {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting Hotel with ID : {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID : " + id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the Hotel with ID : {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID : " + id));
        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        log.info("Update Hotel successfully with ID : {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        log.info("Deleting the Hotel with ID : {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID : " + id));
        hotelRepository.deleteById(id);
        for(Room room : hotel.getRooms()) {
            inventoryService.deleteFutureInventories(room);
        }
        log.info("Delete Hotel successfully with ID : {}", id);
    }

    @Override
    @Transactional
    public void activateHotelById(Long id) {
        log.info("Activating the Hotel with ID : {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID : " + id));
        hotel.setActive(true);

        // assuming only do it once
        for (Room room : hotel.getRooms()) {
            inventoryService.initializeRoomForAYear(room);
        }
    }

}
