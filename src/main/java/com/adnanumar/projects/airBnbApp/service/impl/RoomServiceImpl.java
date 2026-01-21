package com.adnanumar.projects.airBnbApp.service.impl;

import com.adnanumar.projects.airBnbApp.dto.RoomDto;
import com.adnanumar.projects.airBnbApp.entity.Hotel;
import com.adnanumar.projects.airBnbApp.entity.Room;
import com.adnanumar.projects.airBnbApp.exception.ResourceNotFoundException;
import com.adnanumar.projects.airBnbApp.repository.HotelRepository;
import com.adnanumar.projects.airBnbApp.repository.RoomRepository;
import com.adnanumar.projects.airBnbApp.service.InventoryService;
import com.adnanumar.projects.airBnbApp.service.RoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomServiceImpl implements RoomService {

    final RoomRepository roomRepository;

    final HotelRepository hotelRepository;

    final InventoryService inventoryService;

    final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a new Room in hotel with id {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " + hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        if (hotel.getActive()) {
            inventoryService.initializeRoomForAYear(room);
        }
        log.info("Room is created with id {}", room.getId());
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all Rooms in hotel with id {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " + hotelId));
        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting room with id {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id : " + roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with id {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id : " + roomId));
        inventoryService.deleteFutureInventories(room);
        roomRepository.deleteById(roomId);
        log.info("Room is deleted successfully with id {}", roomId);
    }

}
