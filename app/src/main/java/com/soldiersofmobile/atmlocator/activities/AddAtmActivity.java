package com.soldiersofmobile.atmlocator.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;
import com.soldiersofmobile.atmlocator.R;
import com.soldiersofmobile.atmlocator.db.Atm;
import com.soldiersofmobile.atmlocator.db.Bank;
import com.soldiersofmobile.atmlocator.db.DbHelper;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAtmActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    public static final String DATA_TAG = "data";
    @Bind(R.id.pickLocationButton)
    Button pickLocationButton;
    @Bind(R.id.addressEditText)
    EditText addressEditText;
    @Bind(R.id.latitudeTextView)
    TextView latitudeTextView;
    @Bind(R.id.longitudeTextView)
    TextView longitudeTextView;
    @Bind(R.id.bankSpinner)
    Spinner bankSpinner;
    @Bind(R.id.saveButton)
    Button saveButton;
    private DbHelper dbHelper;
    private AtmDataFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atm);

        ButterKnife.bind(this);

        initGoogleApi();

        dbHelper = new DbHelper(getApplicationContext());

        try {
            Dao<Bank, ?> dao = dbHelper.getDao(Bank.class);
            List<Bank> banks = dao.queryForAll();
            ArrayAdapter<Bank> bankArrayAdapter = new ArrayAdapter<Bank>(this, android.R.layout.simple_list_item_1,
                    banks);

            bankSpinner.setAdapter(bankArrayAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if(savedInstanceState == null) {
            fragment = new AtmDataFragment();

            getSupportFragmentManager().beginTransaction().add(fragment, DATA_TAG).commit();
        } else {
            fragment = (AtmDataFragment) getSupportFragmentManager().findFragmentByTag(DATA_TAG);
        }



    }

    @OnClick(R.id.saveButton)
    public void saveAtm() {

        Atm atm = new Atm();
        atm.setAddress(fragment.address);
        atm.setLatitude(fragment.latLng.latitude);
        atm.setLongitude(fragment.latLng.longitude);

        Bank bank = (Bank) bankSpinner.getSelectedItem();

        atm.setBank(bank);

        try {
            Dao<Atm, ?> dao = dbHelper.getDao(Atm.class);

            dao.create(atm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finish();

    }

    @OnClick(R.id.pickLocationButton)
    public void pickLocation() {

        startLocationPicker();

    }


    public static final String LOG_TAG = "TAG";
    public static final int RESOLUTION_REQUEST_CODE = 1234;
    private static final int REQUEST_PLACE_PICKER = 4321;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;



    class AtmDataFragment extends Fragment {

        private String name;
        private String address;
        private String phone;
        private LatLng latLng;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }
    }




    private void initGoogleApi() {

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);
    }

    private void startLocationPicker() {


        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(AddAtmActivity.this);
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

            // Hide the pick option in the UI to prevent users from starting the picker
            // multiple times.
            // showPickAction(false);

        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), AddAtmActivity.this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(AddAtmActivity.this, "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == REQUEST_PLACE_PICKER) {
            // This result is from the PlacePicker dialog.

            // Enable the picker option
            //showPickAction(true);

            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, this);

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */

                addressEditText.setText(place.getAddress());
                latitudeTextView.setText(String.valueOf(place.getLatLng().latitude));
                longitudeTextView.setText(String.valueOf(place.getLatLng().longitude));


                fragment.name = place.getName().toString();
                fragment.address = place.getAddress().toString();
                fragment.latLng = place.getLatLng();
                fragment.phone = place.getPhoneNumber().toString();




            } else {
                // User has not selected a place, hide the card.
                //getCardStream().hideCard(CARD_DETAIL);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // END_INCLUDE(activity_result)
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLUTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d(LOG_TAG, "Connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(lastLocation != null) {
            handleLocation(lastLocation);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    private void handleLocation(Location lastLocation) {
        Log.d(LOG_TAG, "Last location:" + lastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        handleLocation(location);
    }

}
