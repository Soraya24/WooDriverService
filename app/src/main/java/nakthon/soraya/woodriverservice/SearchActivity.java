package nakthon.soraya.woodriverservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    }   // Main Method

    private void initialView() {
        editText = (EditText) findViewById(R.id.edtSearch);
        listView = (ListView) findViewById(R.id.livLocation);
    }

}   // Main Class
