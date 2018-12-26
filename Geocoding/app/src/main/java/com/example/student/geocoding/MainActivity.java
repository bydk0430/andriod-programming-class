package com.example.student.geocoding;

import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView tv_result;
    EditText et_lng, et_lat, et_addr;
    Button btn_geo, btn_revGeo;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_result = (TextView)findViewById(R.id.tv_result);

        et_addr = (EditText)findViewById(R.id.et_addr);
        et_lat = (EditText)findViewById(R.id.et_lat);
        et_lng = (EditText)findViewById(R.id.et_lng);

        btn_geo = (Button)findViewById(R.id.btn_geo);
        btn_revGeo = (Button)findViewById(R.id.btn_revGeo);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoThread geoThread = new GeoThread(
                        et_addr.getText().toString());
                geoThread.start();
            }
        });

        btn_revGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(
                        Double.valueOf(et_lng.getText().toString()),
                        Double.valueOf(et_lat.getText().toString()));
                RevGeoThread revGeoThread = new RevGeoThread(latLng);
                revGeoThread.start();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.googleMap = map;

        LatLng SEOUL = new LatLng(37.56,126.97);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(16));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);
    }

    class RevGeoThread extends Thread {
        LatLng latLng;

        public RevGeoThread(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(MainActivity.this);
            List<Address> addr = null;

            try {
                addr = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
                if(addr != null && addr.size() > 0) {
                    Address pin = addr.get(0);
                    Toast.makeText(MainActivity.this,"주소 : " + pin.getAddressLine(0),Toast.LENGTH_LONG).show();




                } else  {
                    Toast.makeText(MainActivity.this, "검색 결과가 없습니다",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class GeoThread extends Thread {
        String address;

        public GeoThread(String address) {
            this.address = address;
        }

        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(MainActivity.this);
            List<Address> result = null;

            try {
                result = geocoder.getFromLocationName(address, 1);

                if(result != null && result.size() >0) {
                    Address pin = result.get(0);
                    LatLng latLng = new LatLng(pin.getLatitude(), pin.getLongitude());

                    Toast.makeText(MainActivity.this, "lat : " + latLng.latitude
                    + " lng : " + latLng.longitude, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "검색결과가 없습니다",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
