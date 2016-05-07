package org.vzw.beta.homestation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.tools.MyAsyncTask;
import org.vzw.beta.homestation.tools.PreferencesHelper;
import org.vzw.beta.homestation.tools.Utils;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by user109 on 21/03/2016.
 */
public class WeatherFragment extends Fragment {

    private ImageView mainImage;
    private TextView mainCityText;
    private TextView mainDescText;
    private TextView mainTempText;
    private ImageButton btnSync;
    private ImageButton btnSettings;
    private EditText cityEditText;
    private TextView weatherDay1Title;
    private TextView weatherDay1Image;
    private TextView weatherDay1Desc;
    private TextView weatherDay2Title;
    private TextView weatherDay2Image;
    private TextView weatherDay2Desc;
    private TextView weatherDay3Title;
    private TextView weatherDay3Image;
    private TextView weatherDay3Desc;
    private TextView weatherDay4Title;
    private TextView weatherDay4Image;
    private TextView weatherDay4Desc;
    private TextView weatherDay5Title;
    private TextView weatherDay5Image;
    private TextView weatherDay5Desc;
    private TextView weatherDay6Title;
    private TextView weatherDay6Image;
    private TextView weatherDay6Desc;
    private static JSONObject myMainJsonObject;
    private static JSONObject mySecondJsonObject;
    private LinearLayout layoutDay1;


    private TextView weatherIcon;


    private String currentCity = "Antwerpen";
    private String currentUnits = "&units=metric";
    private String weatherDescJSON = "description";
    private String weatherMainDescJSON = "main";
    private String weatherIconJSON = "icon";
    private String mainTempJSON = "temp";
    private String mainHumidityJSON = "humidity";
    private String mainMinTempJSON = "temp_min";
    private String mainMaxTempJSON = "temp_max";
    private String windSpeedJSON = "speed";
    private String cityNameJSON = "name";
    private long sunriseJSON;
    private long sunsetJSON;


    private static int MY_IDENTIFICATION = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.weather_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //mainImage = (ImageView) view.findViewById(R.id.weather_main_image);
        mainCityText = (TextView) view.findViewById(R.id.weather_city);
        mainDescText = (TextView) view.findViewById(R.id.weather_main_description);
        mainTempText = (TextView) view.findViewById(R.id.weather_main_temperature);
        weatherDay1Desc = (TextView) view.findViewById(R.id.weather_day1_desc);
        weatherDay1Title = (TextView) view.findViewById(R.id.weather_day1_title);
        weatherDay1Image = (TextView) view.findViewById(R.id.weather_day1_image);
        weatherDay2Desc = (TextView) view.findViewById(R.id.weather_day2_desc);
        weatherDay2Title = (TextView) view.findViewById(R.id.weather_day2_title);
        weatherDay2Image = (TextView) view.findViewById(R.id.weather_day2_image);
        weatherDay3Desc = (TextView) view.findViewById(R.id.weather_day3_desc);
        weatherDay3Title = (TextView) view.findViewById(R.id.weather_day3_title);
        weatherDay3Image = (TextView) view.findViewById(R.id.weather_day3_image);
        weatherDay4Desc = (TextView) view.findViewById(R.id.weather_day4_desc);
        weatherDay4Title = (TextView) view.findViewById(R.id.weather_day4_title);
        weatherDay4Image = (TextView) view.findViewById(R.id.weather_day4_image);
        weatherDay5Desc = (TextView) view.findViewById(R.id.weather_day5_desc);
        weatherDay5Title = (TextView) view.findViewById(R.id.weather_day5_title);
        weatherDay5Image = (TextView) view.findViewById(R.id.weather_day5_image);
        weatherDay6Desc = (TextView) view.findViewById(R.id.weather_day6_desc);
        weatherDay6Title = (TextView) view.findViewById(R.id.weather_day6_title);
        weatherDay6Image = (TextView) view.findViewById(R.id.weather_day6_image);
        layoutDay1=(LinearLayout)view.findViewById(R.id.weather_linear_layout);


        btnSync = (ImageButton) view.findViewById(R.id.weather_sync);
        btnSettings = (ImageButton) view.findViewById(R.id.weather_settings);
        weatherIcon = (TextView) view.findViewById(R.id.weather_text_Icon);
        callWebServices(currentCity);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebServices(currentCity);
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSettings();
            }
        });
        layoutDay1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setNewIntent(LiveWeatherActivity.class);
                return true;
            }
        });

        return view;
    }
    public void setNewIntent(java.lang.Class intentClass) {
        Intent intent = new Intent(getActivity(), intentClass);
        startActivity(intent);
    }
    public  void callWebServices(String cityName) {
        try {
            if (cityName.contains(" ")) {
                cityName = cityName.replace(" ", "%20");
            }
            String mainResponse = new MyAsyncTask().execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + currentUnits + "&appid=a6e5e95034354e78e79231ac736415a8").get();
            String secondResponse = new MyAsyncTask().execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityName + currentUnits + "&cnt=7&appid=a6e5e95034354e78e79231ac736415a8").get();
            myMainJsonObject = parseJSON(mainResponse);
            mySecondJsonObject = parseJSON(secondResponse);
            setMainWeatherApp(myMainJsonObject);
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
            mainDescText.setText(weatherObj.getString(weatherDescJSON) + "\n" + "Humidity: " + mainObj.getString(mainHumidityJSON) + "%\n" + "Wind: " + jsonObject.getJSONObject("wind").getString(windSpeedJSON) + " m/s");
            mainTempText.setText(mainObj.getInt(mainTempJSON) + "º");
            mainCityText.setText(jsonObject.getString(cityNameJSON));
            sunriseJSON=jsonObject.getJSONObject("sys").getInt("sunrise");
            sunsetJSON=jsonObject.getJSONObject("sys").getInt("sunset");
            PreferencesHelper.COUNTRY_CODE=jsonObject.getJSONObject("sys").getString("country");

            //this is another way to set the icons
           /* if (description.equals("clear sky")) {
                mainImage.setImageResource(R.drawable.weather_sunny);
            } else if (description.equals("few clouds")) {
                mainImage.setImageResource(R.drawable.weather_partially_clouded);
            } else if (description.equals("scattered clouds")) {
                mainImage.setImageResource(R.drawable.weather_scattered_clouds);
            } else if (description.equals("broken clouds")) {
                mainImage.setImageResource(R.drawable.weather_clouded);
            } else if (description.equals("shower rain")) {
                mainImage.setImageResource(R.drawable.weather_rain_shower);
            } else if (description.equals("light rain")) {
                mainImage.setImageResource(R.drawable.weather_rain);
            } else if (description.equals("rain")) {
                mainImage.setImageResource(R.drawable.heavy_rain);
            } else if (description.equals("thunderstorm")) {
                mainImage.setImageResource(R.drawable.weather_thunderstorm_rain);
            } else if (description.equals("snow")) {
                mainImage.setImageResource(R.drawable.weather_snow);
            } else if (description.equals("mist")) {
                mainImage.setImageResource(R.drawable.weather_fog);
            }*/
            MY_IDENTIFICATION = 0;
            setUpWeatherIcon(weatherObj.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000, null);

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

    }

    private void setUpWeatherIcon(int actualId, long sunrise, long sunset, TextView weatherImage) {
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital_weather.ttf");
        //txtV.setTypeface(face);
        if (MY_IDENTIFICATION == 0) {
            weatherIcon.setTypeface(face);
        } else if (MY_IDENTIFICATION == 1) {
            weatherImage.setTypeface(face);
        }
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = WeatherFragment.this.getString(R.string.weather_sunny);
            } else if(currentTime < sunrise && currentTime > sunset) {
                icon = WeatherFragment.this.getString(R.string.weather_clear_night);
            }else{
                icon = WeatherFragment.this.getString(R.string.weather_sunny);
            }
        } else {
            switch (id) {
                case 2:
                    icon = WeatherFragment.this.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = WeatherFragment.this.getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = WeatherFragment.this.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = WeatherFragment.this.getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = WeatherFragment.this.getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = WeatherFragment.this.getString(R.string.weather_rainy);
                    break;
            }
        }
        if (MY_IDENTIFICATION == 0) {
            weatherIcon.setText(icon);
            Utils.weatherIcon=icon;
            setLowerWeatherInfo(mySecondJsonObject);
        } else if (MY_IDENTIFICATION == 1) {
            weatherImage.setText(icon);
        }
    }


    private void showDialogSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //DialogWithBlurredBackgroundLauncher dialogWithBlurredBackgroundLauncher=new DialogWithBlurredBackgroundLauncher(this);


        final View view = inflater.inflate(R.layout.weather_settings_layout, null);
        final RadioButton rdioBttonCelsius = (RadioButton) view.findViewById(R.id.rdioButton_Weather_Celsius);
        final RadioButton rdioBttonKelvin = (RadioButton) view.findViewById(R.id.rdioButton_Weather_Kelvin);
        final RadioButton rdioBttonFahrenheit = (RadioButton) view.findViewById(R.id.rdioButton_Weather_Fahrenheit);
        cityEditText = (EditText) view.findViewById(R.id.weather_settings_city_editText);
        rdioBttonCelsius.setChecked(true);


        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                currentCity = cityEditText.getText().toString();

                if (rdioBttonCelsius.isChecked()) {
                    currentUnits = "&units=metric";
                } else if (rdioBttonFahrenheit.isChecked()) {
                    currentUnits = "&units=imperial";
                } else if (rdioBttonKelvin.isChecked()) {
                    currentUnits = "";
                }
                callWebServices(currentCity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setIcon(R.drawable.ic_action_flag);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    public void setLowerWeatherInfo(JSONObject jsonObject) {
        int epochTime;
        MY_IDENTIFICATION = 1;
        try {
            for (int i = 1; i < 7; i++) {
                JSONObject currenWeatherObj = (JSONObject) jsonObject.getJSONArray("list").getJSONObject(i);
                JSONObject currentTempObj = currenWeatherObj.getJSONObject("temp");
                JSONArray currentWeatherArray = currenWeatherObj.getJSONArray("weather");
                String[] strDays;


                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);

                switch (day)
                {
                    case Calendar.SUNDAY:
                        strDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thusday", "Friday", "Saturday"};
                        break;
                    case Calendar.MONDAY:
                        strDays = new String[]{"Monday","Tuesday", "Wednesday", "Thusday", "Friday", "Saturday", "Sunday" };
                        break;
                    case Calendar.TUESDAY:
                        strDays = new String[]{"Tuesday", "Wednesday", "Thusday", "Friday", "Saturday", "Sunday", "Monday"};
                        break;
                    case Calendar.WEDNESDAY:
                        strDays = new String[]{"Wednesday","Thusday", "Friday", "Saturday", "Sunday", "Monday", "Tuesday" };
                        break;
                    case Calendar.THURSDAY:
                        strDays = new String[]{"Thusday","Friday", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday" };
                        break;
                    case Calendar.FRIDAY:
                        strDays = new String[]{"Friday","Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday"};
                        break;
                    default:
                        strDays = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday", "Friday"};
                        break;
                }

                String myDateResult = strDays[i].toUpperCase().substring(0, 3);
                String myTempDesc = currentTempObj.getInt("max") + "º/" + currentTempObj.getInt("min") + "º";


                if (i == 1) {
                    weatherDay1Title.setText(myDateResult);
                    setUpWeatherIcon(currentWeatherArray.getJSONObject(0).getInt("id"), sunriseJSON, sunsetJSON, weatherDay1Image);
                    weatherDay1Desc.setText(myTempDesc);
                } else if (i == 2) {

                    weatherDay2Title.setText(myDateResult);
                    weatherDay2Desc.setText(myTempDesc);
                    setUpWeatherIcon(currentWeatherArray.getJSONObject(0).getInt("id"), sunriseJSON, sunsetJSON, weatherDay2Image);
                }
                if (i == 3) {
                    weatherDay3Title.setText(myDateResult);
                    weatherDay3Desc.setText(myTempDesc);
                    setUpWeatherIcon(currentWeatherArray.getJSONObject(0).getInt("id"), sunriseJSON, sunsetJSON, weatherDay3Image);
                }
                if (i == 4) {
                    weatherDay4Title.setText(myDateResult);
                    weatherDay4Desc.setText(myTempDesc);
                    setUpWeatherIcon(currentWeatherArray.getJSONObject(0).getInt("id"), sunriseJSON, sunsetJSON, weatherDay4Image);
                }
                if (i == 5) {
                    weatherDay5Title.setText(myDateResult);
                    weatherDay5Desc.setText(myTempDesc);
                    setUpWeatherIcon(currentWeatherArray.getJSONObject(0).getInt("id"), sunriseJSON, sunsetJSON, weatherDay5Image);
                }
                if (i == 6) {
                    weatherDay6Title.setText(myDateResult);
                    weatherDay6Desc.setText(myTempDesc);
                    setUpWeatherIcon(currentWeatherArray.getJSONObject(0).getInt("id"), sunriseJSON, sunsetJSON, weatherDay6Image);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
