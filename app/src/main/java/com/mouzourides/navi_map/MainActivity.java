package com.mouzourides.navi_map;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import layout.ExploreFragment;
import layout.FavFragment;
import layout.FavListFragment;
import layout.InfoFragment;
import layout.MapFragment;
import layout.PlannerFragment;
import layout.PostcardFragment;
// Main activity of application
public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private DatabaseHelper db;
    private MapFragment mapFragment;
    private FavFragment favFragment;

    //sets up viewpager, initialises database and tab layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = new MapFragment();
        favFragment = new FavFragment();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        db = new DatabaseHelper(getApplicationContext());

    }

    // sets up view pager with appropriate fragments
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mapFragment, "Map");
        adapter.addFragment(new ExploreFragment(), "Explore");
        adapter.addFragment(favFragment, "Favourites");
        adapter.addFragment(new PlannerFragment(), "Planner");
        viewPager.setAdapter(adapter);
    }

    // returns database
    public DatabaseHelper getDB(){
        return db;
    }

    // returns map fragment
    public MapFragment getMapFragment(){
        return mapFragment;
    }

    // returns fav fragment
    public FavFragment getFavFragment(){
        return favFragment;
    }

    // button handler for search button
    public void onSearchButtonClick(View view) {
        viewPager.setCurrentItem(0);

        ExploreFragment f2 = (ExploreFragment) adapter.getItem(1);
        f2.submitClickMethod(view);
    }

    // button handler for info button
    public void onInfoButtonClick(View view) {
        viewPager.setCurrentItem(0);
        MapFragment f1 = (MapFragment) adapter.getItem(0);
        InfoFragment f2 = f1.getInfoFragment();
        f2.submitClickMethod(view);
    }

    // button handler for map info button
    public void onMapInfoButtonClick(View view) {
        viewPager.setCurrentItem(0);
        MapFragment f1 = (MapFragment) adapter.getItem(0);
        InfoFragment f2 = f1.getInfoFragment();
        f2.submitFindOnMapClick(view);
    }

    // button handler for fav info button
    public void onFavInfoButtonClick(View view) {
        FavFragment f1 = (FavFragment) adapter.getItem(2);
        FavListFragment f2 = f1.getFavListFragment();
        f2.submitClickMethod(view);
    }

    // button handler for generate plan button
    public void onGenerateClick(View view) {
        PlannerFragment f1 = (PlannerFragment) adapter.getItem(3);
        f1.submitClickMethod(view);
    }

    //button handler for postcard button
    public void onFavPostcardButtonClick(View view) {
        FavFragment f1 = (FavFragment) adapter.getItem(2);
        FavListFragment f2 = f1.getFavListFragment();
        f2.submitClickMethod(view);
    }

    //button handler for upload photo button
    public void UploadPhotoButtonClick(View view) {
            FavFragment f3 = (FavFragment) adapter.getItem(2);
            FavListFragment f4 = f3.getFavListFragment();
            PostcardFragment p2 = f4.getPostcardFragment();
            p2.submitClickMethod(view);
    }

    // button handler for show on map button
    public void onShowOnMapButtonClick(View view) {
        viewPager.setCurrentItem(0);
        FavFragment f1 = (FavFragment) adapter.getItem(2);
        FavListFragment f2 = f1.getFavListFragment();
        f2.submitFindOnMapClick(view);
    }

    // View pager adapter class, cotains list of fragments and
    // provides method to add fragment, get fragment added to view pager
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
