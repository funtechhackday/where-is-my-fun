package fun.hackathon.whereismyfun;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fun.hackathon.whereismyfun.ForAR.MyCurrentAzimuth;
import fun.hackathon.whereismyfun.ForAR.MyCurrentLocation;
import fun.hackathon.whereismyfun.ForAR.OnLocationChangedListener;
import fun.hackathon.whereismyfun.Retrofit.Company;
import fun.hackathon.whereismyfun.Retrofit.Companys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                    Retrofit retrofitMy = new Retrofit.Builder().baseUrl("http://192.168.0.106:5000").
                            addConverterFactory(GsonConverterFactory.create()).build();
                    Companys com = retrofitMy.create(Companys.class);

                    Call<Company> companysList = com.getCompanys(mMyLatitude, mMyLongitude);
                    companysList.enqueue(new Callback<Company>() {
                        @Override
                        public void onResponse(Call<Company> call, Response<Company> response) {
                            List<Company.Comp> list = response.body().listRows;
                            for(Company.Comp co : list){
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(co.lat, co.lon)).title(co.title).snippet(co.desc));
                            }
                        }
                        @Override
                        public void onFailure(Call<Company> call, Throwable t) {
                            Log.d("пример. ошибка", t.toString());
                        }
                    });
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
