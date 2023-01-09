package com.example.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    EditText latitude, longitude;
    Button enter;
    TextView firstCity, secondCity, thirdCity, firstCityTemp, secondCityTemp, thirdCityTemp, firstCityTime, secondCityTime, thirdCityTime, firstCityDate, secondCityDate, thirdCityDate, firstCityWeather, secondCityWeather, thirdCityWeather;
    ImageView firstCityImage, secondCityImage, thirdCityImage;
    String apiKey = "d0e747bf60be98150599d4773814c627";
    JSONObject city1 = new JSONObject();
    JSONObject city2 = new JSONObject();
    JSONObject city3 = new JSONObject();
    int temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        enter = findViewById(R.id.button);
        firstCity = findViewById(R.id.firstCity);
        secondCity = findViewById(R.id.secondCity);
        thirdCity = findViewById(R.id.thirdCity);
        firstCityTemp = findViewById(R.id.firstCityTemp);
        secondCityTemp = findViewById(R.id.secondCityTemp);
        thirdCityTemp = findViewById(R.id.thirdCityTemp);
        firstCityTime = findViewById(R.id.firstCityTime);
        secondCityTime = findViewById(R.id.secondCityTime);
        thirdCityTime = findViewById(R.id.thirdCityTime);
        firstCityDate = findViewById(R.id.firstCityDate);
        secondCityDate = findViewById(R.id.secondCityDate);
        thirdCityDate = findViewById(R.id.thirdCityDate);
        firstCityWeather = findViewById(R.id.firstCityWeather);
        secondCityWeather = findViewById(R.id.secondCityWeather);
        thirdCityWeather = findViewById(R.id.thirdCityWeather);

        firstCityImage = findViewById(R.id.imageView);
        secondCityImage = findViewById(R.id.imageView2);
        thirdCityImage = findViewById(R.id.imageView3);

        //firstCityImage.setImageResource(R.drawable.ic_launcher_background);
        //secondCityImage.setImageResource(R.drawable.ic_launcher_background);
        //thirdCityImage.setImageResource(R.drawable.ic_launcher_background);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonReader().execute();
            }
        });








    }

    private class JsonReader extends AsyncTask<URL, Integer, Void> {
        JSONObject object;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            object = new JSONObject();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected Void doInBackground(URL... urls) {
            int count = urls.length;
            String lat = latitude.getText().toString();
            String lon = longitude.getText().toString();

            String line = "";

            StringBuilder stringBuilder = new StringBuilder();
            JSONObject object = new JSONObject();

            /*
            first location - Portland, ME - Portland Museum of Art
            lat = "43.65412178142164";
            lon = "-70.2625497275448";

            second location - South Brunswick, NJ – SBHS
            lat - "40.37425732388781";
            lon - "-74.56398991571521"

            */
            
            URL url = null;
            try {
                //"https://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=d0e747bf60be98150599d4773814c627"
                url = new URL("https://api.openweathermap.org/data/2.5/find?lat="+lat+"&lon="+lon+"&cnt=3&units=imperial&appid="+apiKey);
                Log.d("URL", url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            URLConnection con = null;
            try {
                assert url != null;
                con = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                assert con != null;
                InputStreamReader input = new InputStreamReader(con.getInputStream());
                BufferedReader reader = new BufferedReader(input);

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                object = new JSONObject(stringBuilder.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("TAG_INFO", object.toString());

            try {
                //name of first city
                Log.d("TAG_INFO_NAME1", object.getJSONArray("list").getJSONObject(0).getString("name"));
                //name of second city
                Log.d("TAG_INFO_NAME2", object.getJSONArray("list").getJSONObject(1).getString("name"));
                //name of third city
                Log.d("TAG_INFO_NAME3", object.getJSONArray("list").getJSONObject(2).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                city1 = object.getJSONArray("list").getJSONObject(0);
                city2 = object.getJSONArray("list").getJSONObject(1);
                city3 = object.getJSONArray("list").getJSONObject(2);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;

        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //setting the city names
            try {
                firstCity.setText(city1.getString("name"));
                secondCity.setText(city2.getString("name"));
                thirdCity.setText(city3.getString("name"));
            } catch (JSONException e) {
                Log.d("TAG", e.toString());
            }

            //setting the city temps
            try {
                firstCityTemp.setText(city1.getJSONObject("main").getString("temp"));
                secondCityTemp.setText(city2.getJSONObject("main").getString("temp"));
                thirdCityTemp.setText(city3.getJSONObject("main").getString("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //setting the pic for weather conditions
            try {
                //if clear
                if (city1.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Clear")) {
                    firstCityImage.setImageResource(R.drawable.clear);
                }
                if (city2.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Clear")) {
                    secondCityImage.setImageResource(R.drawable.clear);
                }
                if (city3.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Clear")) {
                    thirdCityImage.setImageResource(R.drawable.clear);
                }

                //if atmosphere
                if (city1.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Atmosphere")) {
                    firstCityImage.setImageResource(R.drawable.atmosphere);
                }
                if (city2.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Atmosphere")) {
                    secondCityImage.setImageResource(R.drawable.atmosphere);
                }
                if (city3.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Atmosphere")) {
                    thirdCityImage.setImageResource(R.drawable.atmosphere);
                }

                //if cloudy
                if (city1.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Clouds")) {
                    firstCityImage.setImageResource(R.drawable.cloudy);
                }
                if (city2.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Clouds")) {
                    secondCityImage.setImageResource(R.drawable.cloudy);
                }
                if (city3.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Clouds")) {
                    thirdCityImage.setImageResource(R.drawable.cloudy);
                }

                //if drizzle
                if (city1.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Drizzle")) {
                    firstCityImage.setImageResource(R.drawable.drizzle);
                }
                if (city2.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Drizzle")) {
                    secondCityImage.setImageResource(R.drawable.drizzle);
                }
                if (city3.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Drizzle")) {
                    thirdCityImage.setImageResource(R.drawable.drizzle);
                }

                //if rain
                if (city1.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Rain")) {
                    firstCityImage.setImageResource(R.drawable.rain);
                }
                if (city2.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Rain")) {
                    secondCityImage.setImageResource(R.drawable.rain);
                }
                if (city3.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Rain")) {
                    thirdCityImage.setImageResource(R.drawable.rain);
                }

                //if snow
                if (city1.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Snow")) {
                    firstCityImage.setImageResource(R.drawable.snow);
                }
                if (city2.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Cloudy")) {
                    secondCityImage.setImageResource(R.drawable.snow);
                }
                if (city3.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("cloudy")) {
                    thirdCityImage.setImageResource(R.drawable.snow);
                }

                //if thunderstorm
                if (city1.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Thunderstorm")) {
                    firstCityImage.setImageResource(R.drawable.thunderstorm);
                }
                if (city2.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Thunderstorm")) {
                    secondCityImage.setImageResource(R.drawable.thunderstorm);
                }
                if (city3.getJSONArray("weather").getJSONObject(0).getString("main").equalsIgnoreCase("Thunderstorm")) {
                    thirdCityImage.setImageResource(R.drawable.thunderstorm);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //setting the city time and date
            try {
                long time = city1.getInt("dt");
                Log.d("TIME", time+"");
                time = time * 1000L;

                String date = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date (time));

                //date
                firstCityDate.setText(date.substring(0, 10));
                secondCityDate.setText(date.substring(0, 10));
                thirdCityDate.setText(date.substring(0, 10));

                //time
                firstCityTime.setText(date.substring(11));
                secondCityTime.setText(date.substring(11));
                thirdCityTime.setText(date.substring(11));

                Log.d("TIME_NEW", date);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            //setting the weather
            try {
                firstCityWeather.setText(city1.getJSONArray("weather").getJSONObject(0).getString("description"));
                secondCityWeather.setText(city2.getJSONArray("weather").getJSONObject(0).getString("description"));
                thirdCityWeather.setText(city3.getJSONArray("weather").getJSONObject(0).getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

