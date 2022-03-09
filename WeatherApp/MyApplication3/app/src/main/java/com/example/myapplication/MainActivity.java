package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.example.myapplication.R.id.txtCity;
import static com.example.myapplication.R.id.txtTime;
import static com.example.myapplication.R.id.txtValueHumidity;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    String City = "London";
    String Key = "8a8b693af148736d7fca15ad58b5b4f4";


    public class DwonloadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            String result = "";

            try {
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1){

                    result += (char) data;

                    data = inputStreamReader.read();


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    public  class DownloadIcon extends  AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;


            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream =  httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    TextView txtCity, txtTime, txtValueHumidity,txtTemp;
    ImageView imageView;
    EditText edt;
    Button btn;
    RelativeLayout rlWeather,rlRoot;



    public void Loading(View view) {

        City = edt.getText().toString();
        Log.i("my-debug","city: "+City);

        String url= "https://api.openweathermap.org/data/2.5/weather?q=" + City +"&units=metric&appid=" + Key;
        Log.i("my-debug","url: "+url);


        edt.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        rlWeather.setVisibility(View.VISIBLE);
        rlRoot.setBackgroundColor(Color.parseColor("#E6E6E6"));




        DwonloadJSON dwonloadJSON = new DwonloadJSON();

        try {
            String result = dwonloadJSON.execute(url).get();
            Log.i("my-debug","result: "+result);


            JSONObject jsonObject = new JSONObject(result);

            String temp = jsonObject.getJSONObject("main").getString("temp");

            String humidity = jsonObject.getJSONObject("main").getString("humidity");
            Log.i("my-debug","temp: "+temp);
            Log.i("my-debug","humidity: "+humidity);




            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH).format(new Date(time* 1000));

            txtTime.setText(sTime);
            txtCity.setText(City);
            txtValueHumidity.setText(humidity);
            txtTemp.setText(temp + "°");


            String nameIcon = "10d";
            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
            DownloadIcon downloadIcon = new DownloadIcon();


            String urlIcon = "http://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";
            Bitmap bitmap = downloadIcon.execute(urlIcon).get();
            imageView.setImageBitmap(bitmap);









        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCity = findViewById(R.id.txtCity);
        txtTime = findViewById(R.id.txtTime);
        txtValueHumidity = findViewById(R.id.txtValueHumidity);
        txtTemp = findViewById(R.id.txtValueTemperature);
        imageView = findViewById(R.id.imgIcon);
        btn = findViewById(R.id.btn);
        edt = findViewById(R.id.edt);
        rlWeather = findViewById(R.id.rlWeather);
        rlRoot = findViewById(R.id.rlRoot);




    }
}