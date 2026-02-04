package com.example.travelplannerplus.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.travelplannerplus.entities.DestinationActivity;

import java.util.List;

@Dao
public interface DestinationActivityInterface {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DestinationActivity destinationActivity);

    @Update
    void update(DestinationActivity destinationActivity);

    @Delete
    void delete(DestinationActivity destinationActivity);

    @Query("SELECT * FROM DESTINATION_ACTIVITY WHERE destinationId = :destinationId ORDER BY destinationActivityId ASC")
    List<DestinationActivity> getAllDestinationActivity(int destinationId);

    @Query("SELECT * FROM DESTINATION_ACTIVITY WHERE destinationId = :destinationId ORDER BY destinationActivityDate")
    List<DestinationActivity> shareDestinationActivity(int destinationId);

    @Query("SELECT COUNT(*) FROM DESTINATION_ACTIVITY WHERE destinationId = :destinationId")
    int countDestinationActivity(int destinationId);

    @Query("SELECT * FROM DESTINATION_ACTIVITY WHERE destinationActivityId = :destinationActivityId")
    DestinationActivity getDestinationActivityById(int destinationActivityId);
}
