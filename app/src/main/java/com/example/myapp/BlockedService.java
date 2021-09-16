package com.example.myapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;


public class BlockedService extends Service implements View.OnTouchListener {

    private static final String TAG = BlockedService.class.getSimpleName();

    private WindowManager windowManager;
    ImageView img;
    String nazwa_apki;

    private View floatyView;
    private ArrayList<View> lista=new ArrayList<>();
    private LinearLayout layout;

    public BlockedService() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent !=null && intent.getExtras()!=null){
            //nazwa_apki = intent.getExtras().getString("ARG1");
            Bundle b = intent.getExtras();
            nazwa_apki = b.getString("ARG1");


    }
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        addOverlayView();



       showHomeScreen_prev();
        showHomeScreen();


        return START_NOT_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {


        super.onCreate();


    }

    private void addOverlayView() {

        final WindowManager.LayoutParams params;
        int layoutParamsType;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else {
            layoutParamsType = LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutParamsType,
                0,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.START;
        params.x = 0;
        params.y = 0;





        LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        if (inflater != null) {
          //  floatyView = inflater.inflate(R.layout.activity_lock_screen, interceptorLayout);
            floatyView = inflater.inflate(R.layout.activity_lock_screen,null);
            ImageView img=floatyView.findViewById(R.id.lockscreenimg);
            Button button=floatyView.findViewById(R.id.lock_screen_button);
            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
                    am.killBackgroundProcesses(nazwa_apki);
                    showHomeScreen();
                    for(View x:lista)
                    {

                        windowManager.removeView(x);

                    }
                    lista.clear();
                    //startService(new Intent(getApplicationContext(),MyService.class));
                    onDestroy();
                }
            });
            Drawable dd= null;
            try {
                dd = getApplicationContext().getPackageManager().getApplicationIcon(nazwa_apki);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            img.setImageDrawable(dd);
            floatyView.setOnTouchListener(this);

            windowManager.addView(floatyView, params);
            lista.add(floatyView);
        }
        else {

        }
    }


    @Override
    public void onDestroy() {


            if(floatyView!=null) {

                windowManager.removeView(floatyView);
            }

            floatyView = null;
        //stopSelf();

    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();



        // Kill service


        return true;
    }

    public boolean showHomeScreen(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        //startMain.addCategory(Intent.CATEGORY_TAB);
       // startMain.addCategory(Intent.CATEGORY_ALTERNATIVE);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startMain);
        return true;
    }
    public boolean showHomeScreen_prev(){
        Intent startMain = new Intent(this,MainActivity.class);
       // startMain.addCategory(Intent.CATEGORY_APP_CALCULATOR);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startMain);
        return true;
    }
}