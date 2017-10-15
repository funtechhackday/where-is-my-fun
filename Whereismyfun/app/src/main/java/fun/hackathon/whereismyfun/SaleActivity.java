package fun.hackathon.whereismyfun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import fun.hackathon.whereismyfun.Adapters.SaleAdapter;
import fun.hackathon.whereismyfun.Retrofit.Company;
import fun.hackathon.whereismyfun.Retrofit.Companys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SaleActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home = new Intent(SaleActivity.this, MainActivity.class);
                    startActivity(home);
                    return true;
                case R.id.navigation_ar:
                    Intent ar = new Intent(SaleActivity.this, ARActivity.class);
                    startActivity(ar);
                    return true;
                case R.id.navigation_sale:
                    return true;
                case R.id.navigation_setting:
                    Intent setting = new Intent(SaleActivity.this, SettingActivity.class);
                    startActivity(setting);
                    return true;
                case R.id.navigation_statistic:
                    Intent stat = new Intent(SaleActivity.this, StatisticsActivity.class);
                    startActivity(stat);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_sale);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        Retrofit retrofitMy = new Retrofit.Builder().baseUrl("http://192.168.0.106:5000").
                addConverterFactory(GsonConverterFactory.create()).build();
        Companys com = retrofitMy.create(Companys.class);

//        Call<Company> companysList = com.getCompanys(mMyLatitude, mMyLongitude);
//        companysList.enqueue(new Callback<Company>() {
//            @Override
//            public void onResponse(Call<Company> call, Response<Company> response) {
//                List<Company.Comp> list = response.body().listRows;
//                for(Company.Comp co : list){
//                    googleMap.addMarker(new MarkerOptions().position(new LatLng(co.lat, co.lon)).title(co.title).snippet(co.desc));
//                }
//            }
//            @Override
//            public void onFailure(Call<Company> call, Throwable t) {
//                Log.d("пример. ошибка", t.toString());
//            }
//        });

        String[] company = {"Coca-cola", "Oracle", "Nisan", "Holiday"};

        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        SaleAdapter saleAdapter = new SaleAdapter(company);
        rv.setAdapter(saleAdapter);
    }

}
