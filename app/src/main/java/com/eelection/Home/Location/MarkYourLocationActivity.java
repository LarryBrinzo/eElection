package com.eelection.Home.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.eelection.Home.BottomNaviagtionDrawer;
import com.eelection.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MarkYourLocationActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    int REQUEST_CHECK_SETTINGS=1;
    private Location mLocation=null;
    ImageView marker;
    View mapView;
    Button save;
    TextView address,nick;
    String landmark=null,comp_address,nicname,lat,lng,addname,addid;
    LinearLayout back;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_your_location);

        marker=findViewById(R.id.marker);
        save=findViewById(R.id.save);
        nick=findViewById(R.id.nick);
        address=findViewById(R.id.address);
        back=findViewById(R.id.backbt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("landmark"))
                landmark=(bundle.getString("landmark"));

            comp_address=(bundle.getString("complete_address"));
            nicname=(bundle.getString("nickname"));

            addid=bundle.getString("addid");
            addname=bundle.getString("addname");

            nick.setText(nicname);
            address.setText(comp_address+", "+bundle.getString("addname"));

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(MarkYourLocationActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        checkLocation();

        if (ActivityCompat.checkSelfPermission(MarkYourLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MarkYourLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        else {
            startLocationUpdates();

            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLocation == null) {
                startLocationUpdates();
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                int serachnumber=pref.getInt("saved_address", 0)+1;

                String srchitm="comp_address"+(serachnumber);
                searchhint.putString(srchitm,comp_address);

                srchitm="nickname"+(serachnumber);
                searchhint.putString(srchitm,nicname);

                srchitm="landmark"+(serachnumber);
                if(landmark!=null)
                    searchhint.putString(srchitm,landmark);

                srchitm="latitude"+(serachnumber);
                searchhint.putString(srchitm,lat);

                srchitm="addname"+(serachnumber);
                searchhint.putString(srchitm,addname);

                srchitm="longitude"+(serachnumber);
                searchhint.putString(srchitm,lng);

                srchitm="addid"+(serachnumber);
                searchhint.putString(srchitm,addid);

                searchhint.putInt("saved_address",serachnumber);
                searchhint.apply();

                String srchimg="useraddress";
                searchhint.putString(srchimg,addname);

                Intent intent = new Intent(MarkYourLocationActivity.this, BottomNaviagtionDrawer.class);
                startActivity(intent);
                finishAffinity();

            }
        });
    }

    protected void startLocationUpdates() {
        // Create the location request
        long UPDATE_INTERVAL = 2 * 1000;
        long FASTEST_INTERVAL = 2000;
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(!mGoogleApiClient.isConnected())
            return;

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }

        enableMyLocationIfPermitted();

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Double l1=cameraPosition.target.latitude;
                lat=l1.toString();

                Double l2=cameraPosition.target.longitude;
                lng=l2.toString();
            }
        });

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if(mLocation!=null){
                    float distanceInMeters = mLocation.distanceTo(location);
                    boolean isWithin5m = distanceInMeters < 500;

                    if(isWithin5m)
                        return;
                }

                mLocation=location;
                marker.setVisibility(View.VISIBLE);

                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());

                CameraUpdate center=
                        CameraUpdateFactory.newLatLngZoom(coordinate,18);

                mMap.animateCamera(center);
            }
        });

        mGoogleApiClient.connect();
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    if(mLocation!=null && location==mLocation)
                        return;

                    mLocation=location;
                    marker.setVisibility(View.VISIBLE);

                    LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraUpdate center=
                            CameraUpdateFactory.newLatLngZoom(coordinate,18);

                    mMap.animateCamera(center);
                }
            };

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            if(mLocation!=null && location==mLocation)
                return;

            mLocation=location;
            marker.setVisibility(View.VISIBLE);

            LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate center=
                    CameraUpdateFactory.newLatLngZoom(coordinate,18);

            mMap.animateCamera(center);

        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            if(mLocation!=null && mLastLocation==mLocation)
                return;

            mLocation=mLastLocation;
            marker.setVisibility(View.VISIBLE);

            LatLng coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            CameraUpdate center=
                    CameraUpdateFactory.newLatLngZoom(coordinate,18);

            mMap.animateCamera(center);
        }

        else{

            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(28.7041,
                            77.1025));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(18);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            marker.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableMyLocationIfPermitted();
    }

    private boolean checkLocation() {
        if(!isLocationEnabled()){
            showAlert();
            enableMyLocationIfPermitted();
        }

        return isLocationEnabled();
    }

    private void showAlert() {

        LocationRequest locationRequest = LocationRequest.create();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MarkYourLocationActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }

        });
        enableMyLocationIfPermitted();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}