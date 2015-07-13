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
 * Class to get Weather Data from OpenWeatherMap Api
 */
public class RemoteWeatherFetcher {

    private static final String CITY_KEY = "city_key";

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    /**
     * sends HttpRequest to API an parse Response to Json
     * @param context
     * @return
     */
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

    /**
     * Returns CurrentLocation as City Name
     * If LocationManager has no last Location, Shared Preferences chick if Location was found earlier
     * Default loc is Munich
     * @param context
     * @return
     */
    public static String getCurrentLocation(Context context) {
        //Init LocationManager and Geocoder
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Location location = null;
        //Get Shared Prefs for City Key
        SharedPreferences prefs = context.getSharedPreferences("com.team3.pem.pem",Context.MODE_PRIVATE);
        String city = prefs.getString(CITY_KEY, "Munich");

        if (locationManager != null) {
            //Check if Location Manager can find Location without Location Request
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager
                            .getLastKnownLocation(provider);
        }
            // if Location found get City from Location
        if(location != null) {
            List<Address> list = null;
            try {
                list = gcd.getFromLocation(location
                        .getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                city = address.getLocality();
            }
        }
        //Store City in shared prefs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CITY_KEY,city);
        editor.commit();
        return city;
    }

}
