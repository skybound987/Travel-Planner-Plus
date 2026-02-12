package com.example.travelplannerplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ui_destination_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView =
                (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        assert searchView != null;
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                destinationAdapter.searchFilter(searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                destinationAdapter.searchFilter(newText);
                return true;
            }
        });
        return true;
    }

    private void loadDestinations() {
        List<Destination> loadedDestinations = destinationRepository.getAllDestinations();
        if (loadedDestinations == null) {
            loadedDestinations = new java.util.ArrayList<>();
        }
        destinationAdapter.setDestinations(loadedDestinations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDestinations();
    }
}