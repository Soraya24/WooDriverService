package nakthon.soraya.woodriverservice;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DirectionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] destinationStrings;
    private double startLatADouble, startLngADouble;
    private LatLng startLatLng, destinationLatLng;
    private int[] iconInts = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_destination};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        //Receive Value & Setup
        receiveAndSetup();


        //Map Fragment
        mapFragment();

    }   // Main Method

    private void receiveAndSetup() {
        destinationStrings = getIntent().getStringArrayExtra("Result");
        startLatADouble = getIntent().getDoubleExtra("Lat", 0);
        startLngADouble = getIntent().getDoubleExtra("Lng", 0);
        startLatLng = new LatLng(startLatADouble, startLngADouble);
        destinationLatLng = new LatLng(Double.parseDouble(destinationStrings[2]),
                Double.parseDouble(destinationStrings[3]));
    }

    private void mapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Create Center Map
        createCenterMap(startLatLng);

        //Create Marker Start
        createMarker(startLatLng, iconInts[0]);

        //Create Marker Destination
        createMarker(destinationLatLng, iconInts[1]);

    }   // onMapReady

    private void createMarker(LatLng latLng, int intIcon) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(intIcon));
        mMap.addMarker(markerOptions);
    }

    private void createCenterMap(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 16));
    }

}   // Main Class
