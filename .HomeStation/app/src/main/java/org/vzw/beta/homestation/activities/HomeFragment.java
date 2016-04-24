package org.vzw.beta.homestation.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONObject;
import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.tools.MyAsyncTask;
import org.vzw.beta.homestation.tools.PreferencesHelper;

import java.util.Date;

/**
 * Created by Jorciney on 23/04/2016.
 */
public class HomeFragment extends Fragment {

    private static PieChart pieChart;
    private TextView homeTemperatureIcon;
    private TextView homeTemperatureText;
    private String mainTempJSON = "temp";
    private String currentUnits = "&units=metric";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        homeTemperatureIcon = (TextView) view.findViewById(R.id.home_weather_widget_image);
        homeTemperatureText = (TextView) view.findViewById(R.id.home_weather_widget_TempText);
        pieChart = (PieChart) view.findViewById(R.id.home_overview_chart_pie);



        callWeatherWebServices("Deurne");

        pieChart=MainActivity.setUpChart(pieChart);
        return view;
    }


    public void callWeatherWebServices(String cityName) {
        try {
            if (cityName.contains(" ")) {
                cityName = cityName.replace(" ", "%20");
            }
            String mainResponse = new MyAsyncTask().execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + currentUnits + "&appid=a6e5e95034354e78e79231ac736415a8").get();
            MainActivity.myMainJsonObject = parseJSON(mainResponse);
            setMainWeatherApp(MainActivity.myMainJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject parseJSON(String response) {
        try {
            return new JSONObject(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMainWeatherApp(JSONObject jsonObject) {
        try {
            JSONObject weatherObj = (JSONObject) jsonObject.getJSONArray("weather").get(0);
            JSONObject mainObj = jsonObject.getJSONObject("main");
            //mainDescText.setText(weatherObj.getString(weatherDescJSON) + "\n" + "Humidity: " + mainObj.getString(mainHumidityJSON) + "%\n" + "Wind: " + jsonObject.getJSONObject("wind").getString(windSpeedJSON) + " m/s");
            homeTemperatureText.setText(mainObj.getInt(mainTempJSON) + "º");
            PreferencesHelper.COUNTRY_CODE = jsonObject.getJSONObject("sys").getString("country");

            setUpWeatherIcon(weatherObj.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000, null);

        } catch (
                Exception e
                ) {
            e.printStackTrace();
        }
    }
    /**
     * The icon will be setup depending on the weather(json object)
     */
    private void setUpWeatherIcon(int actualId, long sunrise, long sunset, TextView weatherImage) {
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital_weather.ttf");
        homeTemperatureIcon.setTypeface(face);

        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = this.getString(R.string.weather_sunny);
            } else {
                icon = this.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = this.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = this.getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = this.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = this.getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = this.getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = this.getString(R.string.weather_rainy);
                    break;
            }
        }
        homeTemperatureIcon.setText(icon);
    }



}
