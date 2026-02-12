package com.example.travelplannerplus.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerplus.R;
import com.example.travelplannerplus.entities.Destination;

import java.util.ArrayList;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private final OnDestinationClickListener onDestinationClickListener;
    private List<Destination> destinationList = new ArrayList<>();
    private List<Destination> masterDestinationList = new ArrayList<>();
    public interface OnDestinationClickListener {
        void onDestinationClick(Destination destination);
    }
    public DestinationAdapter(OnDestinationClickListener listener) {
        this.onDestinationClickListener = listener;
    }

    public void setDestinations(List<Destination> destinations) {
        if (destinations == null) {
            destinationList = new ArrayList<>();
            masterDestinationList = new ArrayList<>();
        } else {
            destinationList = new ArrayList<>(destinations);
            masterDestinationList = new ArrayList<>(destinations);
        }
        notifyDataSetChanged();
    }

    public void searchFilter(String searchQuery) {
        if (searchQuery == null) {
            searchQuery = "";
        }
        searchQuery = searchQuery.trim().toLowerCase();

        if (searchQuery.isEmpty()) {
            destinationList = new ArrayList<>(masterDestinationList);
        } else {
            List<Destination> filteredResult = new ArrayList<>();
            for (Destination d : masterDestinationList) {
                String title = d.getDestinationTitle();
                String lodging = d.getDestinationLodging();

                if (title != null && title.toLowerCase().contains(searchQuery)) {
                    filteredResult.add(d);
                } else if (lodging != null && lodging.toLowerCase().contains(searchQuery)) {
                    filteredResult.add(d);
                }
            }
            destinationList = filteredResult;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_destination_list_items, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.title.setText(destination.getDestinationTitle());
        holder.itemView.setOnClickListener(v -> onDestinationClickListener.onDestinationClick(destination));
    }

    @Override
    public int getItemCount() {
        if (destinationList == null) {
            return 0;
        }
        return destinationList.size();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.destinationTitleText);
        }
    }
}
