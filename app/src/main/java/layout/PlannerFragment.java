package layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.mouzourides.navi_map.DatabaseHelper;
import com.mouzourides.navi_map.Favourites;
import com.mouzourides.navi_map.MainActivity;
import com.mouzourides.navi_map.PlannerAdapter;
import com.mouzourides.navi_map.R;

import java.util.ArrayList;
import java.util.List;
// PlannerFragment, sets up list view for planner and provides user option to generate new plan
public class PlannerFragment extends Fragment {
    private List<Favourites> favList;
    public DatabaseHelper db;
    private PlannerAdapter adapter;
    private View view;
    private Spinner dropdown;
    private ListView list;
    private int placesNum;

    public PlannerFragment() {
        // Required empty public constructor
    }

    // gets database from main activity and initialses places num
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = ((MainActivity)getActivity()).getDB();
        placesNum = 2;
    }

    // sets up view and components
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_planner, container, false);

        list = (ListView)view.findViewById(R.id.PlannerList);


        dropdown = (Spinner)view.findViewById(R.id.spinner1);
        String[] items = new String[]{"2", "3", "4", "5", "6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        populateList();
        return view;
    }

    // populates list view with data from the database
    public void populateList(){
        try{
            String[] data = db.getRandom(placesNum);
            favList = new ArrayList<Favourites>();
            for (int i = 0; i < data.length; i++) {
                String[] info = db.getDataFromName(data[i]);
                favList.add(new Favourites(data[i], info[0], info[1], "", "", ""));
            }
            adapter = new PlannerAdapter(getContext(), R.layout.planner_layout, favList);
            list.setAdapter(adapter);
        }
        catch(Exception e){
            System.err.println("No names in database");
        }
    }

    // generate plan button handler, calls populate list and gets amount of places from dropdown list
    public void submitClickMethod(View view) {
        List<Favourites> emptyList = new ArrayList<Favourites>();
        adapter = new PlannerAdapter(getContext(), R.layout.planner_layout, emptyList);
        list.setAdapter(adapter);
        String strDropdown = (String) dropdown.getSelectedItem();
        placesNum = Integer.parseInt(strDropdown );
        populateList();

    }
}
