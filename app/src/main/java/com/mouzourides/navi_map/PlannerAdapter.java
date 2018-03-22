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
 * Created by Nik Mouzourides on 17/03/2017.
 */
// Custom adapter that uses allows use of a custom favourites layout for listview
public class PlannerAdapter extends ArrayAdapter<Favourites> {
    private List<Favourites> items;

    public PlannerAdapter(Context context, int resource, List<Favourites> items) {
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
            v = vi.inflate(R.layout.planner_layout, null);
        }

        Favourites p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.textViewPlannerLocation);
            ImageView tt2 = (ImageView) v.findViewById(R.id.imageViewP);
            TextView tt3 = (TextView) v.findViewById(R.id.textViewDesc2);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                new DownloadImageTask(tt2)
                        .execute(p.getImageID());
            }

            if (tt3 != null) {
                tt3.setText(p.getDesc());
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