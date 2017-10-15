package fun.hackathon.whereismyfun;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import fun.hackathon.whereismyfun.ForAR.MyCurrentAzimuth;
import fun.hackathon.whereismyfun.ForAR.MyCurrentLocation;
import fun.hackathon.whereismyfun.ForAR.OnLocationChangedListener;

public class MainActivity extends AppCompatActivity implements OnLocationChangedListener {

    private GoogleMap googleMap = null;
    private double mMyLatitude = 0;
    private double mMyLongitude = 0;
    private MyCurrentLocation myCurrentLocation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_ar:
                    Intent ar = new Intent(MainActivity.this, ARActivity.class);
                    startActivity(ar);
                    return true;
                case R.id.navigation_sale:
                    Intent sale = new Intent(MainActivity.this, SaleActivity.class);
                    startActivity(sale);
                    return true;
                case R.id.navigation_setting:
                    Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(setting);
                    return true;
                case R.id.navigation_statistic:
                    Intent stat = new Intent(MainActivity.this, StatisticsActivity.class);
                    startActivity(stat);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupListeners();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    public void onLocationChanged(Location currentLocation) {
        mMyLatitude = currentLocation.getLatitude();
        mMyLongitude = currentLocation.getLongitude();
        Toast.makeText (this , "latitude: " + mMyLatitude + " longitude: " + mMyLongitude, Toast.LENGTH_SHORT ).show();
        if(googleMap == null){
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap1) {
                    googleMap = googleMap1;
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMyLatitude, mMyLongitude), 14));
                }
            });
        }
    }

    @Override
    protected void onStop() {
        myCurrentLocation.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myCurrentLocation.start();
    }

    private void setupListeners() {
        myCurrentLocation = new MyCurrentLocation(this);
        myCurrentLocation.buildGoogleApiClient( this );
        myCurrentLocation.start();
    }
}
