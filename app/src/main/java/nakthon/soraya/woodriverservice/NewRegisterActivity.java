package nakthon.soraya.woodriverservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewRegisterActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        //Initial View
        initialView();

        //Button Controller
        buttonController();


    }   // Main Method

    private void buttonController() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameString = nameEditText.getText().toString().trim();
                String phoneString = phoneEditText.getText().toString().trim();

                if (nameString.equals("") || phoneString.equals("")) {
                    Toast.makeText(NewRegisterActivity.this, "Please Fill All Blank",
                            Toast.LENGTH_SHORT).show();
                } else {
                    addSQLiteAndServer(nameString, phoneString);
                }

            }
        });
    }

    private void addSQLiteAndServer(String nameString, String phoneString) {
        try {

            //Add Value to Server
            AddPassenger addPassenger = new AddPassenger(NewRegisterActivity.this);
            addPassenger.execute(nameString, phoneString);
            if (Boolean.parseBoolean(addPassenger.get())) {
                //Add Value to SQLite
                MyManage myManage = new MyManage(NewRegisterActivity.this);
                myManage.addValueToSQLite(nameString, phoneString);
                finish();
            } else {
                Toast.makeText(NewRegisterActivity.this,
                        "Error Cannot Upload Value", Toast.LENGTH_SHORT).show();
            }




        } catch (Exception e) {
            Log.d("22JuneV1", "e addSQLite ==> " + e.toString());
        }
    }

    private void initialView() {
        nameEditText = (EditText) findViewById(R.id.edtName);
        phoneEditText = (EditText) findViewById(R.id.edtPhone);
        button = (Button) findViewById(R.id.btnSingUp);
    }

}   // Main Class
