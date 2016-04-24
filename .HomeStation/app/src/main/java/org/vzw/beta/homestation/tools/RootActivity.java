package org.vzw.beta.homestation.tools;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import org.vzw.beta.homestation.R;

import java.util.Locale;

/**
 * Created by user109 on 7/03/2016.
 */
public class RootActivity extends AppCompatActivity{




    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (onStartCount > 1) {
//            this.overridePendingTransition(R.anim.anim_slide_in_right,
//                    R.anim.anim_slide_out_right);

            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }


    /*Language Selection*/

    protected void startLanguageSelection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.language_selection_layout, null);
        final RadioButton rdioBttonEnglish = (RadioButton) view.findViewById(R.id.rdioButton_Language_English);
        final RadioButton rdioBttonDutch = (RadioButton) view.findViewById(R.id.rdioButton_Language_Dutch);

        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (rdioBttonEnglish.isChecked()) {
                    PreferencesHelper.setLanguagePreference(getBaseContext(), Utils.LANGUAGE_EN);
                    setLocalLanguage(Utils.LANGUAGE_EN);
                    createToast("English has been selected");
                } else if (rdioBttonDutch.isChecked()) {
                    PreferencesHelper.setLanguagePreference(getBaseContext(), Utils.LANGUAGE_NL);
                    setLocalLanguage(Utils.LANGUAGE_NL);
                    createToast("Nederlands is geselecteerd");
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
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    protected void setLocalLanguage(String language) {
        Locale myLocale = new Locale(language);
        Locale.setDefault(myLocale);
        Configuration configuration = new Configuration();
        configuration.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }




    protected void createToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    protected void setNewIntent(java.lang.Class intentClass) {
        Intent intent = new Intent(this, intentClass);
        startActivity(intent);
    }


}

