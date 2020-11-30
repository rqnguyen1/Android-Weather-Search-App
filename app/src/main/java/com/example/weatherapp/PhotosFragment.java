package com.example.weatherapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
//photos tab fragment for details activity
public class PhotosFragment extends Fragment {

    private final String TAG = "PHOTOS_FRAGMENT";

    public PhotosFragment() {

    }

    //creates the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        renderView(view);

        return view;
    }

    //renders the view
    private void renderView(View view)
    {
        DetailsActivity detailsActivity = (DetailsActivity) getActivity();
        String json = detailsActivity.getJson();
        try
        {

            JSONObject jsonObject = new JSONObject(json);
            JSONObject customSearch = jsonObject.getJSONObject("customSearch");
            JSONArray items = customSearch.getJSONArray("items");
            RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
            rv.setHasFixedSize(true);
            rv.setItemViewCacheSize(20);
            rv.setDrawingCacheEnabled(true);
            rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(llm);

            List<CityPhoto> photos = new ArrayList<>();


            for (int i = 0; i < items.length(); ++i)
            {
                JSONObject item = items.getJSONObject(i);
                String link = item.getString("link");

                photos.add(new CityPhoto(link));

            }
            RecycleViewAdapter adapter = new RecycleViewAdapter(photos);
            rv.setAdapter(adapter);
        }

        catch (Exception e)
        {
            Log.i(TAG, "Error creating view");
        }
    }
}
