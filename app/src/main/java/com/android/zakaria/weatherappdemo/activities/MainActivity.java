package com.android.zakaria.weatherappdemo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zakaria.weatherappdemo.adapters.HourlyHorizontalAdapter;
import com.android.zakaria.weatherappdemo.adapters.WeeklyHorizontalAdapter;
import com.android.zakaria.weatherappdemo.R;
import com.android.zakaria.weatherappdemo.connections.NetConnectionDetector;
import com.android.zakaria.weatherappdemo.models.RetrofitClient;
import com.android.zakaria.weatherappdemo.models.WeatherApi;
import com.android.zakaria.weatherappdemo.pojos.Weather;
import com.android.zakaria.weatherappdemo.shared_preferences.MySharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.zakaria.weatherappdemo.shared_preferences.MySharedPreferences.getMyPreferences;

public class MainActivity extends AppCompatActivity {

    private TextView currentTemp, currentHumidity, currentDate, currentSummary, more;
    private ImageView currentImgIcon;
    private ListView listView;
    private RadioGroup radioGroup;
    private boolean isFahrenheit = true;
    private LinearLayout linearBack;
    ;

    private static final String URL = "forecast/44af292051fdb50297c330b7e270abc9/";
    private WeatherApi weatherApi;
    private String customUrl;
    private String iconImgString;
    private static String area;
    private int celsius;

    private static final int TIME_DELAY = 2000;
    private static long backPressed;
    private NetConnectionDetector netConnectionDetector;

    private ArrayList<String> hourlyTimeList;
    private ArrayList<String> hourlyPercentList;
    private ArrayList<String> hourlyTempList;
    private ArrayList<String> hourlyIconList;

    private ArrayList<String> weeklyDayList;
    private ArrayList<String> weeklyPercentList;
    private ArrayList<String> weeklyHighTempList;
    private ArrayList<String> weeklyLowTempList;
    private ArrayList<String> weeklyIconList;
    private List<String> currentlyMoreArrayList;

    private String currentTempStr;
    private String currentHumidityStr;
    private String currentDateStr;
    private String currentSummaryStr;
    private MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDate = findViewById(R.id.currentDate);
        currentHumidity = findViewById(R.id.currentHumidity);
        currentTemp = findViewById(R.id.currentTemp);
        currentSummary = findViewById(R.id.currentSummary);
        currentImgIcon = findViewById(R.id.currentImgIcon);
        linearBack = findViewById(R.id.linearBack);

        more = findViewById(R.id.moreTV);
        more.setPaintFlags(more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mySharedPreferences = getMyPreferences(this);

        isFahrenheit = mySharedPreferences.getIsTempType();
        defaultUrlDataLoad();

        netConnectionDetector = new NetConnectionDetector(this);
        if (!netConnectionDetector.isConnected() && !isSharedPreferencesHaveData()) {
            Toast.makeText(MainActivity.this, "Network not found", Toast.LENGTH_LONG).show();
        } else if (!netConnectionDetector.isConnected() && isSharedPreferencesHaveData()) {
            getSavedDataFromSharedPreferences();

            currentTemp.setText(currentTempStr + "\u2109");
            currentHumidity.setText("Humidity: " + currentHumidityStr + "%");
            currentDate.setText(currentDateStr);
            currentSummary.setText(currentSummaryStr);
            currentImgIcon.setImageResource(R.drawable.cloudy);
        }
    }

    public void setSelectedArea(String latitudeLongitude) {
        area = latitudeLongitude;
    }

    public static String getSelectedArea() {
        return area;
    }

    public void getHourlyRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView hourLyRecyclerView = findViewById(R.id.hourLyRecyclerViewId);
        hourLyRecyclerView.setLayoutManager(linearLayoutManager);

        HourlyHorizontalAdapter hourlyHorizontalAdapter = new HourlyHorizontalAdapter(this, hourlyTimeList, hourlyPercentList, hourlyTempList, hourlyIconList);
        hourLyRecyclerView.setAdapter(hourlyHorizontalAdapter);
    }

    public void getWeeklyRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView weeklyRecyclerView = findViewById(R.id.weeklyRecyclerViewId);
        weeklyRecyclerView.setLayoutManager(linearLayoutManager);
        WeeklyHorizontalAdapter weeklyHorizontalAdapter = new WeeklyHorizontalAdapter(this, weeklyDayList, weeklyPercentList, weeklyHighTempList, weeklyLowTempList, weeklyIconList);
        weeklyRecyclerView.setAdapter(weeklyHorizontalAdapter);
    }

    public void getWeatherData(String selectedLatitudeLongitude) {
        hourlyTimeList = new ArrayList<>();
        hourlyPercentList = new ArrayList<>();
        hourlyTempList = new ArrayList<>();
        hourlyIconList = new ArrayList<>();

        weeklyDayList = new ArrayList<>();
        weeklyPercentList = new ArrayList<>();
        weeklyHighTempList = new ArrayList<>();
        weeklyLowTempList = new ArrayList<>();
        weeklyIconList = new ArrayList<>();
        currentlyMoreArrayList = new ArrayList<>();

        weatherApi = RetrofitClient.getRetrofitClient().create(WeatherApi.class);
        customUrl = URL + selectedLatitudeLongitude;

        Call<Weather> callWeatherCondition = weatherApi.getWeather(customUrl);
        callWeatherCondition.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather weather = response.body();

                if (isFahrenheit) {
                    currentTempStr = doubleToIntToString(weather.getCurrently().getTemperature());
                    currentTemp.setText(currentTempStr + "\u2109");
                } else {
                    currentTempStr = doubleToIntToString(((weather.getCurrently().getTemperature() - 32) / 1.8));
                    currentTemp.setText(currentTempStr + "\u2103");
                }

                currentHumidityStr = doubleToIntToString((weather.getCurrently().getHumidity() * 100));
                currentHumidity.setText("Humidity: " + currentHumidityStr + "%");
                currentDateStr = getCurrentDayAndDate(weather.getCurrently().getTime());
                currentDate.setText(currentDateStr);
                currentSummaryStr = weather.getCurrently().getSummary();
                currentSummary.setText(currentSummaryStr);


                currentlyMoreArrayList.add(/*"Dew Point: " + */weather.getCurrently().getDewPoint().toString());
                currentlyMoreArrayList.add(/*"Wind Speed: " + */weather.getCurrently().getWindSpeed().toString());
                currentlyMoreArrayList.add(/*"Wind Bearing: " + */weather.getCurrently().getWindBearing().toString());
                currentlyMoreArrayList.add(/*"Cloud Cover" + */weather.getCurrently().getCloudCover().toString());
                currentlyMoreArrayList.add(/*"Pressure: " + */weather.getCurrently().getPressure().toString());
                currentlyMoreArrayList.add(/*"Ozone: " + */weather.getCurrently().getOzone().toString());

                for (int i = 0; i < 24; i++) {
                    hourlyTimeList.add(getCurrentTime(weather.getHourly().getData().get(i).getTime()));
                    hourlyPercentList.add(doubleToIntToString(weather.getHourly().getData().get(i).getHumidity() * 100) + "%");

                    if (isFahrenheit) {
                        hourlyTempList.add(doubleToIntToString(weather.getHourly().getData().get(i).getTemperature()) + "\u2109");
                    } else {
                        hourlyTempList.add(doubleToIntToString((weather.getHourly().getData().get(i).getTemperature() - 32) / 1.8) + "\u2103");
                    }
                    hourlyIconList.add(String.valueOf(R.drawable.weekly));
                }


                for (int i = 0; i < 7; i++) {
                    weeklyDayList.add(getCurrentDayName(weather.getDaily().getData().get(i).getTime()));
                    weeklyPercentList.add(doubleToIntToString(weather.getDaily().getData().get(i).getHumidity() * 100) + "%");
                    if (isFahrenheit) {
                        weeklyHighTempList.add(doubleToIntToString(weather.getDaily().getData().get(i).getTemperatureHigh()) + "\u2109");
                        weeklyLowTempList.add(doubleToIntToString(weather.getDaily().getData().get(i).getTemperatureLow()) + "\u2109");
                    } else {
                        weeklyHighTempList.add(doubleToIntToString((weather.getDaily().getData().get(i).getTemperatureHigh() - 32) / 1.8) + "\u2103");
                        weeklyLowTempList.add(doubleToIntToString((weather.getDaily().getData().get(i).getTemperatureLow() - 32) / 1.8) + "\u2103");
                    }
                    weeklyIconList.add(String.valueOf(R.drawable.weekly));
                }

                iconImgString = weather.getCurrently().getIcon();
                getCurrentIcon();

                saveDataToSharedPreferences();
                getHourlyRecyclerView();
                getWeeklyRecyclerView();
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Internet connection needed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCurrentIcon() {
        if (iconImgString.equals("clear-day")) {
            currentImgIcon.setImageResource(R.drawable.clear_day);
            linearBack.setBackgroundResource(R.drawable.bg_img1);
        } else if (iconImgString.equals("clear-night")) {
            currentImgIcon.setImageResource(R.drawable.clear_night);
            linearBack.setBackgroundResource(R.drawable.bd_im);
        } else if (iconImgString.equals("rain")) {
            currentImgIcon.setImageResource(R.drawable.rain_strom);
            linearBack.setBackgroundResource(R.drawable.bg_image);
        } else if (iconImgString.equals("fog")) {
            currentImgIcon.setImageResource(R.drawable.fog);
            linearBack.setBackgroundResource(R.drawable.bg_image);
        } else if (iconImgString.equals("cloudy")) {
            currentImgIcon.setImageResource(R.drawable.cloudy);
            linearBack.setBackgroundResource(R.drawable.bg_image);
        } else if (iconImgString.equals("partly-cloudy-night")) {
            currentImgIcon.setImageResource(R.drawable.partly_cloudy_night);
            linearBack.setBackgroundResource(R.drawable.bd_im);
        } else if (iconImgString.equals("partly-cloudy-day")) {
            currentImgIcon.setImageResource(R.drawable.partly_cloudy_day);
            linearBack.setBackgroundResource(R.drawable.bg_image);
        } else if (iconImgString.equals("snow")) {
            currentImgIcon.setImageResource(R.drawable.snow);
            linearBack.setBackgroundResource(R.drawable.bg_image);
        } else if (iconImgString.equals("sleet")) {
            currentImgIcon.setImageResource(R.drawable.sleet);
            linearBack.setBackgroundResource(R.drawable.bg_image);
        } else if (iconImgString.equals("wind")) {
            currentImgIcon.setImageResource(R.drawable.wind);
        } else {
            currentImgIcon.setImageResource(R.drawable.cloudy);
            linearBack.setBackgroundResource(R.drawable.bg_image);
        }
    }

    public String convertToCelsius(double fahrenheit) {
        fahrenheit = (fahrenheit - 32) / 1.8;
        return String.valueOf(fahrenheit);
    }

    public void saveDataToSharedPreferences() {
        mySharedPreferences.setCurrentTemp(currentTempStr);
        mySharedPreferences.setCurrentHumidity(currentHumidityStr);
        mySharedPreferences.setCurrentDate(currentDateStr);
        mySharedPreferences.setCurrentSummary(currentSummaryStr);
        mySharedPreferences.setIsTempType(isFahrenheit);
    }

    public void getSavedDataFromSharedPreferences() {
        currentTempStr = mySharedPreferences.getCurrentTemp();
        currentHumidityStr = mySharedPreferences.getCurrentHumidity();
        currentDateStr = mySharedPreferences.getCurrentDate();
        currentSummaryStr = mySharedPreferences.getCurrentSummary();
        isFahrenheit = mySharedPreferences.getIsTempType();
    }

    public String getCurrentDayAndDate(long unixDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd");
        String currentDate = simpleDateFormat.format(new Date(unixDate * 1000L));
        return currentDate;
    }

    public String getCurrentDayName(long unixDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
        String currentDate = simpleDateFormat.format(new Date(unixDate * 1000L));
        return currentDate.toUpperCase();
    }

    public String getCurrentTime(long unixDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String currentDate = simpleDateFormat.format(new Date(unixDate * 1000L));
        return currentDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.country_menu, menu);
        return true;
    }

    public void setUnit(MenuItem item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_unit_layout, null);
        builder.setView(view);
        builder.setTitle("Choose your type");
        builder.setIcon(R.drawable.ic_radio_button_checked_black_24dp);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.celsius:
                        isFahrenheit = false;
                        break;

                    case R.id.fahrenheit:
                        isFahrenheit = true;
                        break;

                    default:
                        break;
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection needed", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                } else {
                    getWeatherData(getSelectedArea());
                    dialog.cancel();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String latitudeLongitude;

        switch (item.getItemId()) {
            case R.id.dhaka:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    setSelectedArea("23.8103,90.4125");
                    setTitle("Dhaka - 24/7 Weather");
                    defaultUrlDataLoad();
                }

            case R.id.rajshahi:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "24.3636,88.6241";
                    setTitle("Rajshahi - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.chattogram:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "22.3569,91.7832";
                    setTitle("Chattogram - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.rangpur:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "25.7468,89.2508";
                    setTitle("Rangpur - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.sylhet:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "24.8949,91.8687";
                    setTitle("Sylhet - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.barishal:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "22.7010,90.3535";
                    setTitle("Barishal - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.mymensingh:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "24.7471,90.4203";
                    setTitle("Mymensingh - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.khulna:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "22.8456,89.5403";
                    setTitle("Khulna - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.sydney:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "-33.8688,151.2093";
                    setTitle("Sydney - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.victoria:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "-36.686043,143.580322";
                    setTitle("Victoria - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.melbourne:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    latitudeLongitude = "-37.8136,144.9631";
                    setTitle("Melbourne - 24/7 Weather");
                    setSelectedArea(latitudeLongitude);
                    getWeatherData(latitudeLongitude);
                    return true;
                }

            case R.id.refresh:
                if (!netConnectionDetector.isConnected()) {
                    Toast.makeText(MainActivity.this, "Internet connection not found", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    getWeatherData(getSelectedArea());
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void defaultUrlDataLoad() {
        String savedLatitudeLongitude = "23.8103,90.4125";
        setTitle("Dhaka - 24/7 Weather");
        setSelectedArea(savedLatitudeLongitude);
        getWeatherData(getSelectedArea());
    }

    public boolean isSharedPreferencesHaveData() {
        SharedPreferences myPreference = getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        String curTemp = mySharedPreferences.getCurrentTemp();
        String curHumidity = mySharedPreferences.getCurrentHumidity();
        String curDate = mySharedPreferences.getCurrentDate();
        String curSummary = mySharedPreferences.getCurrentSummary();

        if (curTemp != "" && curHumidity != "" && curDate != "" && curSummary != "") {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    public void currentMore(View view) {
        if (netConnectionDetector.isConnected()) {
            Intent intent = new Intent(this, CurrentMoreActivity.class);
            intent.putStringArrayListExtra("current_more", (ArrayList<String>) currentlyMoreArrayList);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Internet connection needed", Toast.LENGTH_LONG).show();
        }
    }

    public String doubleToIntToString(double value) {
        return String.valueOf((int) Math.round(value));
    }
}

