package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mouzourides.navi_map.DatabaseHelper;
import com.mouzourides.navi_map.DownloadImageTask;
import com.mouzourides.navi_map.Favourites;
import com.mouzourides.navi_map.FavAdapter;
import com.mouzourides.navi_map.MainActivity;
import com.mouzourides.navi_map.R;

import java.util.List;

/**
 * Created by Mouzourides on 16/03/2017.
 */
// FavListFragment, fragment that contains information on a location in their favourites
public class FavListFragment extends Fragment {
    private List<Favourites> favList;
    private String locationName;
    private String locationImg;
    private String locationDesc;
    private String locationRating;
    private String locationOpenNow;
    private String locationNotes;
    private int position;
    private EditText editTextNotes;
    private PostcardFragment postcardFragment;

    private DatabaseHelper db;
    private FavFragment favFragment;
    private FavAdapter adapter;
    private MapFragment mapFragment;

    public FavListFragment() {
        // Required empty public constructor
    }

    // gest bundles sent from fav fragment and gets database, map fragment, fav fragment and adapter
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            locationName = bundle.getString("locationName", "");
            locationImg = bundle.getString("locationImage", "");
            locationDesc = bundle.getString("locationDesc", "");
            locationRating = bundle.getString("locationRating", "");
            locationOpenNow = bundle.getString("locationOpenNow", "");
            locationNotes = bundle.getString("locationNotes", "");
            position = bundle.getInt("position");
        }
        db = ((MainActivity)getActivity()).getDB();
        mapFragment = ((MainActivity)getActivity()).getMapFragment();
        favFragment = ((MainActivity)getActivity()).getFavFragment();
        adapter = favFragment.getAdapter();
    }
    // creates view and sets up components
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_fav, container, false);
        TextView textViewLocationName = (TextView)  view.findViewById(R.id.textViewLocationName2);
        textViewLocationName.setText(locationName);
        ImageView photo = (ImageView)  view.findViewById(R.id.image2);
        new DownloadImageTask(photo)
                .execute(locationImg);
        TextView textViewDesc = (TextView)  view.findViewById(R.id.textViewDescription2);
        textViewDesc.setText(locationDesc);
        RatingBar ratingBar = (RatingBar)  view.findViewById(R.id.ratingBarRating2);
        float rating = Float.valueOf(locationRating);
        ratingBar.setRating(rating);
        TextView textViewOpenNow = (TextView)  view.findViewById(R.id.textViewOpenNow2);
        textViewOpenNow.setText(locationOpenNow);
        editTextNotes = (EditText)  view.findViewById(R.id.editText);
        editTextNotes.setText(locationNotes);
        Spinner dropdown = (Spinner)view.findViewById(R.id.spinnerVisited);
        String[] items = new String[]{"No", "Yes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        return view;
    }

    // button handler for unfavourite, save notes and postcard button
    public void submitClickMethod(View v) {
        switch(v.getId()) {
            case R.id.buttonUnfavourite:
                db.deleteRow(locationName);
                adapter.remove(position);
                adapter.notifyDataSetChanged();
                favFragment.resetAdapter(adapter);
                Toast.makeText(getContext(), "Location Unfavourited", Toast.LENGTH_LONG).show();

                break;
            case R.id.buttonSave:
                Toast.makeText(getContext(), "Notes Saved", Toast.LENGTH_LONG).show();
                String notes = editTextNotes.getText().toString();
                db.updateNotes(notes, locationName);
                adapter.notifyDataSetChanged();
                favFragment.resetAdapter(adapter);
                break;

            case R.id.buttonPostcard2:
                postcardFragment = new PostcardFragment();
                setPostcardFragment(postcardFragment);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, postcardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Bundle bundle = new Bundle();
                bundle.putString("locationName", locationName);
                bundle.putString("locationImg", locationImg);
                postcardFragment.setArguments(bundle);
                break;

        }
    }

    // button handler for show on map button
    public void submitFindOnMapClick(View v){
        switch(v.getId()) {
        case R.id.buttonShowMap2:
        String[] strLat = db.getLatFromName(locationName);
        String[] strLng = db.getLngFromName(locationName);
        float lat =  Float.valueOf(strLat[0]);
        float lng =  Float.valueOf(strLng[0]);
        mapFragment.drawPoly(lat,lng,locationName);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(this);
            transaction.commit();

        break;
        }
    }
    // returns postcard fragment
    public PostcardFragment getPostcardFragment() {
        return postcardFragment;
    }

    // sets postcard fragment
    public void setPostcardFragment(PostcardFragment postcardFragment) {
        this.postcardFragment = postcardFragment;
    }
}
