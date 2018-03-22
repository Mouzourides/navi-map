package layout;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mouzourides.navi_map.LocationAssistant;
import com.mouzourides.navi_map.MainActivity;
import com.mouzourides.navi_map.R;

import java.util.Map;
// Explore Fragment, sets up components and contains handler for search button
public class ExploreFragment extends Fragment  {

    private CheckBox cbEat;
    private CheckBox cbArt;
    private CheckBox cbBars;
    private CheckBox cbLandmarks;
    private CheckBox cbShops;
    private CheckBox cbParks;
    private Button btnSubmit;
    private SeekBar slider;
    private RatingBar ratingBar;
    private String finalType;
    private int slider_value;
    private TextView txtDistance;
    private String placesRequest;
    private MapFragment map;

    private double myLong;
    private double myLat;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        map = ((MainActivity)getActivity()).getMapFragment();
    }

    // Sets up components and creates view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        cbEat = (CheckBox)  view.findViewById(R.id.checkBoxEat);
        cbArt = (CheckBox)  view.findViewById(R.id.checkBoxArt);
        cbBars = (CheckBox)  view.findViewById(R.id.checkBoxBars);
        cbLandmarks = (CheckBox)  view.findViewById(R.id.checkBoxLandmarks);
        cbShops = (CheckBox)  view.findViewById(R.id.checkBoxShops);
        cbParks = (CheckBox)  view.findViewById(R.id.checkBoxParks);
        btnSubmit = (Button) view.findViewById(R.id.buttonSubmit);
        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        ratingBar.setRating(5);
        slider = (SeekBar) view.findViewById(R.id.seekBarRadius);
        slider.setMax(1000);
        slider.setProgress(500);
        txtDistance =(TextView)view.findViewById(R.id.textViewDistance);
        txtDistance.setText("Distance : " + slider.getProgress() + "m / " + slider.getMax()+ "m");
        slider_value = 500;
        slider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        slider_value = progress;
                        txtDistance.setText("Distance: " + progress + "m / " + slider.getMax()+ "m");

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txtDistance.setText("Distance: " + slider_value + "m / " + slider.getMax() + "m");

                    }
                }
        );

        finalType = "";
        return view;
    }

    //checks components are selected and if so, sends a google places request
    public void submitClickMethod(View v) {
        switch(v.getId()) {
            case R.id.buttonSubmit:
                if(cbArt.isChecked() || cbEat.isChecked() || cbBars.isChecked() ||
                        cbLandmarks.isChecked() || cbShops.isChecked() || cbParks.isChecked() || ratingBar.getRating() != 0.0){
                    if(cbArt.isChecked()){
                        finalType = finalType + "|art_gallery|museum";
                    }
                    if(cbEat.isChecked()){
                        finalType = finalType + "|food|restaurant";
                    }
                    if(cbBars.isChecked()){
                        finalType = finalType + "|bar|night_club";
                    }
                    if(cbLandmarks.isChecked()){
                        finalType = finalType + "|point_of_interest";
                    }
                    if(cbShops.isChecked()){
                        finalType = finalType + "|shopping_mall|store";
                    }
                    if(cbParks.isChecked()){
                        finalType = finalType + "|park";
                    }
                    myLat = map.getLat();
                    myLong = map.getLng();
                    float rating = ratingBar.getRating();

                    String placesKey = this.getResources().getString(R.string.places_key);
                    String radius = "" + slider_value;
                    placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                            myLat + "," + myLong + "&radius=" + radius +"&type="+ finalType +"&key=" + placesKey;
                    //Toast.makeText(getContext(), placesRequest, Toast.LENGTH_LONG).show();

                    map.launchPlacesRequest(placesRequest, rating);

                    finalType = "";
                }else{
                    Toast.makeText(getContext(), "Please enter the required fields" , Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
