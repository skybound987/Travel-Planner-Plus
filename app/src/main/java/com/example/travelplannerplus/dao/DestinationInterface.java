package com.example.travelplannerplus.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.travelplannerplus.entities.Destination;

import java.util.List;

@Dao
public interface DestinationInterface {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Destination destination);

    @Update
    void update(Destination destination);

    @Delete
    void delete(Destination destination);

    @Query("SELECT * FROM DESTINATIONS ORDER BY destinationId ASC")
    List<Destination> getAllDestinations();

    @Query("SELECT * FROM DESTINATIONS WHERE destinationId = :destinationId")
    Destination getDestinationById(int destinationId);
}

