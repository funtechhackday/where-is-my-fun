package fun.hackathon.whereismyfun;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.LocationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fun.hackathon.whereismyfun.ForAR.FunCompany;
import fun.hackathon.whereismyfun.ForAR.MyCurrentAzimuth;
import fun.hackathon.whereismyfun.ForAR.MyCurrentLocation;
import fun.hackathon.whereismyfun.ForAR.OnAzimuthChangedListener;
import fun.hackathon.whereismyfun.ForAR.OnLocationChangedListener;


public class ARActivity extends AppCompatActivity implements SurfaceHolder.Callback, LocationSource.OnLocationChangedListener, OnAzimuthChangedListener, OnLocationChangedListener {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private boolean isCameraviewOn = false;
    private FunCompany mPoi;

    private double mAzimuthReal = 0;
    private double mAzimuthTeoretical = 0;

    private static final double DISTANCE_ACCURACY = 2000; // 0.9м
    private static final double AZIMUTH_ACCURACY = 25; // градусы

    public static final double TARGET_LATITUDE = 55.0308931;
    public static final double TARGET_LONGITUDE = 82.9206974;

    private MyCurrentAzimuth myCurrentAzimuth;
    private MyCurrentLocation myCurrentLocation;

    TextView descriptionTextView;
    ImageView pointerIcon;
    Button btnMap;

    private double mMyLatitude = 0;
    private double mMyLongitude = 0;

    private TextView mTextMessage;

    double distance;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent main = new Intent(ARActivity.this, MainActivity.class);
                    startActivity(main);
                    return true;
                case R.id.navigation_ar:
                    return true;
                case R.id.navigation_sale:
                    Intent sale = new Intent(ARActivity.this, SaleActivity.class);
                    startActivity(sale);
                    return true;
                case R.id.navigation_setting:
                    Intent setting = new Intent(ARActivity.this, SettingActivity.class);
                    startActivity(setting);
                    return true;
                case R.id.navigation_statistic:
                    Intent stat = new Intent(ARActivity.this, StatisticsActivity.class);
                    startActivity(stat);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_ar);

        setupListeners();
        setupLayout();
        setAugmentedRealityPoint();
    }

    private void setAugmentedRealityPoint() {
        mPoi = new FunCompany(
                "Test123",
                TARGET_LATITUDE, TARGET_LONGITUDE
        );
    }
    /*вычисляем дистанцию*/
    public double calculateDistance() {
        double dX = mPoi .getPoiLatitude() - mMyLatitude;
        double dY = mPoi .getPoiLongitude() - mMyLongitude;

        double distance = (Math. sqrt(Math.pow (dX, 2 ) + Math.pow(dY, 2 )) * 100000 );

        return distance;
    }

    /*вычисляем теоретический азимут*/
    public double calculateTeoreticalAzimuth() {
        double dX = mPoi .getPoiLatitude() - mMyLatitude;
        double dY = mPoi .getPoiLongitude() - mMyLongitude ;

        double phiAngle;
        double tanPhi;
        double azimuth = 0;

        tanPhi = Math.abs (dY / dX);
        phiAngle = Math.atan (tanPhi);
        phiAngle = Math.toDegrees (phiAngle);

        if (dX > 0 && dY > 0) { // I quater
            return azimuth = phiAngle;
        } else if (dX < 0 && dY > 0) { // II
            return azimuth = 180 - phiAngle;
        } else if (dX < 0 && dY < 0) { // III
            return azimuth = 180 + phiAngle;
        } else if (dX > 0 && dY < 0) { // IV
            return azimuth = 360 - phiAngle;
        }

        return phiAngle;
    }

    //расчитываем точность азимута
    private List<Double> calculateAzimuthAccuracy(double azimuth) {
        double minAngle = azimuth - AZIMUTH_ACCURACY ;
        double maxAngle = azimuth + AZIMUTH_ACCURACY ;
        List<Double> minMax = new ArrayList<Double>();

        if (minAngle < 0)
            minAngle += 360;

        if (maxAngle >= 360)
            maxAngle -= 360;

        minMax.clear();
        minMax.add(minAngle);
        minMax.add(maxAngle);

        return minMax;
    }

    // находится ли азимут в целевом диапазоне с учетом допустимых отклонений
    private boolean isBetween( double minAngle, double maxAngle, double azimuth) {
        if (minAngle > maxAngle) {
            if (isBetween( 0, maxAngle, azimuth) && isBetween(minAngle, 360 , azimuth))
                return true ;
        } else {
            if (azimuth > minAngle && azimuth < maxAngle)
                return true ;
        }
        return false;
    }

    // вывод информации о целе
    private void updateDescription() {
        long distance = ( long ) calculateDistance();
        int tAzimut = ( int ) mAzimuthTeoretical ;
        int rAzimut = ( int ) mAzimuthReal ;

        String text = mPoi.getPoiName()
                + " location:"
                + "\n latitude: " + TARGET_LATITUDE + "  longitude: " + TARGET_LONGITUDE
                + "\n Current location:"
                + "\n Latitude: " + mMyLatitude       + "  Longitude: " + mMyLongitude
                + "\n "
                + "\n Target azimuth: " + tAzimut
                + " \n Current azimuth: " + rAzimut
                + " \n Distance: " + distance;

        descriptionTextView.setText(text);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if ( isCameraviewOn ) {
            mCamera.stopPreview();
            isCameraviewOn = false ;
        }

        if ( mCamera != null ) {
            try {
                mCamera.setPreviewDisplay( mSurfaceHolder);
                mCamera.startPreview();
                isCameraviewOn = true ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null ;
        isCameraviewOn = false ;
    }

    @Override
    public void onLocationChanged(Location location) {
        mMyLatitude = location.getLatitude();
        mMyLongitude = location.getLongitude();
        mAzimuthTeoretical = calculateTeoreticalAzimuth();
        Toast.makeText (this , "latitude: "+location.getLatitude()+ " longitude: "+location.getLongitude(), Toast.LENGTH_SHORT ).show();

        //если устройство возвращает азимут = 0 отображаем картинку на основе значения дистанции
        if (mAzimuthReal == 0){
            if (distance <= DISTANCE_ACCURACY) {
                pointerIcon.setVisibility(View.VISIBLE);
            } else {
                pointerIcon.setVisibility(View.INVISIBLE);
            }
        }
        updateDescription();
    }

    @Override
    public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
        mAzimuthReal = azimuthChangedTo;
        mAzimuthTeoretical = calculateTeoreticalAzimuth();
        int distance = ( int ) calculateDistance();

        pointerIcon = (ImageView) findViewById(R.id. icon );

        double minAngle = calculateAzimuthAccuracy(mAzimuthTeoretical ).get( 0);
        double maxAngle = calculateAzimuthAccuracy(mAzimuthTeoretical ).get( 1);

        if ((isBetween(minAngle, maxAngle, mAzimuthReal )) && distance <= DISTANCE_ACCURACY ) {
            pointerIcon.setVisibility(View. VISIBLE );
        } else {
            pointerIcon.setVisibility(View. INVISIBLE );
        }

        updateDescription();
    }

    @Override
    protected void onStop() {
        myCurrentAzimuth.stop();
        myCurrentLocation.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myCurrentAzimuth.start();
        myCurrentLocation.start();
    }

    private void setupListeners() {
        myCurrentLocation = new MyCurrentLocation(this);
        myCurrentLocation.buildGoogleApiClient( this );
        myCurrentLocation.start();

        myCurrentAzimuth = new MyCurrentAzimuth( this, this);
        myCurrentAzimuth.start();
    }

    private void setupLayout() {
        descriptionTextView = (TextView) findViewById(R.id.cameraTextView );
        getWindow().setFormat(PixelFormat. UNKNOWN);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraview );
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback( this );
        mSurfaceHolder.setType(SurfaceHolder. SURFACE_TYPE_PUSH_BUFFERS );
    }
}
