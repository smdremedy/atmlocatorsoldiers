package com.soldiersofmobile.atmlocator.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.Dao;
import com.soldiersofmobile.atmlocator.R;
import com.soldiersofmobile.atmlocator.db.Atm;
import com.soldiersofmobile.atmlocator.db.AtmDao;
import com.soldiersofmobile.atmlocator.db.Bank;
import com.soldiersofmobile.atmlocator.db.DbHelper;

import java.sql.SQLException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DbHelper dbHelper = new DbHelper(getApplicationContext());

        try {
            AtmDao atmDao = dbHelper.getDao(Atm.class);
            Dao<Bank, ?> bankDao = dbHelper.getDao(Bank.class);

            List<Atm> atms = atmDao.queryForAll();

            for (Atm atm : atms) {

                bankDao.refresh(atm.getBank());
                LatLng sydney = new LatLng(atm.getLatitude(), atm.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney)
                        .title(atm.getBank().getName()))
                        .setSnippet(atm.getBank().getPhone());



                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(5));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(this, AddAtmActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }
}
