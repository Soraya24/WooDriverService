package nakthon.soraya.woodriverservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by masterUNG on 6/21/2017 AD.
 */

public class AddLocation extends AsyncTask<String, Void, String> {

    private static final String urlPHP = "http://woodriverservice.com/Android/addLocation.php";
    private Context context;

    public AddLocation(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isAdd", "true")
                    .add("Name", strings[0])
                    .add("Lat", strings[1])
                    .add("Lng", strings[2])
                    .build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(urlPHP).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            Log.d("21JuneV1", "e doIn ==> " + e.toString());
            return null;
        }


    }
}   // Class
