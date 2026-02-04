package com.example.travelplannerplus.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DESTINATION_ACTIVITY")
public class DestinationActivity {
    @PrimaryKey(autoGenerate = true)
    private int destinationActivityId;
    @NonNull
    private String destinationActivityTitle;
    private int destinationId;  // Foreign Key
    private long destinationActivityDate;


    public DestinationActivity(int destinationActivityId, int destinationId, @NonNull String destinationActivityTitle, long destinationActivityDate) {
        this.destinationActivityId = destinationActivityId;
        this.destinationId = destinationId;
        this.destinationActivityTitle = destinationActivityTitle;
        this.destinationActivityDate = destinationActivityDate;
    }

    //  Getters
    public int getDestinationId() { return destinationId; }
    public int getDestinationActivityId() { return destinationActivityId; }
    @NonNull
    public String getDestinationActivityTitle() { return destinationActivityTitle; }
    public long getDestinationActivityDate() { return destinationActivityDate; }

    //  Setters
    public void setDestinationId(int destinationId) { this.destinationId = destinationId; }
    public void setDestinationActivityId(int destinationActivityId) { this.destinationActivityId = destinationActivityId; }
    public void setDestinationActivityTitle(@NonNull String destinationActivityTitle) { this.destinationActivityTitle = destinationActivityTitle; }
    public void setDestinationActivityDate(long destinationActivityDate) { this.destinationActivityDate = destinationActivityDate; }
}
