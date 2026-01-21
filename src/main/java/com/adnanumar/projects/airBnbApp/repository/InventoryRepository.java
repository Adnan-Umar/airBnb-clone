package com.adnanumar.projects.airBnbApp.repository;

import com.adnanumar.projects.airBnbApp.entity.Inventory;
import com.adnanumar.projects.airBnbApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByDateAfterAndRoom(LocalDate date, Room room);

}
