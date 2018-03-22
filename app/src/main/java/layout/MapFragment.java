package layout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.clustering.ClusterManager;
import com.mouzourides.navi_map.DatabaseHelper;
import com.mouzourides.navi_map.LocationAssistant;
import com.mouzourides.navi_map.MainActivity;
import com.mouzourides.navi_map.MyItem;
import com.mouzourides.navi_map.R;
import com.mouzourides.navi_map.model.GooglePlace;
import com.mouzourides.navi_map.model.GooglePlaceList;
import com.mouzourides.navi_map.model.GooglePlacesUtility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;
// MapFragment, sets up Google Map and handles all Map related features
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter, LocationAssistant.Listener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener {
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private GoogleMap mMap;
    private double myLat;
    private double myLong;
    private String placesKey;
    private LocationAssistant assistant;
    private GooglePlaceList nearby;
    private InfoFragment infoFragment;
    private ClusterManager<MyItem> mClusterManager;
    private List<Circle> circleList;

    public MapFragment() {
        // Required empty public constructor
    }

    // sets up location assistant which returns users current location, initialises circle list
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assistant = new LocationAssistant(getContext(), this, LocationAssistant.Accuracy.HIGH, 5000, false);
        assistant.setVerbose(true);
        assistant.start();
        circleList = new ArrayList<Circle>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        this.getMapAsync(this);
        return v;
    }

    // sets up basic empty map and checks user location permissions
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(this);


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
        }
    }

    // request location permission from user if location declined
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                            assistant.onPermissionsUpdated(requestCode, grantResults);
                    }
                }else {
                    Toast.makeText(getContext(), "Navi-Map requires location", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // draws polyline from user location to given location
    public void drawPoly(float lat, float lng, String name){
        clearMap();
        List<LatLng> list = new ArrayList();
        list.add(new LatLng(myLat,myLong));
        list.add(new LatLng(lat, lng));
        mMap.addPolyline(new PolylineOptions().addAll(list));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat,myLong), 15));
    }

    // launches google places request passed to it
    public void launchPlacesRequest(String request, float rating){
        PlacesReadFeed detailTask = new PlacesReadFeed(rating);
        detailTask.execute(new String[]{request});
    }

    // returns users lat
    public double getLat(){
        return myLat;
    }

    // returns users long
    public double getLng(){
        return myLong;
    }

    // replaces current fragment with fragment passed to it
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // sets info fragment to the info fragment passed to it
    private void setInfoFragment(InfoFragment infoFragment){
        this.infoFragment = infoFragment;
    }

    // returns info fragment
    public InfoFragment getInfoFragment(){
        return infoFragment;
    }

    // when an info window is selected, info screen is created allowing the user to view more
    // details on location and appropriate info is sent to the info screen via bundles
    @Override
    public void onInfoWindowClick(Marker marker) {
        //Toast.makeText(getContext(), marker.getSnippet(), Toast.LENGTH_LONG).show();
        InfoFragment fragment = new InfoFragment();
        replaceFragment(fragment);
        setInfoFragment(fragment);
        Bundle bundle = new Bundle();
        bundle.putString("locationName", marker.getTitle());
        bundle.putString("locationSnippet", marker.getSnippet());
        LatLng position = marker.getPosition();
        float lat = (float) position.latitude;
        float lng = (float) position.longitude;
        bundle.putFloat("locationLat", lat);
        bundle.putFloat("locationLng", lng);
        fragment.setArguments(bundle);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return prepareInfoView(marker);
    }

    // sets up info window with only appropriate info
    private View prepareInfoView(Marker marker){
        //prepare InfoView programmatically
        LinearLayout infoView = new LinearLayout(getContext());
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        TextView title = new TextView(getContext());
        title.setTextColor(Color.BLACK);
        title.setText(""+ marker.getTitle());
        infoView.addView(title);

        return infoView;
    }

    // handles data recieved from google places request and adds markers to the map in a cluster
    protected void reportBack(GooglePlaceList nearby, float rating) {
        if (this.nearby == null) {
            this.nearby = nearby;

        }

        List placeList = nearby.getResults();
        List item = new ArrayList<MyItem>();
        mClusterManager = new ClusterManager<MyItem>(getContext(), mMap);
           for (int i = 0; i < placeList.size(); i++) {
               GooglePlace place = (GooglePlace) placeList.get(i);
               double lat = place.getGeometry().getLocation().getLat();
               double lng = place.getGeometry().getLocation().getLng();
               String title = place.getName();
               boolean open = false;
               String photoref = "CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU";
               try{
                   open = place.getOpeningHours().getOpenNow();
                   photoref = place.getPhotos().getPhotoReference();
               }catch(Exception e){
                   Log.i("PLACES_EXAMPLE", "Data not found");
               }
               String snippet = place.getRating() + "#" + place.getTypes().get(0) + " located at " + place.getVicinity() + "#" + open + "#" + photoref;

               if(rating >= place.getRating()){
               item.add(new MyItem(lat, lng, title, snippet));}
            }

            mClusterManager.addItems(item);
            //mMap.setOnCameraIdleListener(mClusterManager);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat,myLong), 15));

            mMap.setOnCameraIdleListener(this);
    }

    // adds circle to the map
    private void addCircleToMap(float lat, float lng, double radius) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(radius)
                .strokeColor(Color.RED);
        Circle circle = mMap.addCircle(circleOptions);
        circleList.add(circle);

    }

    // When camera is still, removes all circles, checks to see if any markers are off screen and then
    // makes the appropriate halos via the addCirclesToMap method
    private void fetchData(){
        try{
            for (Circle myCircle : circleList) {
                myCircle.remove();
            }
            circleList.clear();
        LatLngBounds currentScreen = mMap.getProjection().getVisibleRegion().latLngBounds;
            MarkerManager.Collection userCollection = mClusterManager.getMarkerCollection();
            java.util.Collection<Marker> userCollectionMarkers = userCollection.getMarkers();
            ArrayList<Marker> mMarkers = new ArrayList<Marker>(userCollectionMarkers);
        for(Marker marker : mMarkers) {
            if(currentScreen.contains(marker.getPosition())) {
                //Toast.makeText(getContext(), "some markers are visible", Toast.LENGTH_SHORT).show();
            } else {

                float lat = (float) marker.getPosition().latitude;
                float lng = (float) marker.getPosition().longitude;
                double radius = computeDistanceBetween(currentScreen.getCenter(), marker.getPosition());
                addCircleToMap(lat, lng, radius);
                //Toast.makeText(getContext(), "some markers are not visible" + lat + "," + lng , Toast.LENGTH_SHORT).show();
               // marker outside visible region
            }
       }}catch(Exception e){
            Toast.makeText(getContext(), "No markers", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNeedLocationPermission() {

    }

    @Override
    public void onExplainLocationPermission() {

    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNeedLocationSettingsChange() {

    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    // sets user current lat and long to fields myLat, myLong
    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        myLong = location.getLongitude();
        myLat = location.getLatitude();
        //Toast.makeText(getContext(), "Long: " + myLong + " Lat: " + myLat, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }

    // calls fetchData method when camera is idle and re-clusters markers
    @Override
    public void onCameraIdle() {
        //Toast.makeText(getContext(), "The camera has stopped moving. Fetch the data from the server!", Toast.LENGTH_SHORT).show();
        fetchData();
        try{
            mClusterManager.cluster();}
        catch(Exception e){
            System.err.print("Unable to cluster");
        }
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    // clears everything on map
    public void clearMap() {
        mMap.clear();
    }

    // PlacesReadFeed, handles google places request
    private class PlacesReadFeed extends AsyncTask<String, Void, GooglePlaceList> {
        private final ProgressDialog dialog = new ProgressDialog(getContext());
        private float rating;

        public  PlacesReadFeed(float rating){
            this.rating = rating;
        }

        // Uses gson to map google places data to java classes in model folder
        @Override
        protected GooglePlaceList doInBackground(String... urls) {
            try {
                String referer = null;
                //dialog.setMessage("Fetching Places Data");
                if (urls.length == 1) {
                    referer = null;
                } else {
                    referer = urls[1];
                }
                String input = GooglePlacesUtility.readGooglePlaces(urls[0], referer);
                Gson gson = new Gson();
                GooglePlaceList places = gson.fromJson(input, GooglePlaceList.class);
                Log.i("PLACES_EXAMPLE", "Number of places found is " + places.getResults().size());
                return places;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PLACES_EXAMPLE", e.getMessage());
                return null;
            }
        }

        // clears map and informs user of getting nearby places
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Getting nearby places...");
            this.dialog.show();
            clearMap();
        }

        // calls reportBack method with the places from google places api
        @Override
        protected void onPostExecute(GooglePlaceList places) {
            this.dialog.dismiss();
            reportBack(places, rating);
        }


    }
}
