package nakthon.soraya.woodriverservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private double userLatADouble = 13.66788036,
            userLngADouble = 100.6222558;  //Fix to Bangna
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
    private double destinateLatADouble, destinateLngADouble;
    private String[] resultStrings;
    private LatLng userLatLng;
    private MyManage myManage;
    private String idPassengerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Check SQLite
        checkSQLite();


        //Synchronize Location
        //synchronizeLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        myMapFragment();

        //Setup Object
        setupObject();

        //searchController
        searchController();

    }   // Main Method

    private void checkSQLite(){

        //Instant Object MyManage
        myManage = new MyManage(MapsActivity.this);

        //Create Cursor by Select All
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM passengerTABLE", null);
        cursor.moveToFirst();
        Log.d("22JuneV1", "cursor.getCount ==> " + cursor.getCount() );

        //Check Cursor.count
        if (cursor.getCount() == 0) {

            //No Data
            Log.d("22JuneV1", "No Data");

            Toast.makeText(MapsActivity.this, "Please Register", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MapsActivity.this, NewRegisterActivity.class));

        } else {

            //Have Data
            String passengerString = cursor.getString(1);
            String urlPHP = "http://woodriverservice.com/Android/getPassengerWhereName.php";
            Log.d("22JuneV1", "passengerString ==> " + passengerString);

            //Get ID
            try {

                GetValueWhere getValueWhere = new GetValueWhere(MapsActivity.this);
                getValueWhere.execute("Name", passengerString, urlPHP);
                String strJSON = getValueWhere.get();

                JSONArray jsonArray = new JSONArray(strJSON);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                idPassengerString = jsonObject.getString("id");

                Log.d("22JuneV1", "id of Passenger ==> " + idPassengerString);

            } catch (Exception e) {
                Log.d("22JuneV1", "e Check ==> " + e.toString());
            }


        }


        cursor.close();

    }   // checkSQLite

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("8JuneV3", "onActivity Work");
        Log.d("8JuneV3", "requestCode ==> " + requestCode);

        resultStrings = data.getStringArrayExtra("Result");
        for (int i = 0; i < resultStrings.length; i += 1) {
            Log.d("8JuneV3", "resultString(" + i + ") ==> " + resultStrings[i]);

            //Change Center Map & Create Marker
            destinateLatADouble = Double.parseDouble(resultStrings[2]);
            destinateLngADouble = Double.parseDouble(resultStrings[3]);
            LatLng latLng = new LatLng(destinateLatADouble, destinateLngADouble);
            createCenterMap(latLng);
            createMarker(latLng, R.mipmap.ic_destination);

        }


    }   // onActivityResult

    private void searchController() {
        EditText editText = (EditText) findViewById(R.id.edtSearch);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this,
                        SearchActivity.class);
                intent.putExtra("Lat", userLatADouble);
                intent.putExtra("Lng", userLngADouble);
                startActivity(intent);
            }
        });
    }   // searchController

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

        checkSQLite();


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
        userLatLng = new LatLng(userLatADouble, userLngADouble);

        //Create Center Map
        createCenterMap(userLatLng);

        //Create Marker
        createMarker(userLatLng, R.mipmap.ic_start);


    }   // onMapReady

    private void createCenterMap(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    private void createMarker(LatLng latLng, int intIcon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(intIcon));
        mMap.addMarker(markerOptions);
    }

}   // Main Class
