package nakthon.soraya.woodriverservice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.maps.model.PolylineOptions;

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
    private boolean aBoolean2 = true;
    private boolean aBoolean3 = false;
    private String dateString, timeString;
    private PolylineOptions polylineOptions;


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

        //Add Point Controller
        addPointController();

        //Confirm Controller
        confirmController();


    }   // Main Method

    @Override
    protected void onResume() {
        super.onResume();

        String tag = "20JuneV2";
        Log.d(tag, "aBoolean2 ==> " + aBoolean2);

        if (!aBoolean2) {

            Log.d(tag, "สิ่งที่ทำหลังจาก AddPoint");

        }




    }

    private void confirmController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvConfirm);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAlertConfirm("Add Point");
            }
        });
    }

    private void addPointController() {
        final ImageView imageView = (ImageView) findViewById(R.id.imvAddPoint);
        final String tag = "19JuneV1";
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // aBoolean2 false ==> สามารถเพิ่ม Point ได้
                aBoolean2 = false;
                Log.d(tag, "Click Add Point aBoolean2 ==> " + aBoolean2);

                //Change Image When Click
                imageView.setImageResource(R.mipmap.ic_unadd);


            }   // onClick
        });
    }   //

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

        //Initial View
        ImageView imageView = (ImageView) findViewById(R.id.imvBack);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("20JuneV1", "Click Back Status ==> " + aBoolean2);

                Intent intent = new Intent(DirectionActivity.this, SearchActivity.class);
                intent.putExtra("Status", aBoolean2);
                setResult(1000, intent);

                if (aBoolean2) {
                    finish();
                } else {

                    backForAddPoint();


                    //startActivity(intent);
                }

            }   // onClick
        });
    }   // backController

    private void backForAddPoint() {

        Intent intent = new Intent(DirectionActivity.this, SearchActivity.class);
        intent.putExtra("Status", aBoolean2);
        startActivityForResult(intent, 1200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String tag = "20JuneV2";

        if (requestCode == 1200) {

            Log.d(tag, "สิ่งที่ทำงาน หลัง SearchView ที่ AddPoinet");
            String[] locationStrings = data.getStringArrayExtra("Result");
            LatLng latLng = new LatLng(Double.parseDouble(locationStrings[2]),
                    Double.parseDouble(locationStrings[3]));
            addMarkerPoint(latLng);


        }


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

    private void myAlertConfirm(String strMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DirectionActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Please Confirm");
        builder.setMessage(strMessage);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                aBoolean3 = true;
                aBoolean2 = true;
                destinationLatLng = startLatLng;

                //Clear Map
                mMap.clear();

                //Marker of Start
                createMarker(startLatLng, iconInts[0]);

                //Create Marker of Destination
                createMarker(destinationLatLng, iconInts[1]);

                createDirection(startLatLng, destinationLatLng);

                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

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

        //Click On Map
        clickOnMap();

    }   // onMapReady

    private void clickOnMap() {

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (aBoolean2) {
                    Log.d("9JuneV3", "You Click Map");
                    destinationLatLng = latLng;

                    //Clear Map
                    mMap.clear();

                    //Marker of Start
                    createMarker(startLatLng, iconInts[0]);

                    //Create Marker of Destination
                    createMarker(destinationLatLng, iconInts[1]);

                    createDirection(startLatLng, destinationLatLng);
                } else {
                    addMarkerPoint(latLng);

                   // myAlertConfirm("Add Point2");

                }


            }   // onMapClick
        });

    }   // clickOnMap

    private void addMarkerPoint(LatLng latLngDestination) {
        startLatLng = destinationLatLng; // Assign Destination ==> Start
        destinationLatLng = latLngDestination;      // Assign latLnt ==> Destination
        createMarker(destinationLatLng, iconInts[1]);
        createDirection(startLatLng, destinationLatLng);
    }

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

            polylineOptions = DirectionConverter.createPolyline(DirectionActivity.this,
                    latLngArrayList, 5, Color.RED);


            mMap.addPolyline(polylineOptions);



//            mMap.addPolyline(DirectionConverter.createPolyline(DirectionActivity.this,
//                    latLngArrayList, 5, Color.RED));

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
