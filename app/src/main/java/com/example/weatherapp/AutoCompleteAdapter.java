package com.example.weatherapp;

import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;

//adapter for autocomplete feature
public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<String> suggestions;
    Context context;
    int resource;

    public AutoCompleteAdapter(Context context,int resource, ArrayList<String> suggestions)
    {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.suggestions = suggestions;
    }

    @Override
    public int getCount()
    {
        return suggestions.size();
    }

    @Override
    public String getItem(int pos)
    {
        return suggestions.get(pos);
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults filterResults = new FilterResults();
                if (constraint != null)
                {
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results )
            {
                if (suggestions != null && suggestions.size() > 0)
                {
                    notifyDataSetChanged();
                }
                else
                {
                    notifyDataSetInvalidated();
                }
            }

        };
        return filter;
    }


}
