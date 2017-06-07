package nakthon.soraya.woodriverservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private double userLatADouble = 13.66788036,
            userLngADouble = 100.6222558;  //Fix to Bangna


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Synchronize Location
        synchronizeLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        myMapFragment();

        //Setup Object
        setupObject();

        //searchController
        searchController();

    }   // Main Method

    private void searchController() {
        EditText editText = (EditText) findViewById(R.id.edtSearch);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, SearchActivity.class));
            }
        });
    }

    private void synchronizeLocation() {

        try {

            String strURL = "http://woodriverservice.com/Android/getLocation.php";

            GetAllData getAllData = new GetAllData(this);
            getAllData.execute(strURL);

            String strJSON = getAllData.get();
            Log.d("7JuneV1", "JSON ==> " + strJSON);

            getAllData.progressDialog.dismiss();


            JSONArray jsonArray = new JSONArray(strJSON);
            int intLength = jsonArray.length();
            Log.d("7JuneV2", "intLength ==> " + intLength);






        } catch (Exception e) {
            Log.d("7JuneV1", "e synchronize ==> " + e.toString());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //For Network
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            userLatADouble = networkLocation.getLatitude();
            userLngADouble = networkLocation.getLongitude();
        }

        //For GPS
        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            userLatADouble = gpsLocation.getLatitude();
            userLngADouble = gpsLocation.getLongitude();
        }

        Log.d("7JuneV1", "Lat ==> " + userLatADouble);
        Log.d("7JuneV1", "Lng ==> " + userLngADouble);

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location myFindLocation(String strProvider) {

        Location location = null;
        if (locationManager.isProviderEnabled(strProvider)) {
            //Auto Complete Permission Check
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);
        }

        return location;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            userLatADouble = location.getLatitude();
            userLngADouble = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void setupObject() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
    }

    private void myMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Create Center Map
        LatLng userLatLng = new LatLng(userLatADouble, userLngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16));

        //Create Marker
        createMarker(userLatLng, R.mipmap.ic_launcher);


    }   // onMapReady

    private void createMarker(LatLng latLng, int intIcon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(intIcon));
        mMap.addMarker(markerOptions);
    }

}   // Main Class
