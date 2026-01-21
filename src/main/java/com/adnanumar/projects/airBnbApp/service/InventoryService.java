package com.adnanumar.projects.airBnbApp.service;

import com.adnanumar.projects.airBnbApp.entity.Room;

public interface InventoryService {

    void initializeRoomForAYear(Room Room);

    void  deleteFutureInventories(Room Room);

}
