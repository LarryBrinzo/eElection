package com.quidish.anshgupta.login.Home.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;
import com.google.firebase.database.*;
import com.quidish.anshgupta.login.Home.BottomNavifation.BottomNavigationDrawerActivity;
import com.quidish.anshgupta.login.R;
import com.quidish.anshgupta.login.SavedLocationModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GetUsersLocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    LinearLayout curloc, rest, recent, saved, add;
    LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    List<Pair<String, String>> clg = new ArrayList<>();
    ArrayList<Pair<String, String>> recsugglist = new ArrayList<>();
    int REQUEST_CHECK_SETTINGS = 1;
    EditText search;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    View recview;
    LocationAdapter suggestionAdapter;
    RecyclerView suggestionrecycle, recentser, savedadd;
    List<Pair<String, String>> sugglist = new ArrayList<>();
    List<SavedLocationModel> savedAdd = new ArrayList<>();
    NestedScrollView scrollView;
    static public int changelay = 0;
    com.quidish.anshgupta.login.Home.Location.RecentAddAdapter recentAdapter;
    SavedAddressAdapter savedAddressAdapter;
    ProgressBar searchprog;
    ImageView cancel;
    TextView noresult;
    ProgressBar progbar;
    ConstraintLayout proglayout;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_users_location);

        clgset();

        search = findViewById(R.id.search);
        scrollView = findViewById(R.id.scrollView2);
        suggestionrecycle = findViewById(R.id.suggestion);
        rest = findViewById(R.id.rest);
        recview = findViewById(R.id.recview);
        recent = findViewById(R.id.recent);
        saved = findViewById(R.id.saved);
        recentser = findViewById(R.id.recentser);
        savedadd = findViewById(R.id.savedadd);
        add = findViewById(R.id.add);
        cancel = findViewById(R.id.cancel);
        searchprog = findViewById(R.id.searchprog);
        noresult = findViewById(R.id.noresult);
        proglayout=findViewById(R.id.proglayout);
        progbar=findViewById(R.id.progbar);
        back=findViewById(R.id.backbt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("change")) {
                rest.setVisibility(View.GONE);
                changelay = 1;
            }

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this, AddInstituteActivity.class);
                startActivity(intent);
            }
        });

        suggestionAdapter = new LocationAdapter(sugglist, com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this);
        RecyclerView.LayoutManager recyceSugg = new GridLayoutManager(com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this, 1);
        suggestionrecycle.setLayoutManager(recyceSugg);
        recyceSugg.setAutoMeasureEnabled(false);
        suggestionrecycle.setItemAnimator(new DefaultItemAnimator());
        suggestionrecycle.setAdapter(suggestionAdapter);

        recentAdapter = new com.quidish.anshgupta.login.Home.Location.RecentAddAdapter(recsugglist, com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this);
        RecyclerView.LayoutManager recyceSugg2 = new GridLayoutManager(com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this, 1);
        recentser.setLayoutManager(recyceSugg2);
        recyceSugg2.setAutoMeasureEnabled(false);
        recentser.setItemAnimator(new DefaultItemAnimator());
        recentser.setAdapter(recentAdapter);

        savedAddressAdapter = new SavedAddressAdapter(savedAdd, com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this);
        RecyclerView.LayoutManager recyceSugg3 = new GridLayoutManager(com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this, 1);
        savedadd.setLayoutManager(recyceSugg3);
        recyceSugg3.setAutoMeasureEnabled(false);
        savedadd.setItemAnimator(new DefaultItemAnimator());
        savedadd.setAdapter(savedAddressAdapter);

        displayrecentsearch();
        displaysavedaddress();

        suggestionrecycle.setNestedScrollingEnabled(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence st, int start, int before, int count) {

                String newText = st.toString();

                if (newText.length() == 0) {
                    suggestionrecycle.setVisibility(View.GONE);
                    recview.setVisibility(View.GONE);

                    if (changelay == 0)
                        rest.setVisibility(View.VISIBLE);

                    sugglist.clear();
                    suggestionrecycle.removeAllViewsInLayout();
                    suggestionAdapter.notifyDataSetChanged();
                    searchprog.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    noresult.setVisibility(View.GONE);
                }

                newText = newText.trim();
                newText = newText.toLowerCase();

                if (newText.length() == 1) {
                    searchprog.setVisibility(View.GONE);
                    cancel.setVisibility(View.VISIBLE);
                    noresult.setVisibility(View.GONE);

                    if (sugglist.size() == 0)
                        recview.setVisibility(View.GONE);
                }

                if (newText.length() > 1) {

                    searchprog.setVisibility(View.VISIBLE);

                    DatabaseReference ref;
                    ref = FirebaseDatabase.getInstance().getReference().child("Institutes");

                    final String finalNewText = newText;
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            int ct = 0;

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                String actualinst = dataSnapshot1.child("name").getValue(String.class);
                                String actualinstid = dataSnapshot1.child("id").getValue(String.class);

                                if (actualinst == null)
                                    continue;

                                String inst = actualinst.toLowerCase();

                                if (inst.length() > finalNewText.length() && (inst.startsWith(finalNewText))) {

                                    if (ct == 0) {
                                        ct = 1;

                                        sugglist.clear();
                                        suggestionrecycle.removeAllViewsInLayout();
                                        suggestionAdapter.notifyDataSetChanged();
                                    }

                                    if (sugglist.size() == 0)
                                        sugglist.add(new Pair(actualinst, actualinstid));

                                    else
                                        sugglist.add(new Pair(actualinst, actualinstid));
                                } else if (inst.length() > finalNewText.length() && (inst.contains(finalNewText))) {

                                    if (ct == 0) {
                                        ct = 1;

                                        sugglist.clear();
                                        suggestionrecycle.removeAllViewsInLayout();
                                        suggestionAdapter.notifyDataSetChanged();
                                    }

                                    sugglist.add(new Pair(actualinst, actualinstid));
                                } else {
                                    String[] splited = finalNewText.split(" ");

                                    for (String aSplited : splited) {

                                        if (inst.length() > aSplited.length() && inst.contains(aSplited)) {

                                            if (ct == 0) {
                                                ct = 1;

                                                sugglist.clear();
                                                suggestionrecycle.removeAllViewsInLayout();
                                                suggestionAdapter.notifyDataSetChanged();
                                            }

                                            sugglist.add(new Pair(actualinst, actualinstid));
                                        }
                                    }
                                }
                            }

                            searchprog.setVisibility(View.GONE);
                            cancel.setVisibility(View.VISIBLE);
                            suggestionrecycle.setVisibility(View.VISIBLE);
                            recview.setVisibility(View.VISIBLE);
                            rest.setVisibility(View.GONE);

                            if (sugglist.size() == 0) {
                                noresult.setVisibility(View.VISIBLE);
                                noresult.setText("Sorry, we couldn't find result matching " + "\"" + search.getText() + "\"");
                                recview.setVisibility(View.GONE);
                            } else {
                                noresult.setVisibility(View.GONE);
                                recview.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    suggestionAdapter.notifyDataSetChanged();

                }

            }


        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        curloc = findViewById(R.id.curloc);

        curloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableMyLocationIfPermitted();

                checkLocation();

                startLocationUpdates();
            }
        });


    }


    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    if(mLocation == null){
                        startLocationUpdates();
                    }
                    if (mLocation != null) {
                        startLocationUpdates();
                    } else {
                        startLocationUpdates();

                    }

                } else {
                    proglayout.setVisibility(View.GONE);

                }
                return;
            }

        }
    }


    public void displayrecentsearch(){

        recsugglist.clear();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int serachnumber=pref.getInt("institutenumber", 0);

        for(int i=serachnumber;i>=1;i--){
            String srchkey="recentinst"+Integer.toString(i);
            String itm=pref.getString(srchkey, null);

            String srchkeyid="recentinstid"+Integer.toString(i);
            String itmid=pref.getString(srchkeyid, null);

            if(itm!=null)
                recsugglist.add(new Pair(itm,itmid));
        }

        if(recsugglist.size()==0)
            recent.setVisibility(View.GONE);

        else {
            recentAdapter.notifyDataSetChanged();
            recent.setVisibility(View.VISIBLE);
        }
    }

    public void displaysavedaddress(){

        savedAdd.clear();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int serachnumber=pref.getInt("saved_address", 0);

        for(int i=serachnumber;i>=1;i--){

            SavedLocationModel model=new SavedLocationModel();

            String srchkey="comp_address"+Integer.toString(i);
            String itm=pref.getString(srchkey, null);
            if(itm!=null)
                model.setCompadd(itm);

            srchkey="nickname"+Integer.toString(i);
            itm=pref.getString(srchkey, null);
            if(itm!=null)
                model.setNickname(itm);

            srchkey="landmark"+Integer.toString(i);
            itm=pref.getString(srchkey, null);
            if(itm!=null)
                model.setLandmark(itm);

            srchkey="latitude"+Integer.toString(i);
            itm=pref.getString(srchkey, null);
            if(itm!=null)
                model.setLat(itm);

            srchkey="longitude"+Integer.toString(i);
            itm=pref.getString(srchkey, null);
            if(itm!=null)
                model.setLng(itm);

            srchkey="inst_name"+Integer.toString(i);
            itm=pref.getString(srchkey, null);
            if(itm!=null)
                model.setInstname(itm);

            srchkey="inst_id"+Integer.toString(i);
            itm=pref.getString(srchkey, null);
            if(itm!=null)
                model.setInstid(itm);

            model.setId(i);

            savedAdd.add(model);
        }

        if(savedAdd.size()==0)
            saved.setVisibility(View.GONE);

        else {
            savedAddressAdapter.notifyDataSetChanged();
            saved.setVisibility(View.VISIBLE);
        }
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

    protected void startLocationUpdates() {

        proglayout.setVisibility(View.VISIBLE);
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

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {

        try {

            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.isEmpty()) {
            }
            else {
                Address returnedAddress = addresses.get(0);

                String add=returnedAddress.getAddressLine(0);
                add=add.toLowerCase();

                for(int i=0;i<clg.size();i++){
                    if(add.contains(clg.get(i).first.toLowerCase())){

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                        searchhint.putString("userinstitute",clg.get(i).first);
                        searchhint.putString("userinstituteid",clg.get(i).second);

                        searchhint.apply();

                        Intent intent = new Intent(com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this, BottomNavigationDrawerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                onBackPressed();

                Toast.makeText(getApplicationContext(),"Sorry, we couldn't find result matching to your current location",Toast.LENGTH_SHORT).show();


            }
        } catch (IOException e) {

        }

    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
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
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            proglayout.setVisibility(View.GONE);
                            status.startResolutionForResult(com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        proglayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void clgset(){

        DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference().child("Institutes");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    clg.add(new Pair(dataSnapshot1.child("name").getValue(String.class),dataSnapshot1.child("id").getValue(String.class)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}