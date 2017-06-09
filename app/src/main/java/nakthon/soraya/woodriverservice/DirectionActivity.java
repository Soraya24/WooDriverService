package nakthon.soraya.woodriverservice;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class DirectionActivity extends FragmentActivity implements OnMapReadyCallback, DirectionCallback {

    private GoogleMap mMap;
    private String[] destinationStrings;
    private double startLatADouble, startLngADouble;
    private LatLng startLatLng, destinationLatLng;
    private int[] iconInts = new int[]{R.mipmap.ic_start, R.mipmap.ic_destination};
    private String serverKeyString = "AIzaSyAloVYlvZeXa7A86bqofs_0ytQ4Pz-CBaQ";
    private int dayAnInt, monthAnInt, yearAnInt, hourAnInt, minusAnInt;
    private boolean aBoolean = true;
    private String dateString, timeString;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        //Receive Value & Setup
        receiveAndSetup();

        //Map Fragment
        mapFragment();

        //Back Controller
        backController();

        //Date Controller
        dateController();

        //Find Current Date & Time
        findCurrentDatTime();

    }   // Main Method

    private void dateController() {

        ImageView imageView = (ImageView) findViewById(R.id.imvDate);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setupDateAndTime();
                //setupTime(hourAnInt, minusAnInt);

            }
        });

    }

    private void setupDateAndTime() {

        aBoolean = true;
        //Show Date Picker
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearAnInt = i;
                monthAnInt = i1;
                dayAnInt = i2;
                if (aBoolean) {
                    setupTime(hourAnInt, minusAnInt);
                }
            }
        }, yearAnInt, monthAnInt, dayAnInt);
        datePickerDialog.setCancelable(false);

        datePickerDialog.show();
    }

    private void findCurrentDatTime() {
        Calendar calendar = Calendar.getInstance();
        dayAnInt = calendar.get(Calendar.DAY_OF_MONTH);
        monthAnInt = calendar.get(Calendar.MONTH);
        yearAnInt = calendar.get(Calendar.YEAR);
        hourAnInt = calendar.get(Calendar.HOUR_OF_DAY);
        minusAnInt = calendar.get(Calendar.MINUTE);

        showDateTime();

    }

    private void showDateTime() {
        TextView textView = (TextView) findViewById(R.id.txtDate);
        dateString = Integer.toString(dayAnInt) + "/" + Integer.toString(monthAnInt + 1) + "/" + yearAnInt;
        timeString = Integer.toString(hourAnInt) + ":" + Integer.toString(minusAnInt) + ":00";

        textView.setText(dateString + " " + timeString);

    }

    private void setupTime(final int myHourAnInt, int myMinusAnInt) {
        Log.d("9JuneV2", "setupTime Work");
        aBoolean = false;

        TimePickerDialog timePickerDialog = new TimePickerDialog(DirectionActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hourAnInt = i;
                minusAnInt = i1;
                showDateTime();
            }
        }, myHourAnInt, myMinusAnInt, true);
        timePickerDialog.show();

    }

    private void backController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

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

        //Create Direction
        createDirection(startLatLng, destinationLatLng);

    }   // onMapReady

    private void createDirection(LatLng startLatLng, LatLng destinationLatLng) {

        GoogleDirection.withServerKey(serverKeyString)
                .from(startLatLng)
                .to(destinationLatLng)
                .transportMode(TransportMode.DRIVING)
                .execute(DirectionActivity.this);

    }

    private void createMarker(LatLng latLng, int intIcon) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(intIcon));
        mMap.addMarker(markerOptions);
    }

    private void createCenterMap(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 12));
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

        if (direction.isOK()) {

            ArrayList<LatLng> latLngArrayList = direction.getRouteList()
                    .get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(DirectionActivity.this,
                    latLngArrayList, 5, Color.RED));

            Info distanceInfo = direction.getRouteList().get(0).getLegList().get(0).getDistance();
            String strdistance = distanceInfo.getText();
            Log.d("9JuneV1", "distance ==> " + strdistance);

            //Calculate Price And Show
            calculatePriceAndShow(strdistance);

        }

    }

    private void calculatePriceAndShow(String strdistance) {

        //Show Distance
        TextView distanceTextView = (TextView) findViewById(R.id.txtDistance);
        distanceTextView.setText(strdistance);

        //Show Price
        String strPrice = myCalPrice(strdistance);
        TextView priceTextView = (TextView) findViewById(R.id.txtPrice);
        priceTextView.setText(strPrice);


    }

    private String myCalPrice(String strdistance) {

        String strPrice = null;
        double douDistance = 0;
        double douPrice = 0;
        Log.d("9JuneV1", "Receive strdistance ==> " + strdistance);
        String[] strings = strdistance.split(" ");

        douDistance = Double.parseDouble(strings[0]);
        douPrice = douDistance * 5;
        strPrice = Double.toString(douPrice) + " THB";

        return strPrice;
    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }
}   // Main Class
