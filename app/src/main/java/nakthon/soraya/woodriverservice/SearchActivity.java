package nakthon.soraya.woodriverservice;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Create Search View
        createSearchView();


    }   // Main Method

    private void createSearchView() {

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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }   // createSearchView


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
