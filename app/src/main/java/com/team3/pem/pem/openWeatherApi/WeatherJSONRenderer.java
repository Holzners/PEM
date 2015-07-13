package com.team3.pem.pem.openWeatherApi;

import android.util.Log;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by Stephan on 28.06.15.
 */
public class WeatherJSONRenderer {

    /**
     * Convert Weather Data JsonMessage to Multiline String with only Data we want to display
     * @param json
     * @return
     */
    public static String renderWeather(JSONObject json){
        try {
            String city = json.getString("name").toUpperCase(Locale.US) + ", " +
                    json.getJSONObject("sys").getString("country");

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            String detailsString = (
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            String currentTemp = (
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            Log.d("Stadt" , city);
            Log.d("Details" , detailsString);
            Log.d("Temp" , currentTemp);
            return city + "\n" + detailsString + "\n" +currentTemp;
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            return "";
        }
    }
}
