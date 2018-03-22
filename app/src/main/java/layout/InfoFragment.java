package layout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mouzourides.navi_map.DatabaseHelper;
import com.mouzourides.navi_map.DownloadImageTask;
import com.mouzourides.navi_map.MainActivity;
import com.mouzourides.navi_map.R;

import java.io.InputStream;
// Info Fragment, information on a location if user selects a marker afer searching
public class InfoFragment extends Fragment {
    private TextView textViewLocationName;
    private ImageView photo;
    private RatingBar ratingBar;
    private TextView textViewDescription;
    private TextView textViewOpenNow;
    private String locationName;
    private String locationSnippet;
    private String strDesc;
    private String photoKey;
    private float rating;
    private String openNow;
    private float lat;
    private float lng;
    private DatabaseHelper db;
    private MapFragment map;

    public InfoFragment() {
        // Required empty public constructor
    }

    // gets name, snippet and lat long from marker, gets database and map fragment from main activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            locationName = bundle.getString("locationName", "");
            locationSnippet = bundle.getString("locationSnippet", "");
            lat = bundle.getFloat("locationLat", 0);
            lng = bundle.getFloat("locationLng", 0);

        }
        db = ((MainActivity)getActivity()).getDB();
        map = ((MainActivity)getActivity()).getMapFragment();
        openNow = "Unknown";
    }

    // sets up view and components
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        String[] parts = locationSnippet.split("#");
        String strRating = parts[0];
        strDesc = parts[1];
        String strOpenNow = parts[2];
        String photoref = parts[3];
        //Toast.makeText(getContext(), strOpenNow, Toast.LENGTH_LONG).show();

        textViewLocationName = (TextView)  view.findViewById(R.id.textViewLocationName);
        textViewLocationName.setText(locationName);
        photo = (ImageView)  view.findViewById(R.id.image);

        textViewOpenNow = (TextView)  view.findViewById(R.id.textViewOpenNow);
        if(strOpenNow.equals("true")){
            openNow = "Currently Open";

        }else{
            openNow = "Not Currently Open";
        }
        textViewOpenNow.setText(openNow);
        String placesKey = this.getResources().getString(R.string.places_key);
        photoKey = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoref + "&key=" + placesKey;
        new DownloadImageTask(photo)
                .execute(photoKey);

        ratingBar = (RatingBar)  view.findViewById(R.id.ratingBarRating);
        rating = Float.valueOf(parts[0]);
        ratingBar.setRating(rating);
        textViewDescription = (TextView)  view.findViewById(R.id.textViewDescription);
        textViewDescription.setText(parts[1]);

        return view;
    }

    // button handler for favourite location button
    public void submitClickMethod(View v) {
        switch(v.getId()) {
            case R.id.buttonFavourite:
                db.insertData(locationName, "No notes entered", strDesc, photoKey, 0, rating, openNow, lat, lng);
                Toast.makeText(getContext(), "Favourited Location", Toast.LENGTH_LONG).show();
                break;
        }
    }

    // button handler for find on map
    public void submitFindOnMapClick(View v){
        switch(v.getId()) {
            case R.id.buttonShowMap:
                //Toast.makeText(getContext(), "Show on Map", Toast.LENGTH_LONG).show();
                map.drawPoly(lat,lng,locationName);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(this);
                transaction.commit();
                break;
        }
    }
}

