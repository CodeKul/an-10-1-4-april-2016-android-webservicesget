package com.codekul.webserviceget;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyTask().execute("");
    }

    private OkHttpClient initHttpClient(){

        OkHttpClient httpClient = new OkHttpClient();

        return  httpClient;
    }

    private Request buildRequest(String url){

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return request;
    }

    private String sendRequestAndGetResponse(Request request){

        String responseBody = "";
        try {

            Response response = initHttpClient().newCall(request).execute();

            if(response != null){

                responseBody = response.body().string();

                //2xx - success , request reached to server and got response back
                //3xx - redirection, request recaheched to server and redirected to other server
                //4xx - bad request, request not reached to server due to problem in request data
                //5xx - internal server error, request reached and error is generated during processing

                Log.i("@codekul","Response Code - "+response.code());

                if(response.code() == 200){

                    JSONArray jsonRootArray = new JSONArray(responseBody);

                    for(int i = 0 ; i < jsonRootArray.length() ;i++){

                        JSONObject jsonObject = jsonRootArray.getJSONObject(i);
                        String fullName = jsonObject.getString("fullName");
                        String email = jsonObject.getString("eMail");
                        String mobileNo = jsonObject.getString("mobileNo");

                        Log.i("@codekul","--------------");
                        Log.i("@codekul","Fn - "+fullName +" Em - "+email+" M - "+mobileNo);

                        JSONArray arrayTechnologies = new JSONArray(jsonObject.getString("technologies"));
                        for(int j = 0 ; j < arrayTechnologies.length(); j++){

                            String technology = (String) arrayTechnologies.get(j);
                            Log.i("@codekul","Technology - "+technology);
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return  responseBody;
    }

    private class MyTask extends AsyncTask<String,Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String responseJson = sendRequestAndGetResponse(buildRequest(params[0]));

            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
