package com.shubham16598.reportingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView mrecyclerView;
        myCustomAdapter myadapter;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            final JSONObject jsonObject = new JSONObject()
                    .put("type", "select")
                    .put("args", new JSONObject()
                            .put("table", "Complaints")
                            .put("columns", new JSONArray()
                                    .put("*")
                            )
                    );
            String url = "https://data.hyperventilation16.hasura-app.io/v1/query";

            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    //Handle failure
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    // Handle success
                    String res = response.body().string();
                    Log.e("onResponse:  ",res );
                    try {
                        JSONArray jsonarray = new JSONArray(res);
                        JSONObject jsonobj = jsonarray.getJSONObject(1);
                        Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            // To execute the call synchronously
            // try {
            // 	Response response = client.newCall(request).execute();
            // 	String responseString = response.body().string(); // handle response
            // } catch (IOException e) {
            // 	e.printStackTrace(); // handle error
            // }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
