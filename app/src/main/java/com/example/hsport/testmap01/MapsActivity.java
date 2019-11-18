package com.example.hsport.testmap01;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ListView lstPlaces;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;
    LocationManager locationManager;
    private static final String TAG = "MapsActivity";
    private final LatLng mDefaultLocation = new LatLng(21.412906, 39.823865);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REUEST_FINE_LOCATION = 1;
    private boolean mLocationPermission;

    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributtions;
    private String[] mLikelyPlaceLatLngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        MapFromYoutube();
//        MapFromCodelabs();
    }

//    public void MapFromCodelabs(){
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        lstPlaces = findViewById(R.id.listPlaces);
//
//        String apiKey = getString(R.string.google_maps_key);
//        Places.initialize(getApplicationContext(), apiKey);
//        placesClient = Places.createClient(this);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//    }
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_geolocate:
                MapFromYoutube();
                return true;

                default:
                return super.onOptionsItemSelected(item);
        }
    }
//
//
//
//    private void getLocationPermission(){
//        mLocationPermission = false;
//        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            mLocationPermission = true;
//        }else {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REUEST_FINE_LOCATION);
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        mLocationPermission = false;
//
//        switch(requestCode){
//            case PERMISSIONS_REUEST_FINE_LOCATION:{
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    mLocationPermission = true;
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//





    // from https://www.youtube.com/watch?v=_y6imr1NQmA
   public void MapFromYoutube(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;

        }




        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longtiude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longtiude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longtiude, 1);
                        String string = addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(string));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });

        }
    }

 

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;

            mMap.getUiSettings().setZoomControlsEnabled(true);

            // Prompt the user for permission.
//            getLocationPermission();
            // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }


//    private void getCurrentPlaceLikelihoods() {
//        // Use fields to define the data types to return.
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
//                Place.Field.LAT_LNG);
//
//        // Get the likely places - that is, the businesses and other points of interest that
//        // are the best match for the device's current location.
//        @SuppressWarnings("MissingPermission") final FindCurrentPlaceRequest request =
//                FindCurrentPlaceRequest.builder(placeFields).build();
//        Task<FindCurrentPlaceResponse> placeResponse = PlacesClient.findCurrentPlace(request);
//        placeResponse.addOnCompleteListener(this,
//                new OnCompleteListener<FindCurrentPlaceResponse>() {
//                    @RequiresApi(api = Build.VERSION_CODES.O)
//                    @Override
//                    public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
//                        if (task.isSuccessful()) {
//                            FindCurrentPlaceResponse response = task.getResult();
//                            // Set the count, handling cases where less than 5 entries are returned.
//                            int count;
//                            if (response.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
//                                count = response.getPlaceLikelihoods().size();
//                            } else {
//                                count = M_MAX_ENTRIES;
//                            }
//
//                            int i = 0;
//                            mLikelyPlaceNames = new String[count];
//                            mLikelyPlaceAddresses = new String[count];
//                            mLikelyPlaceAttributtions = new String[count];
//                            mLikelyPlaceLatLngs = new String[count];
//
//                            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                                Place currPlace = placeLikelihood.getPlace();
//                                mLikelyPlaceNames[i] = currPlace.getName();
//                                mLikelyPlaceAddresses[i] = currPlace.getAddress();
//                                mLikelyPlaceAttributtions[i] = (currPlace.getAttributions() == null) ?
//                                        null : String.join(" ", currPlace.getAttributions());
//                                mLikelyPlaceLatLngs[i] = String.valueOf(currPlace.getLatLng());
//
//                                String currLatLng = (mLikelyPlaceLatLngs[i] == null) ?
//                                        "" : mLikelyPlaceLatLngs[i].toString();
//
//                                Log.i(TAG, String.format("Place " + currPlace.getName()
//                                        + " has likelihood: " + placeLikelihood.getLikelihood()
//                                        + " at " + currLatLng));
//
//                                i++;
//                                if (i > (count - 1)) {
//                                    break;
//                                }
//                            }
//
//
//                            // COMMENTED OUT UNTIL WE DEFINE THE METHOD
//                            // Populate the ListView
//                            // fillPlacesList();
//                        } else {
//                            Exception exception = task.getException();
//                            if (exception instanceof ApiException) {
//                                ApiException apiException = (ApiException) exception;
//                                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (mLocationPermission) {
//                Task<Location> locationResult = FusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            lastLocation = task.getResult();
//                            Log.d(TAG, "Latitude: " + lastLocation.getLatitude());
//                            Log.d(TAG, "Longitude: " + lastLocation.getLongitude());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(lastLocation.getLatitude(),
//                                            lastLocation.getLongitude()), DEFAULT_ZOOM));
//                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                        }
//
//                        getCurrentPlaceLikelihoods();
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }
//
//    private void pickCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermission) {
//            getDeviceLocation();
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }
//
//    private AdapterView.OnItemClickListener listClickedHandler = new AdapterView.OnItemClickListener() {
//        public void onItemClick(AdapterView parent, View v, int position, long id) {
//            // position will give us the index of which place was selected in the array
//            LatLng markerLatLng = mLikelyPlaceLatLngs[position];
//            String markerSnippet = mLikelyPlaceAddresses[position];
//            if (mLikelyPlaceAttributtions[position] != null) {
//                markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributtions[position];
//            }
//
//            // Add a marker for the selected place, with an info window
//            // showing information about that place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(mLikelyPlaceNames[position])
//                    .position(markerLatLng)
//                    .snippet(markerSnippet));
//
//            // Position the map's camera at the location of the marker.
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng));
//        }
//    };
    }

