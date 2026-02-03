package com.example.travelplannerplus.repository;

import android.app.Application;

import com.example.travelplannerplus.dao.DestinationInterface;
import com.example.travelplannerplus.database.DestinationDatabase;
import com.example.travelplannerplus.entities.Destination;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DestinationRepository {
    private final DestinationInterface destinationInterface;
    private final DestinationActivityInterface destinationActivityInterface;
    private List<Destination> allDestinations;
    private List<DestinationActivity> allDestinationActivity;
    private int destinationActivityCount;
    private Destination destination;
    private DestinationActivity destinationActivity;
    private static final int THREADS = 4;
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(THREADS);

    public DestinationRepository(Application app) {
        DestinationDatabase destinationDatabase = DestinationDatabase.getDatabase(app);
        destinationInterface = destinationDatabase.destinationInterface();
        destinationActivityInterface = destinationDatabase.destinationActivityInterface();
    }

    // Count and Exclude Activities
    public int countDestinationActivities(int destinationId) {
        databaseExecutor.execute(()->{ destinationActivityCount = destinationActivityInterface.countDestinationActivity(destinationId); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destinationActivityCount;
    }


    //  Get Destinations/Activity
    public List<Destination>getAllDestinations() {
        databaseExecutor.execute(()->{ allDestinations = destinationInterface.getAllDestinations(); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allDestinations;
    }

    public Destination getDestinationById(int destinationId) {  // for deletion purposes
        databaseExecutor.execute(()->{ destination = destinationInterface.getDestinationById(destinationId); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destination;
    }

    public DestinationActivity getDestinationActivityById(int destinationActivityId) {  // for deletion purposes
        databaseExecutor.execute(()->{ destinationActivity = destinationActivityInterface.getDestinationActivityById(destinationActivityId); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destinationActivity;
    }

    public List<DestinationActivity>getActivityForDestinations(int destinationActivityId) {
        databaseExecutor.execute(()->{ allDestinationActivity = destinationActivityInterface.getAllDestinationActivity(destinationActivityId); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allDestinationActivity;
    }

    public List<DestinationActivity> shareDestinationActivity(int destinationActivityId) {
        databaseExecutor.execute(()->{ allDestinationActivity = destinationActivityInterface.shareDestinationActivity(destinationActivityId); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allDestinationActivity;
    }

    //  CRUD:  Create/Insert
    public void insert(Destination destination) {
        databaseExecutor.execute(()->{ destinationInterface.insert(destination); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void insert(DestinationActivity destinationActivity) {
        databaseExecutor.execute(()->{ destinationActivityInterface.insert(destinationActivity); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // CRUD: Update
    public void update(Destination destination) {
        databaseExecutor.execute(()->{ destinationInterface.update(destination); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(DestinationActivity destinationActivity) {
        databaseExecutor.execute(()->{ destinationActivityInterface.update(destinationActivity); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // CRUD: Delete
    public void delete(Destination destination) {
        databaseExecutor.execute(()->{ destinationInterface.delete(destination); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(DestinationActivity destinationActivity) {
        databaseExecutor.execute(()->{ destinationActivityInterface.delete(destinationActivity); });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

