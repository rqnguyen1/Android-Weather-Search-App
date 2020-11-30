package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import android.view.Menu;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import android.widget.AdapterView;
import android.content.Context;


//main activity when app is launched
public class MainActivity extends AppCompatActivity {

    private String city;
    private String state;
    private String country;
    private PlansPagerAdapter adapter;
    private ViewPager viewPager;
    private JSONObject currLocJson;
    private final String TAG = "MAIN_ACTIVITY";

    private void ipApiRequest(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    System.out.println(response);
                    city = response.getString("city");
                    state = response.getString("region");
                    country = response.getString("countryCode");
                    weatherRequest(response.getString("lat"), response.getString("lon"));

                }
                catch (Exception e)
                {
                    Log.i(TAG, "IP Api request error");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i(TAG, "IP Api request error");
            }
        });

        queue.add(jsonObjectRequest);
    }

    //fetch real-time weather data
    private void weatherRequest(String lat, String lon)
    {
        String url = "https://android-nodejs.appspot.com/weather?street=&city=&state=&lat=" + lat + "&lng=" +lon;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            System.out.println(response);
                            currLocJson = response;
                            currLocJson.put("city", city);
                            currLocJson.put("state", state);
                            currLocJson.put("country", country);


                            SharedPreferences sharedPrefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            String favoritesString = sharedPrefs.getString("favorites", null);
                            Gson gson = new Gson();
                            ArrayList<String> favorites = new ArrayList<>();

                            if ( favoritesString != null )
                            {
                                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                                favorites = gson.fromJson(favoritesString, type);
                            }


                            TabLayout tab = findViewById(R.id.tabs);
                            viewPager = findViewById(R.id.viewPager);

                            //add main tab
                            tab.addTab(tab.newTab().setText(""));

                            System.out.println("SIZE" + favorites.size());
                            for (int i = 0; i < favorites.size(); ++i)
                            {
                                tab.addTab(tab.newTab().setText(""));
                            }

                            adapter = new PlansPagerAdapter
                                    (getSupportFragmentManager(), tab.getTabCount(),favorites, response, tab);
                            viewPager.setAdapter(adapter);
                            viewPager.setOffscreenPageLimit(10);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                            ConstraintLayout progress = findViewById(R.id.progress);
                            progress.setVisibility(View.GONE);


                        }
                        catch (Exception e)
                        {
                            Log.i(TAG, "onResponse: ERROR");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i(TAG, "onResponse: ERROR");
            }
        });

        queue.add(jsonObjectRequest);

    }

    //fetch autocomplete data
    public void autoCompleteRequest(String city, final SearchView searchView, final Context context)
    {
        String url = "https://android-nodejs.appspot.com/autoComplete?city=" + city;

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Log.i(TAG, "autoCompleteResponse: " + response);
                            ArrayList<String> suggestions = new ArrayList<>();
                            JSONArray predictions = response.getJSONArray("predictions");
                            for (int i = 0; i < predictions.length(); ++i)
                            {
                                JSONObject prediction = predictions.getJSONObject(i);
                                JSONObject structured_formatting = prediction.getJSONObject("structured_formatting");
                                String suggestion = structured_formatting.getString("main_text");
                                suggestion += ", ";
                                suggestion += structured_formatting.getString("secondary_text");
                                suggestions.add(suggestion);
                            }


                            final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(com.google.android.material.R.id.search_src_text);
                            searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
                            searchAutoComplete.setAdapter(new AutoCompleteAdapter(context, android.R.layout.simple_list_item_1, suggestions));

                            searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id)
                                {
                                    String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                                    searchAutoComplete.setText("" + queryString);
                                }
                            });


                        }
                        catch (Exception e)
                        {
                            Log.i(TAG, "Autocomplete error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i(TAG, "Autocomplete error");
            }
        });

        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        ipApiRequest("http://ip-api.com/json");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.ic_search, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextChange (String newText)
                    {
                        autoCompleteRequest(newText, searchView, getApplicationContext() );
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        String[] array = query.split(",");
                        state = "";
                        if (array.length == 2)
                        {
                            city = array[0].trim();
                            country = array[1].trim();
                        }

                        else if (array.length == 3)
                        {
                            city = array[0].trim();
                            state = array[1].trim();
                            country = array[2].trim();
                        }
                        Log.i(TAG, "onQueryTextSubmit: " + query);
                        Intent intent = new Intent(MainActivity.this, SearchableActivity.class);
                        intent.putExtra("city", city);
                        intent.putExtra("state", state);
                        intent.putExtra("country", country);
                        startActivity(intent);
                        finish();
                        return false;
                    }
                }
        );

        return true;
    }


    public PlansPagerAdapter getPagerAdapter()
    {
        return adapter;
    }

    public ViewPager getViewPager()
    {
        return viewPager;
    }




}
