package nakthon.soraya.woodriverservice;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends ListActivity {

    private EditText editText;
    private ListView listView;
    private String[] listview_names;
    private ArrayList<String> array_sort;
    private int textlength = 0, Index;
    private boolean aBoolean = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Back Controller
        backController();

        //Create Search View
        createSearchView();


    }   // Main Method

    private void backController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createSearchView() {

//        Synchronize Data Location From JSON
        try {

            String urlJSON = "http://woodriverservice.com/Android/getLocation.php";
            String tag = "8JuneV1";
            editText = (EditText) findViewById(R.id.edtSearch2);
            listView = (ListView) findViewById(android.R.id.list);

            //Get Data from passengerTABLE
            GetAllData getAllData = new GetAllData(SearchActivity.this);
            getAllData.execute(urlJSON);
            getAllData.progressDialog.dismiss();
            String strJSON = getAllData.get();
            Log.d(tag, "JSON ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            listview_names = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listview_names[i] = jsonObject.getString("Name");
            }   // for


            array_sort = new ArrayList<String>(Arrays.asList(listview_names));
            setListAdapter(new bsAdapter(this));


            editText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void beforeTextChanged(CharSequence s,
                                              int start, int count, int after) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void onTextChanged(CharSequence s,
                                          int start, int before, int count) {
                    textlength = editText.getText().length();
                    array_sort.clear();
                    for (int i = 0; i < listview_names.length; i++) {
                        if (textlength <= listview_names[i].length()) {
                            /***
                             * If you want to highlight the countries which start with
                             * entered letters then choose this block.
                             * And comment the below If condition Block
                             */
                        /*if(et.getText().toString().equalsIgnoreCase(
                                (String)
								listview_names[i].subSequence(0,
										textlength)))
						{
							array_sort.add(listview_names[i]);
							image_sort.add(listview_images[i]);
						}*/

                            /***
                             * If you choose the below block then it will act like a
                             * Like operator in the Mysql
                             */

                            if (listview_names[i].toLowerCase().contains(
                                    editText.getText().toString().toLowerCase().trim())) {
                                array_sort.add(listview_names[i]);
                            }
                        }
                    }
                    AppendList(array_sort);
                }
            });

            //Click ListView For Active
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("8JuneV2", "You click ==> " + array_sort.get(i));
                    findDetailPhone(array_sort.get(i));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }   // createSearchView

    private void findDetailPhone(String strLocation) {

        String tag = "8JuneV2";
        String[] columnLocationStrings = new String[]{"id", "Name", "Lat", "Lng"};
        String urlGetLocationWhereName = "http://woodriverservice.com/Android/getLocationWhereName.php";

        try {


            GetDataWhere getDataWhere = new GetDataWhere(SearchActivity.this);
            getDataWhere.execute(columnLocationStrings[1], strLocation,
                    urlGetLocationWhereName);
            String strJSON = getDataWhere.get();
            Log.d(tag, "JSON where ==> " + strJSON);

            String[] locationStrings = new String[columnLocationStrings.length];
            JSONArray jsonArray = new JSONArray(strJSON);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            for (int i = 0; i < columnLocationStrings.length; i++) {
                locationStrings[i] = jsonObject.getString(columnLocationStrings[i]);
                Log.d(tag, "locationString(" + i + ") ==> " + locationStrings[i]);
            }   // for

            //Click ListView Finish
            Intent intent = new Intent(SearchActivity.this, DirectionActivity.class);

            //Result ที่ได้จากการคลิก ListView
            intent.putExtra("Result", locationStrings);

            //Lat, Lng ที่รับมาจาก MapsActivity กำลังส่งต่อไปที่ Direct
            intent.putExtra("Lat", getIntent().getDoubleExtra("Lat", 0));
            intent.putExtra("Lng", getIntent().getDoubleExtra("Lng", 0));

            startActivityForResult(intent, 1000);

        } catch (Exception e) {
            Log.d(tag, "e findDetail ==> " + e.toString());
        }

    }   // findDetail

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String tag = "20JuneV1";

        Log.d(tag, "onActivityResult Working");
        Log.d(tag, "requestCode ==> " + requestCode);
        Log.d(tag, "resultCode ==> " + resultCode);

        // resultCode ==> 0

        if (requestCode == 1000 && resultCode == 1000) {

            Log.d(tag, "onActivityResult ==> OK");
            aBoolean = data.getBooleanExtra("Status", true);
            Log.d(tag, "aBoolean ที่รับได้ ==> " + aBoolean);

        }   // if

    }   // onActivityResult

    @Override
    protected void onResume() {
        super.onResume();


        aBoolean = getIntent().getBooleanExtra("Status", true);

        String tag = "20JuneV1";
        Log.d(tag, "aBoolean From onResume ==> " + aBoolean);


    }

    public void AppendList(ArrayList<String> str) {
        setListAdapter(new bsAdapter(this));
    }

    public class bsAdapter extends BaseAdapter {
        Activity cntx;

        public bsAdapter(Activity context) {
            // TODO Auto-generated constructor stub
            this.cntx = context;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return array_sort.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return array_sort.get(position);
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return array_sort.size();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = null;

            LayoutInflater inflater = cntx.getLayoutInflater();
            row = inflater.inflate(R.layout.search_list_item, null);

            TextView tv = (TextView) row.findViewById(R.id.title);

            tv.setText(array_sort.get(position));

            return row;
        }
    }    //bsAdapter


}   // Main Class
