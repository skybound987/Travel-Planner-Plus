package com.example.travelplannerplus.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerplus.R;
import com.example.travelplannerplus.entities.Destination;
import com.example.travelplannerplus.repository.DestinationRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DestinationList extends AppCompatActivity {
    private DestinationRepository destinationRepository;
    private DestinationAdapter destinationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ui_destination_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        destinationRepository = new DestinationRepository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.destinationRecyclerView);
        destinationAdapter = new DestinationAdapter(destination -> {
            Intent intent = new Intent(DestinationList.this, DestinationDetails.class);
            intent.putExtra("destinationId", destination.getDestinationId());
            startActivity(intent);
        });
        recyclerView.setAdapter(destinationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.destinationFAB);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(DestinationList.this, DestinationDetails.class);
            intent.putExtra("destinationId", -1);
            startActivity(intent);
        });
        loadDestinations();
    }

    private void loadDestinations() {
        List<Destination> destinations = destinationRepository.getAllDestinations();
        destinationAdapter.setDestinations(destinations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDestinations();
    }
}