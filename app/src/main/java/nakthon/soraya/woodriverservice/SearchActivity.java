package nakthon.soraya.woodriverservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    private EditText editText;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Initial View
        initialView();

        //Create Search View
        createSearchView();



    }   // Main Method

    private void createSearchView() {

        try {

            String urlJSON = "http://woodriverservice.com/Android/getLocation.php";
            String tag = "8JuneV1";

            //Get Data from passengerTABLE
            GetAllData getAllData = new GetAllData(SearchActivity.this);
            getAllData.execute(urlJSON);
            getAllData.progressDialog.dismiss();
            String strJSON = getAllData.get();
            Log.d(tag, "JSON ==> " + strJSON);

//            JSONArray jsonArray = new JSONArray(strJSON);
//            listview_names = new String[jsonArray.length()];
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                listview_names[i] = jsonObject.getString(columnLocationStrings[1]);
//            }   // for
//
//
//            array_sort = new ArrayList<String>(Arrays.asList(listview_names));
//            setListAdapter(new bsAdapter(this));
//
//
//            editText.addTextChangedListener(new TextWatcher() {
//                public void afterTextChanged(Editable s) {
//                    // Abstract Method of TextWatcher Interface.
//                }
//
//                public void beforeTextChanged(CharSequence s,
//                                              int start, int count, int after) {
//                    // Abstract Method of TextWatcher Interface.
//                }
//
//                public void onTextChanged(CharSequence s,
//                                          int start, int before, int count) {
//                    textlength = editText.getText().length();
//                    array_sort.clear();
//                    for (int i = 0; i < listview_names.length; i++) {
//                        if (textlength <= listview_names[i].length()) {
//                            /***
//                             * If you want to highlight the countries which start with
//                             * entered letters then choose this block.
//                             * And comment the below If condition Block
//                             */
//                        /*if(et.getText().toString().equalsIgnoreCase(
//                                (String)
//								listview_names[i].subSequence(0,
//										textlength)))
//						{
//							array_sort.add(listview_names[i]);
//							image_sort.add(listview_images[i]);
//						}*/
//
//                            /***
//                             * If you choose the below block then it will act like a
//                             * Like operator in the Mysql
//                             */
//
//                            if (listview_names[i].toLowerCase().contains(
//                                    editText.getText().toString().toLowerCase().trim())) {
//                                array_sort.add(listview_names[i]);
//                            }
//                        }
//                    }
//                    AppendList(array_sort);
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }   // createSearchView

    private void initialView() {
        editText = (EditText) findViewById(R.id.edtSearch);
        listView = (ListView) findViewById(R.id.livLocation);
    }

}   // Main Class
