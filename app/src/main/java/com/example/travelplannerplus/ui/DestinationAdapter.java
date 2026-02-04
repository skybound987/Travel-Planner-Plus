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
    private List<Destination> destinations = new ArrayList<>();
    public interface OnDestinationClickListener {
        void onDestinationClick(Destination destination);
    }
    public DestinationAdapter(OnDestinationClickListener listener) {
        this.onDestinationClickListener = listener;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
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
        Destination destination = destinations.get(position);
        holder.title.setText(destination.getDestinationTitle());
        holder.itemView.setOnClickListener(v -> onDestinationClickListener.onDestinationClick(destination));
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.destinationTitleText);
        }
    }
}
