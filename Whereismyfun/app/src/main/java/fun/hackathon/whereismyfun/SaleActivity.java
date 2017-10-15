package fun.hackathon.whereismyfun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fun.hackathon.whereismyfun.Adapters.SaleAdapter;

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

        String[] company = {"Coca-cola", "Oracle", "Nisan", "Holiday"};

        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        SaleAdapter saleAdapter = new SaleAdapter(company);
        rv.setAdapter(saleAdapter);
    }

}
