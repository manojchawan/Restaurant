package com.manchaw.restaurant.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.manchaw.restaurant.R;
import com.manchaw.restaurant.model.NearbyRestaurant;
import com.manchaw.restaurant.model.SearchResponse;
import com.manchaw.restaurant.util.Util;
import com.manchaw.restaurant.viewModal.RestaurantViewModal;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_PERMISSION_CODE = 1;
    protected GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ConstraintLayout root;
    private Toolbar toolbar;
    private TextView locationTitle;
    private LottieAnimationView loading;

    private RestaurantViewModal mViewModel;
    private List<NearbyRestaurant> restaurantList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NearbyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        root = findViewById(R.id.rootLayout);
        locationTitle = findViewById(R.id.locationTitle);
        loading = findViewById(R.id.loading);

        recyclerView = findViewById(R.id.nearbyRv);
        mAdapter = new NearbyAdapter(this, restaurantList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void checkPermissions() {
        if (Util.INSTANCE.isConnected(this)) {
            Util.INSTANCE.debugLog(TAG, "internet available");

            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new
                        String[]{ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
            } else {
                getlocationData(); //permission already given

            }
        } else
            Util.INSTANCE.showSnackbar(root, "Please Connect to internet and click your location to fetch restaurants");

    }


    @SuppressLint("MissingPermission")
    private void getlocationData() {
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) { // Got last known location.
                            mViewModel = ViewModelProviders.of(MainActivity.this).get(RestaurantViewModal.class);
                            mViewModel.getRestaurants(location.getLatitude(), location.getLongitude()).observe(MainActivity.this, new Observer<SearchResponse>() {
                                @Override
                                public void onChanged(SearchResponse searchResponse) {
                                    restaurantList.addAll(searchResponse.getNearbyRestaurants());
                                    locationTitle.setText(searchResponse.getLocation().getTitle());
                                    recyclerView.setVisibility(View.VISIBLE);
                                    mAdapter.notifyDataSetChanged();
                                    loading.setVisibility(View.GONE);
                                }
                            });
                        } else
                            Util.INSTANCE.showSnackbar(root, "Something went wrong while fetching location");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_location, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_location:
                locationTitle.setText("Loading");
                checkPermissions();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Util.INSTANCE.debugLog(TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Util.INSTANCE.debugLog(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }


    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Util.INSTANCE.debugLog(TAG, "PermissionsResult: Location Permission granted");
                getlocationData();
            } else {
                Snackbar.make(root, "App needs access to Location", Snackbar.LENGTH_LONG)
                        .setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_CODE);
                            }
                        }).show();
            }
        }
    }
}
