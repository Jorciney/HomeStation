package org.vzw.beta.homestation.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.json.JSONObject;
import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.beans.Electricity;
import org.vzw.beta.homestation.beans.Fuel;
import org.vzw.beta.homestation.beans.Gas;
import org.vzw.beta.homestation.beans.Water;
import org.vzw.beta.homestation.tools.RootActivity;
import org.vzw.beta.homestation.tools.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends RootActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static JSONObject myMainJsonObject;
    private static PieDataSet dataset;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment fragment = null;
    private Class fragmentClass = HomeFragment.class;

    private Integer position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add content_main to the frameLayout
        replaceFragment(fragmentClass);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        //Drawer menu setup
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerToggle = setupDrawerToggle();
        drawer.setDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*Init database is now done on the splashscreen*/
        //initDatabase();
//        getDBKeyValueData("Electricity");
//        getDBKeyValueData("Water");
//        getDBKeyValueData("Gas");
//        getDBKeyValueData("Fuel");
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);

    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
    }

    /*Chart setup*/
    public static PieChart setUpChart(PieChart pieChart) {
        dataset = new PieDataSet(MainActivity.getEntries(), "Consumption");
        PieData data = new PieData(MainActivity.getLabels(), dataset); // initialize Piedata
        pieChart.setData(data); //set data into chart
        pieChart.setDescription("Total Consumption");
        int[] color = {Color.rgb(139, 195, 74), Color.rgb(255, 193, 7), Color.rgb(38, 198, 218), Color.rgb(239, 83, 80),
                Color.rgb(53, 194, 209)};


        dataset.setColors(color);
        pieChart.animateY(3000);
        pieChart.animateX(3000);
        pieChart.setCenterTextSizePixels((float) 40);
        pieChart.setDescriptionTextSize((float) 18);
        return pieChart;
    }

    public static ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Electricity");
        labels.add("Water");
        labels.add("Gas");
        labels.add("Fuel");
        return labels;
    }

    public static ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(MainActivity.getValueDBData("Electricity").floatValue(), 0));
        entries.add(new Entry(MainActivity.getValueDBData("Water").floatValue(), 1));
        entries.add(new Entry(MainActivity.getValueDBData("Gas").floatValue(), 2));
        entries.add(new Entry(MainActivity.getValueDBData("Fuel").floatValue(), 3));

        return entries;
    }

    public static Long getValueDBData(String str) {
        String info = "";
        Long value = (long) 0;
        if (str.equals("Electricity")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsElectricityDB.entrySet()) {
                value += (((Electricity) entry.getValue()).getValue());
            }
        } else if (str.equals("Water")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsWaterDB.entrySet()) {
                value += (((Water) entry.getValue()).getValue());
            }
        } else if (str.equals("Gas")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsGasDB.entrySet()) {
                value += (((Gas) entry.getValue()).getValue());
            }
        } else if (str.equals("Fuel")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsFuelDB.entrySet()) {
                value += (((Fuel) entry.getValue()).getValue());
            }
        }
        info += value + "\n";
        return value;
    }

    /*Firebase Database*/
    public void initDatabase() {
        Firebase.setAndroidContext(this);
        Utils.myFirebaseRef = new Firebase("https://jorcystation.firebaseIO.com");

    }


    public static void getDBKeyValueData(final String key) {
//        Utils.dataArrayDB.clear();

        Firebase data = Utils.myFirebaseRef.child(key);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) child.getValue();
                        String myKey = map.get("key").toString();
                        HashMap myValue = (HashMap) map.get("value");

                        Long b = (Long) myValue.get("value");
                        String c = (String) myValue.get("date");
                        if (key.equals("Electricity"))
                            Utils.dataObjectsElectricityDB.put(myKey, new Electricity(b, c, myKey));
                        else if (key.equals("Gas"))
                            Utils.dataObjectsGasDB.put(myKey, new Gas(b, c, myKey));
                        else if (key.equals("Fuel"))
                            Utils.dataObjectsFuelDB.put(myKey, new Fuel(b, c, myKey));
                        else if (key.equals("Water"))
                            Utils.dataObjectsWaterDB.put(myKey, new Water(b, c, myKey));
                    }
                    // TODO update fragment
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    public void setNewIntent(java.lang.Class intentClass) {
        Intent intent = new Intent(this, intentClass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_energy) {
            setNewIntent(EnergyActivity.class);
        } else if (id == R.id.nav_weather) {
//            setNewIntent(WeatherFragment.class);
            fragmentClass=WeatherFragment.class;

        } else if (id == R.id.nav_weather_radar) {
            fragmentClass=WeatherRadarWebViewFragment.class;
//        } else if (id == R.id.nav_radio) {
        } else if (id == R.id.nav_language) {
            startLanguageSelection();
//        } else if (id == R.id.nav_font_style) {
//            startThemeSelection();
        } else if (id == R.id.nav_Exit) {
            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Insert the fragment by replacing any existing fragment
        replaceFragment(fragmentClass);

        // Highlight the selected item, update the title, and close the drawer
        setTitle(item.getTitle());
        return true;
    }
    public void replaceFragment(Class fragmentClass) {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frameLayoutContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
