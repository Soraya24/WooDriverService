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
 * Created by masterUNG on 6/23/2017 AD.
 */

public class AddPureJob extends AsyncTask<String, Void, String>{

    private Context context;
    private static final String urlPHP = "http://woodriverservice.com/Android/addPureJob.php";

    public AddPureJob(Context context) {
        this.context = context;
    }


    @Override
    protected String doInBackground(String... strings) {

        try {

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isAdd", "true")
                    .add("id_Passenger", strings[0])
                    .add("TimeWork", strings[1])
                    .add("LatStart", strings[2])
                    .add("LngStart", strings[3])
                    .add("Job", strings[4])
                    .build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(urlPHP).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            Log.d("23JuneV1", "e doin ==> " + e.toString());
            return null;
        }


    }
}   // Main Class
