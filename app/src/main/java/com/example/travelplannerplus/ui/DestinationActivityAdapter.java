package com.example.travelplannerplus.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerplus.R;
import com.example.travelplannerplus.entities.DestinationActivity;

import java.util.ArrayList;
import java.util.List;

public class DestinationActivityAdapter extends RecyclerView.Adapter<DestinationActivityAdapter.DestinationActivityViewHolder> {

    private final OnDestinationActivityClickListener onDestinationActivityClickListener;
    private List<DestinationActivity> destinationActivity = new ArrayList<>();
    public interface OnDestinationActivityClickListener {
        void onDestinationActivityClick(DestinationActivity destinationActivity);
    }
    public DestinationActivityAdapter(DestinationActivityAdapter.OnDestinationActivityClickListener listener) {
        this.onDestinationActivityClickListener = listener;
    }
    public void setDestinationActivity(List<DestinationActivity> destinationActivity) {
        this.destinationActivity = destinationActivity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DestinationActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_activity_list_items, parent, false);
        return new DestinationActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationActivityViewHolder holder, int position) {
        DestinationActivity destinationActivity = this.destinationActivity.get(position);
        holder.title.setText(destinationActivity.getDestinationActivityTitle());
        holder.itemView.setOnClickListener(v -> OnDestinationActivityClickListener.onDestinationActivityClick(destinationActivity));
    }

    @Override
    public int getItemCount() {
        return destinationActivity.size();
    }

    static class DestinationActivityViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public DestinationActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.destinationActivityTitleText);
        }
    }
}
}
