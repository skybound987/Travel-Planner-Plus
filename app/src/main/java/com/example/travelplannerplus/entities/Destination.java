package com.example.travelplannerplus.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DESTINATIONS")
public class Destination {
    @PrimaryKey(autoGenerate = true)
    private int destinationId;
    private long destinationStartDate;
    private long destinationEndDate;
    @NonNull
    private String destinationTitle;
    private String destinationLodging;

    public Destination(int destinationId, @NonNull String destinationTitle, String destinationLodging, long destinationStartDate, long destinationEndDate ) {
        this.destinationId = destinationId;
        this.destinationTitle = destinationTitle;
        this.destinationLodging = destinationLodging;
        this.destinationStartDate = destinationStartDate;
        this.destinationEndDate = destinationEndDate;
    }

    //  Getters
    public int getDestinationId() { return destinationId; }
    @NonNull
    public String getDestinationTitle() { return destinationTitle; }
    public String getDestinationLodging() { return destinationLodging; }
    public long getDestinationStartDate() { return destinationStartDate; }
    public long getDestinationEndDate() { return destinationEndDate; }

    //  Setters
    public void setDestinationId(int destinationId) { this.destinationId = destinationId; }
    public void setDestinationTitle(@NonNull String destinationTitle) { this.destinationTitle = destinationTitle; }
    public void setDestinationLodging(String destinationLodging) { this.destinationLodging = destinationLodging; }
    public void setDestinationStartDate(long destinationStartDate) { this.destinationStartDate = destinationStartDate; }
    public void setDestinationEndDate(long destinationEndDate) { this.destinationEndDate = destinationEndDate; }
}

