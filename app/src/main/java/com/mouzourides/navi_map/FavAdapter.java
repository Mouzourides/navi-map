package com.mouzourides.navi_map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mouzourides on 16/03/2017.
 */
// Custom adapter that uses allows use of a custom favourites layout for listview
public class FavAdapter extends ArrayAdapter<Favourites> {
    private List<Favourites> items;

    public FavAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FavAdapter(Context context, int resource, List<Favourites> items) {
        super(context, resource, items);
        this.items = items;
    }

    // creates the view setting values to components
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.favourites_layout, null);
        }

        Favourites p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id);
            ImageView tt2 = (ImageView) v.findViewById(R.id.imageView7);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
            }

            if (tt3 != null) {
                tt3.setText(p.getNotes());
            }
        }

        return v;
    }

    // removes item at a set position from list
    public void remove(int position){
        items.remove(items.get(position));
    }

    // gets item at position
    @Override
    public Favourites getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

}