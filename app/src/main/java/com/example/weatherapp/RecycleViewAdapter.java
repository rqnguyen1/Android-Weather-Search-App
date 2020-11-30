package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//adapter for recycle view used in photos fragment
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.PhotosViewHolder>
{
    public static class PhotosViewHolder extends RecyclerView.ViewHolder {

        ImageView photoView;
        CardView cardView;

        PhotosViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView3);
            photoView = (ImageView) itemView.findViewById(R.id.photoView);
        }
    }

    List<CityPhoto> photos;

    RecycleViewAdapter(List<CityPhoto> photos)
    {
        this.photos = photos;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_photo_layout, viewGroup, false);
        PhotosViewHolder photosViewHolder = new PhotosViewHolder(v);
        return photosViewHolder;
    }


    @Override
    public void onBindViewHolder(PhotosViewHolder photosViewHolder, int i) {
        Context context = photosViewHolder.photoView.getContext();

        GlideApp.with(context)
                .load(photos.get(i).link)
                .into(photosViewHolder.photoView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
