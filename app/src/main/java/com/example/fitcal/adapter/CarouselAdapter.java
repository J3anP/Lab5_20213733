package com.example.fitcal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitcal.R;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    private final String[] names;
    private final String[] calories;
    private final String[] descriptions;
    private final int[] images;

    public CarouselAdapter(String[] names, String[] calories, String[] descriptions, int[] images) {
        this.names = names;
        this.calories = calories;
        this.descriptions = descriptions;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(names[position]);
        holder.caloriesTextView.setText(calories[position]);
        holder.descriptionTextView.setText(descriptions[position]);
        holder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView caloriesTextView;
        TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            nameTextView = itemView.findViewById(R.id.name);
            caloriesTextView = itemView.findViewById(R.id.calories);
            descriptionTextView = itemView.findViewById(R.id.description);
        }
    }
}

