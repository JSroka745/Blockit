package com.example.myapp.guide;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro {
    int REQUEST_ID_MULTIPLE_PERMISSIONS =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_intro);
        setSkipButtonEnabled(false);
        setColorTransitionsEnabled(true);
        addSlide(AppIntroFragment.newInstance("Witaj", "Dziękujemy za pobranie naszej aplikacji. Przed tobą kilka ważnych informacji",
                R.drawable.baselogo, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark)));
        addSlide(AppIntroFragment.newInstance("Funkcje", "Dzięki tej aplikacji możesz w prosty sposób kontrolować swoje programy",
                R.drawable.zdj1, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_light)));
        addSlide(AppIntroFragment.newInstance("Fukncje", "Kliknij na aplikacje aby podjąć działanie",
                R.drawable.zdj2, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_dark)));
        addSlide(AppIntroFragment.newInstance("Fukncje", "Kliknij na ikone aplikacji aby usłyszeć o niej informacje",
                R.drawable.zdj3, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light)));
        addSlide(AppIntroFragment.newInstance("Kategorie", "Twórz kategorie aby ułatwić dostęp",
                R.drawable.zdj4, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light)));
        addSlide(AppIntroFragment.newInstance("Uprawnienia", "Zezwól aplikacji na dostęp do pamięci urządzenia",
                android.R.drawable.ic_menu_manage, ContextCompat.getColor(getApplicationContext(), R.color.purple_500)));
        addSlide(AppIntroFragment.newInstance("Blokowanie aplikacji", "Dotknij przycisk Menu, aby przejść do strony Ostatnich Aplikacji. Wybierz karte mobliny system zarządzania aplikacjami, a następnie zablokuj ",
                R.drawable.zdj6, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light)));
        addSlide(AppIntroFragment.newInstance("Uprawnienia", "Po klinięciu skończone zezwól na dostęp do danych o użyciu oraz na wyświetlanie nad innymi aplikacjiami ",
                R.drawable.zdj5, ContextCompat.getColor(getApplicationContext(), R.color.purple_200)));

        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 6);
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);


    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_ID_MULTIPLE_PERMISSIONS);

            }
        }

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingSupportFragment

        //  Intro App Initialize SharedPreferences
        SharedPreferences getSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = getSharedPreferences.edit();
        e.putBoolean("firstStart", false);
        e.apply();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);




        finish();
    }

    @Override
    protected void onNextSlide() {
        super.onNextSlide();


    }



    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

    }
}