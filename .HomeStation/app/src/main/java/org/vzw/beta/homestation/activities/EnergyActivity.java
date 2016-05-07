package org.vzw.beta.homestation.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.goka.blurredgridmenu.BlurredGridMenuConfig;
import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.beans.Electricity;
import org.vzw.beta.homestation.beans.Fuel;
import org.vzw.beta.homestation.beans.Gas;
import org.vzw.beta.homestation.beans.Water;
import org.vzw.beta.homestation.tools.RootActivity;
import org.vzw.beta.homestation.tools.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by user109 on 22/03/2016.
 */
public class EnergyActivity extends RootActivity {

    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;
    private ImageButton pickDateButton;

    private EditText givenValueEditText;
    private EditText givenDateEditText;
    private Spinner dropdown;
    private GridMenuFragment mGridMenuFragment;
    private ImageButton menuButtonExpand;
    private ImageButton menuButtonCollapse;
    private SlidingUpPanelLayout panel;
    private View mDecorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_activity_layout);
        mDecorView = getWindow().getDecorView();
        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.gradient_background_half_transparent);
//        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.gradient_background_other);
        menuButtonExpand = (ImageButton) findViewById(R.id.energy_expand_menu);
        menuButtonCollapse = (ImageButton) findViewById(R.id.energy_collapse_menu);

        BlurredGridMenuConfig.build(new BlurredGridMenuConfig.Builder().radius(10).downsample(10).overlayColor(Color.parseColor("#46DEDADA")));
        panel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        panel.setOverlayed(true);

        panel.setPanelHeight(35);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.energy_slide_up_linear_layout, mGridMenuFragment);
        tx.addToBackStack(null);
        tx.commit();
        menuButtonCollapse.setVisibility(View.INVISIBLE);
        menuButtonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                panel.setAlpha((float) 0.7);
                panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        menuButtonCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                panel.setAlpha((float) 1);
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        panel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panelView, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panelView, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
//                    panel.setAlpha((float) 0.7);
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    menuButtonCollapse.setVisibility(View.VISIBLE);
                    menuButtonExpand.setVisibility(View.INVISIBLE);
                }
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED || newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    menuButtonCollapse.setVisibility(View.INVISIBLE);
                    menuButtonExpand.setVisibility(View.VISIBLE);
                }
            }
        });
        setupGridMenu();
        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {
                Intent intent = new Intent(EnergyActivity.this, MainActivity.class);
                switch (position) {
                    case 0:
                        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        MainActivity.FRAG_ACTION=Utils.FRAG_ACTION_HOME;
                        startActivity(intent);
                        finish();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case 1:
                        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    case 2:
                        finish();
                        MainActivity.FRAG_ACTION=Utils.FRAG_ACTION_WEATHER;
                        startActivity(intent);
                        break;
                    case 3:
                        finish();
                        MainActivity.FRAG_ACTION=Utils.FRAG_ACTION_RADAR;
                        startActivity(intent);
                        break;
                    case 4:
                        Toast.makeText(EnergyActivity.this, "Title:" + gridMenu.getTitle() + ", Position:" + position,
                                Toast.LENGTH_SHORT).show();
                        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    case 5:
                        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        startLanguageSelection();
                        break;
                    case 6:
                        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        addConsumptionDataDialog();
                        break;
                    default:
                        finish();
                        break;
                }
            }
        });

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.overview, OverviewFragment.class)
                .add(R.string.electricity, ElectricityFragment.class)
                .add(R.string.watercounter, WaterFragment.class)
                .add(R.string.gas, GasFragment.class)
                .add(R.string.fuel, FuelFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        // int position = FragmentPagerItem.getPosition(getArguments());
        //getDBKeyValueData("Electricity");
    }

    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.clear();
        menus.add(new GridMenu("Home", R.drawable.ic_home_white_48_));
        menus.add(new GridMenu("Energy", R.drawable.ic_energy_gas_48_));
        menus.add(new GridMenu("Weather", R.drawable.ic_sun_weather_48_));
        menus.add(new GridMenu("Radar", R.drawable.ic_weather_umbrela_48));
        menus.add(new GridMenu("Settings", R.drawable.ic_setting_gear_48_));
        menus.add(new GridMenu("Language", R.drawable.ic_language_world_48_));
        menus.add(new GridMenu("AddConsumption", R.drawable.ic_add_circle_48));
        menus.add(new GridMenu("Exit", R.drawable.ic_exit_direction_48_));

        mGridMenuFragment.setupMenu(menus);
    }

    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            moveTaskToBack(true);
            EnergyActivity.this.finish();
        }
    }

    private void addConsumptionDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();


        final View view = inflater.inflate(R.layout.energy_consumption_add_dialog, null);

        dateView = (EditText) view.findViewById(R.id.consumption_date_editText);
        pickDateButton = (ImageButton) view.findViewById(R.id.consumption_pickDate_imagebutton);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        dropdown = (Spinner) view.findViewById(R.id.dropdownChooseConsumptionSpinner);
        givenValueEditText = (EditText) view.findViewById(R.id.consumption_value_editText);
        givenDateEditText = (EditText) view.findViewById(R.id.consumption_date_editText);

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(view);
            }
        });


        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (dropdown.getSelectedItemPosition()) {
                    case 0:
                        Electricity tempElectricity = new Electricity(Long.parseLong(givenValueEditText.getText().toString()), givenDateEditText.getText().toString());
                        Utils.dataObjectsElectricityDB.clear();
                        setDBKeyValueData("Electricity", tempElectricity);
                        ElectricityFragment.recyclerViewAdapter.notifyDataSetChanged();
                        ElectricityFragment.updateInfo();
                        break;
                    case 1:
                        Water tempWater = new Water(Long.parseLong(givenValueEditText.getText().toString()), givenDateEditText.getText().toString());
                        Utils.dataObjectsWaterDB.clear();
                        setDBKeyValueData("Water", tempWater);
                        WaterFragment.recyclerViewAdapter.notifyDataSetChanged();
                        WaterFragment.updateInfo();
                        break;
                    case 2:
                        Gas tempGas = new Gas(Long.parseLong(givenValueEditText.getText().toString()), givenDateEditText.getText().toString());
                        Utils.dataObjectsGasDB.clear();
                        setDBKeyValueData("Gas", tempGas);
                        GasFragment.recyclerViewAdapter.notifyDataSetChanged();
                        GasFragment.updateInfo();
                        break;
                    default:
                        Fuel tempFuel = new Fuel(Long.parseLong(givenValueEditText.getText().toString()), givenDateEditText.getText().toString());
                        Utils.dataObjectsFuelDB.clear();
                        setDBKeyValueData("Fuel", tempFuel);
                        FuelFragment.recyclerViewAdapter.notifyDataSetChanged();
                        FuelFragment.updateInfo();
                        break;
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createToast("Canceled");
            }
        });
        builder.setIcon(R.drawable.ic_action_flag);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public static void addDBKeyValue(String key, Object value) {
        Utils.myFirebaseRef.child(key).setValue(value);
    }

    public static void updateDBKeyValue(String key, Object value) {
        Firebase data = Utils.myFirebaseRef.child(key);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue()); // the String "John"
                for (DataSnapshot child : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) child.getValue();
                    String a = (String) map.get("uniqueID");
                    String b = (String) map.get("date");
                    Long c = (Long) map.get("value");
                    System.out.println(b + " -- " + a + " -- " + c + "\n");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public static void setDBKeyValueData(final String key, final Object value) {

        Firebase data = Utils.myFirebaseRef.child(key);
        data.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue() + " Count: " + snapshot.getChildrenCount()); // the String "John"
                if (snapshot != null && snapshot.getChildrenCount() > 0) {

                    for (DataSnapshot child : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) child.getValue();
                        String myKey = (String) map.get("key");
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
                }
                if (key.equals("Electricity")) {
                    Utils.dataObjectsElectricityDB.put(((Electricity) value).getUniqueID(), value);
                    Utils.myFirebaseRef.child(key).setValue(Utils.dataObjectsElectricityDB.entrySet());
                } else if (key.equals("Gas")) {
                    Utils.dataObjectsGasDB.put(((Gas) value).getUniqueID(), value);
                    Utils.myFirebaseRef.child(key).setValue(Utils.dataObjectsGasDB.entrySet());
                } else if (key.equals("Fuel")) {
                    Utils.dataObjectsFuelDB.put(((Fuel) value).getUniqueID(), value);
                    Utils.myFirebaseRef.child(key).setValue(Utils.dataObjectsFuelDB.entrySet());
                } else if (key.equals("Water")) {
                    Utils.dataObjectsWaterDB.put(((Water) value).getUniqueID(), value);
                    Utils.myFirebaseRef.child(key).setValue(Utils.dataObjectsWaterDB.entrySet());
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

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}