package com.shubham16598.reportingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


public class MainActivity extends AppCompatActivity {
    EditText mobile,problem,detail,date;
    Button submit,request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mobile =(EditText)findViewById(R.id.mobile);
        problem =(EditText)findViewById(R.id.problem);
        detail =(EditText)findViewById(R.id.Details);
        date =(EditText)findViewById(R.id.date);
        submit = (Button)findViewById(R.id.submit);
        request = (Button)findViewById(R.id.list);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://data.hyperventilation16.hasura-app.io/v1/query";

                try {
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    JSONObject jsonObject = new JSONObject()
                            .put("type", "insert")
                            .put("args", new JSONObject()
                                    .put("table", "Complaints")
                                    .put("objects", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Mobile", mobile.getText())
                                                    .put("Problem", problem.getText())
                                                    .put("Details", detail.getText())
                                                    .put("Date", date.getText())
                                            )
                                    )
                            );

                    RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                    Request request = new Request.Builder()
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
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Log.e("onResponse:  ", response.body().string());
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
        });

    }
}
