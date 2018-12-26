package com.example.student.gmaps;

import android.app.FragmentManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{


    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.googleMap = map;

        ArrayList<LocationVO> list = new ArrayList<>();
        list.add(new LocationVO(33.511379, 126.491417, "제주공항", "제주"));
        list.add(new LocationVO(37.504026, 127.039146, "멀티캠퍼스", "서울 강남"));
        list.add(new LocationVO(37.589034, 126.974470,"청와대", "서울"));

        MarkerOptions markerOptions;

        for(int i=0; i<list.size(); i++) {
            markerOptions = new MarkerOptions();
            LatLng SEOUL = new LatLng(list.get(i).getLat(), list.get(i).getLng());
            markerOptions.position(SEOUL);
            markerOptions.title(list.get(i).getName());
            markerOptions.snippet(list.get(i).getAddr());
            map.addMarker(markerOptions);
        }

        //  지도 객체 map
        LatLng SEOUL = new LatLng(37.56,126.97);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));



    }

}
