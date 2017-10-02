package jt.directiongiver000;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryMapsActivity extends DGActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener  {
    private static final double[] TODO = null;
    private GoogleMap mMap;
    private PolylineOptions bestLine = new PolylineOptions();
    private PolylineOptions userLine = new PolylineOptions();
    private ArrayList<LatLng> bestLinePosition = new ArrayList<>();
    private String bestLineString;
    private ArrayList<LatLng> userLinePosition = new ArrayList<>();
    private String userLineString;
    private double px;
    private double py;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("歷史紀錄");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = this.getIntent();//取得傳遞過來的資料
        final String startname = intent.getStringExtra("startname");
        final String endname = intent.getStringExtra("endname");
        bestLineString = intent.getStringExtra("bestLine");
        userLineString = intent.getStringExtra("userLine");
        px = intent.getDoubleExtra("px",0.00);
        py = intent.getDoubleExtra("py",0.00);



        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(startname + "→" + endname);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HistoryMapsActivity.this, MapsActivity.class);
                String file_name = "destination";

                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                    fileOutputStream.write(endname.getBytes());
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });


    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(HistoryMapsActivity.this, HistoryActivity.class);
            startActivity(intent);
            HistoryMapsActivity.this.finish();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBestLine(bestLineString);
                mMap.addPolyline(bestLine.color(0x993498DB));
                System.out.println("userLineString : " + userLineString);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(bestLinePosition.get(0))
                                .title("當時出發點")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.next_road)));
                mMap.addMarker(
                        new MarkerOptions()
                                .position(bestLinePosition.get(bestLinePosition.size()-1))
                                .title("目的地")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_ico)));
                if(!userLineString.isEmpty())
                {
                    setUserLine(userLineString);
                    mMap.addPolyline(userLine.color(0x99E74C3C));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(bestLinePosition.get(bestLinePosition.size()/2)));
                moveMap(new LatLng(py,px));
            }});
    }private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(15)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    private void setBestLine(String bestLineString)
    {
        String[] tokens = bestLineString.split(";");
        for (String token:tokens)
        {
            String[] tmp = token.split(",");
            System.out.println(Double.parseDouble(tmp[0])+","+Double.parseDouble(tmp[1]));
            LatLng tmpPosition = new LatLng(Double.parseDouble(tmp[1]),Double.parseDouble(tmp[0]));
            bestLinePosition.add(tmpPosition);
            bestLine.add(tmpPosition);
        }
    }

    private void setUserLine(String userLineString)
    {
        String[] tokens = userLineString.split(";");
        for (String token:tokens)
        {
            String[] tmp = token.split(",");
            System.out.println(Double.parseDouble(tmp[0])+","+Double.parseDouble(tmp[1]));
            LatLng tmpPosition = new LatLng(Double.parseDouble(tmp[1]),Double.parseDouble(tmp[0]));
            userLinePosition.add(tmpPosition);
            userLine.add(tmpPosition);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
