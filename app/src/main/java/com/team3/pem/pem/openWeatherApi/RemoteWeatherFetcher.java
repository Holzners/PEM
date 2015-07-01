package com.team3.pem.pem.openWeatherApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.team3.pem.pem.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by Stephan on 28.06.15.
 */
public class RemoteWeatherFetcher {

    private static final String CITY_KEY = "city_key";

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    public static JSONObject getJSON(Context context) {

        String city = getCurrentLocation(context);

        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_api_key));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentLocation(Context context) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
         Location location = null;
        SharedPreferences prefs = context.getSharedPreferences("com.team3.pem.pem",Context.MODE_PRIVATE);
        String city = prefs.getString(CITY_KEY, "Munich");
        if (locationManager != null) {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager
                            .getLastKnownLocation(provider);
        }

        if(location != null) {
            List<Address> list = null;
            try {
                list = gcd.getFromLocation(location
                        .getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (list != null & list.size() > 0) {
                Address address = list.get(0);
                city = address.getLocality();
            }
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CITY_KEY,city);
        editor.commit();
        return city;
    }

}
