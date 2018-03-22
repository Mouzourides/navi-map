package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mouzourides.navi_map.DatabaseHelper;
import com.mouzourides.navi_map.Favourites;
import com.mouzourides.navi_map.FavAdapter;
import com.mouzourides.navi_map.MainActivity;
import com.mouzourides.navi_map.R;

import java.util.ArrayList;
import java.util.List;
// Fav Fragment, shows user list of their favourites
public class FavFragment extends Fragment{

    private List<Favourites> favList;
    private DatabaseHelper db;
    private ListView list;
    private FavListFragment favListFragment;
    public FavAdapter adapter;
    public FavFragment() {
        // Required empty public constructor
    }

    // gets database from main activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = ((MainActivity)getActivity()).getDB();

    }
    // creates the view and calls populateList method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        list = (ListView)view.findViewById(R.id.FavouritesList);
        populateList();

        return view;
    }

    // populates list view with data from database and sets up on click for list view
    // sending the data to the new fragment via bundles and creating the favourite list fragment
    private void populateList(){
        try{
            String[] itemNames = db.getNames();
            String[] itemImg = db.getImage();
            String[] itemDesc = db.getDesc();
            String[] itemRating = db.getRating();
            String[] itemOpenNow = db.getOpenNow();
            String[] itemNotes = db.getNotes();
            favList = new ArrayList<Favourites>();
            for (int i = 0; i < itemNames.length; i++) {
                favList.add(new Favourites(itemNames[i], itemImg[i],itemDesc[i], itemRating[i], itemOpenNow[i], itemNotes[i]));
            }

            adapter = new FavAdapter(getContext(), R.layout.favourites_layout, favList);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Favourites selectedFav = (Favourites) list.getItemAtPosition(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("locationName", selectedFav.getName());
                    bundle.putString("locationImage", selectedFav.getImageID());
                    bundle.putString("locationDesc", selectedFav.getDesc());
                    bundle.putString("locationRating", selectedFav.getRating());
                    bundle.putString("locationOpenNow", selectedFav.getOpenNow());
                    bundle.putString("locationNotes", selectedFav.getNotes());
                    bundle.putInt("position", position);

                    FavListFragment fragment = new FavListFragment();
                    setFavListFragment(fragment);
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(android.R.id.content, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            });
        }
        catch(Exception e){
            System.err.println("No names in database");
        }
    }

    // sets fav list fragment
    private void setFavListFragment(FavListFragment favListFragment){
        this.favListFragment = favListFragment;
    }

    // returns current fav list fragment
    public FavListFragment getFavListFragment( ){
        return favListFragment;
    }

    // returns adapter
    public FavAdapter getAdapter(){
        return adapter;
    }

    // resets adapter to immedietly show changes in list view
    public void resetAdapter(FavAdapter adapter) {
        List<Favourites> emptyList = new ArrayList<Favourites>();
        adapter = new FavAdapter(getContext(), R.layout.favourites_layout, emptyList);
        list.setAdapter(adapter);
        list.invalidate();
        adapter.notifyDataSetChanged();
        populateList();
        adapter = new FavAdapter(getContext(), R.layout.favourites_layout, favList);
        list.setAdapter(adapter);
    }
}