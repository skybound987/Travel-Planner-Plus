package com.example.travelplannerplus.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.travelplannerplus.dao.DestinationActivityInterface;
import com.example.travelplannerplus.dao.DestinationInterface;
import com.example.travelplannerplus.entities.Destination;
import com.example.travelplannerplus.entities.DestinationActivity;

@Database(entities = {Destination.class, DestinationActivity.class}, version = 0, exportSchema = false)
public abstract class DestinationDatabase extends RoomDatabase {
    public abstract DestinationInterface destinationInterface();
    public abstract DestinationActivityInterface destinationActivityInterface();
    private static volatile DestinationDatabase INSTANCE;

    public static DestinationDatabase getDatabase(final Context c) {
        if (INSTANCE == null) {
            synchronized (DestinationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(c.getApplicationContext(), DestinationDatabase.class, "DestinationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        } return INSTANCE;
    }
}

