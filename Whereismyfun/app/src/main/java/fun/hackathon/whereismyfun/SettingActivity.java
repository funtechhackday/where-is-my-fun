package fun.hackathon.whereismyfun;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(home);
                    return true;
                case R.id.navigation_ar:
                    Intent ar = new Intent(SettingActivity.this, ARActivity.class);
                    startActivity(ar);
                    return true;
                case R.id.navigation_sale:
                    Intent sale = new Intent(SettingActivity.this, SaleActivity.class);
                    startActivity(sale);
                    return true;
                case R.id.navigation_setting:
                    return true;
                case R.id.navigation_statistic:
                    Intent stat = new Intent(SettingActivity.this, StatisticsActivity.class);
                    startActivity(stat);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_setting);
    }

}
