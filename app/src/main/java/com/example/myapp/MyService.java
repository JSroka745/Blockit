package com.example.myapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapp.ui.home.HomeFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;
import static com.example.myapp.App.CHANNEL_ID;

public class MyService extends Service implements View.OnTouchListener  {
    private WindowManager windowManager;
    private boolean isRunning = false;
    private boolean sprawdzanie = true;

    private View floatyView;
    public MyService() {

    }


    @Override
    public void onCreate() {
        if (!isRunning) {
            windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            String NOTIFICATION_CHANNEL_ID2 = "com.example.simpleapp2";
            String channelName2 = "My Background Service2";
            NotificationChannel chan2 = new NotificationChannel(NOTIFICATION_CHANNEL_ID2, channelName2, NotificationManager.IMPORTANCE_HIGH);
            chan2.setLightColor(Color.BLUE);
            chan2.setVibrationPattern(new long[]{1000, 200, 300, 400, 500, 400, 300, 200, 400});
            chan2.enableVibration(true);


            chan2.setLockscreenVisibility(NotificationCompat.PRIORITY_HIGH);

            NotificationManager manager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            assert manager2 != null;
            manager2.createNotificationChannel(chan2);

            Intent inte = new Intent(this, MainActivity.class);
            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, inte, 0);




            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.baselogo)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.baselogo))

                    .setContentTitle("Aplikacja działa w tle")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(2, notification);
            isRunning = true;
            // do your work here

            // return super.onStartCommand(intent, flags, startId);
            // class Array_data extends ArrayList<Block_data> {};
            ArrayList<Block_data> array_data = new ArrayList<Block_data>();
            Context mContext = this;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    ArrayList<Block_data> array_data = new ArrayList<Block_data>();
                    ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
                    //  am.killBackgroundProcesses("com.facebook.orca");

                    String path = mContext.getFilesDir().getAbsolutePath();
                    Boolean cont = true;
                    final File suspend_f = new File(path + "/data.dat");
                    Log.i("nice1", path + "/data.dat");
                    if (suspend_f.exists()) {
                        FileInputStream inStream = null;
                        try {
                            inStream = new FileInputStream(suspend_f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ObjectInputStream objectInStream = null;
                        try {


                            while (cont) {
                                objectInStream = new ObjectInputStream(inStream);
                                Block_data data = null;

                                try {
                                    data = (Block_data) objectInStream.readObject();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (data != null) {
                                    array_data.add(data);
                                } else {
                                    cont = false;
                                }
                            }
                            try {
                                objectInStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    String currentApp = getForegroundApp();

                    Log.i("xxx", "current_app: " + currentApp);
                    for (int i = 0; i < array_data.size(); i++) {
                        Log.i("xxx1", String.valueOf(array_data.get(i).packagename));
                    }
                    for (int i = 0; i < array_data.size(); i++) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int startHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int startMin = mcurrentTime.get(Calendar.MINUTE);
                        int startYear=mcurrentTime.get(Calendar.YEAR);
                        int startMonth=mcurrentTime.get(Calendar.MONTH);
                        int startDay=mcurrentTime.get(Calendar.DAY_OF_MONTH);

                        if (array_data.get(i).endYear <= startYear && array_data.get(i).endMonth <= startMonth && array_data.get(i).endDay <= startDay  && array_data.get(i).endHour <= startHour && array_data.get(i).endMin <= startMin) {
                            array_data.get(i).deleteData(mContext, array_data.get(i).packagename);




                            PackageManager pm=getPackageManager();
                            PackageInfo info= null;
                            try {
                                info = pm.getPackageInfo(array_data.get(i).packagename,0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Create an explicit intent for an Activity in your app
                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID2);
                            NotificationCompat.Builder notification = notificationBuilder
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setSmallIcon(R.drawable.baselogo)

                                    .setContentTitle("Odblokowano aplikacje")
                                    .setContentText("Aplikacja "+info.applicationInfo.loadLabel(mContext.getPackageManager()).toString()+" została odblokowana")
                                    .setPriority(NotificationManager.IMPORTANCE_HIGH)


                                    //LED
                                    .setLights(Color.RED, 3000, 3000)

                                    .setCategory(Notification.CATEGORY_SERVICE);



                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

// notificationId is a unique int for each notification that you must define
                            notificationManager.notify(10, notification.build());












                        }
                        if (array_data.get(i).packagename.equals(currentApp) && sprawdzanie) {



                            Intent svc = new Intent(mContext, BlockedService.class);
                            svc.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            svc.putExtra("ARG1",array_data.get(i).packagename);

                            startService(svc);
                            // sprawdzanie=false;
                            showHomeScreen_prev();
                            showHomeScreen();
                            //   addOverlayView(array_data.get(i).packagename);
                            currentApp = "NULL";

                            // timer.cancel();
                            am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
                            am.killBackgroundProcesses(array_data.get(i).packagename);

                            //stopSelf();








                        }
                    }


                }

            }, 0, 1000);





        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            String NOTIFICATION_CHANNEL_ID2 = "com.example.simpleapp2";
            String channelName2 = "My Background Service2";
            NotificationChannel chan2 = new NotificationChannel(NOTIFICATION_CHANNEL_ID2, channelName2, NotificationManager.IMPORTANCE_HIGH);
            chan2.setLightColor(Color.BLUE);
            chan2.setVibrationPattern(new long[]{1000, 200, 300, 400, 500, 400, 300, 200, 400});
            chan2.enableVibration(true);


            chan2.setLockscreenVisibility(NotificationCompat.PRIORITY_HIGH);

            NotificationManager manager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            assert manager2 != null;
            manager2.createNotificationChannel(chan2);

            Intent inte = new Intent(this, MainActivity.class);
            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, inte, 0);




            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.baselogo)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.baselogo))

                    .setContentTitle("Aplikacja działa w tle")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(2, notification);
            isRunning = true;
            // do your work here

        // return super.onStartCommand(intent, flags, startId);
        // class Array_data extends ArrayList<Block_data> {};
        ArrayList<Block_data> array_data = new ArrayList<Block_data>();
        Context mContext = this;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                ArrayList<Block_data> array_data = new ArrayList<Block_data>();
                ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
                //  am.killBackgroundProcesses("com.facebook.orca");

                String path = mContext.getFilesDir().getAbsolutePath();
                Boolean cont = true;
                final File suspend_f = new File(path + "/data.dat");
                if (suspend_f.exists()) {
                    FileInputStream inStream = null;
                    try {
                        inStream = new FileInputStream(suspend_f);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ObjectInputStream objectInStream = null;
                    try {


                        while (cont) {
                            objectInStream = new ObjectInputStream(inStream);
                            Block_data data = null;

                            try {
                                data = (Block_data) objectInStream.readObject();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (data != null) {
                                array_data.add(data);
                            } else {
                                cont = false;
                            }
                        }
                        try {
                            objectInStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                String currentApp = getForegroundApp();

                Log.i("xxx", "current_app: " + currentApp);
                for (int i = 0; i < array_data.size(); i++) {
                    Log.i("xxx1", String.valueOf(array_data.get(i).packagename));
                }
                for (int i = 0; i < array_data.size(); i++) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int startHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int startMin = mcurrentTime.get(Calendar.MINUTE);
                    int startYear=mcurrentTime.get(Calendar.YEAR);
                    int startMonth=mcurrentTime.get(Calendar.MONTH);
                    int startDay=mcurrentTime.get(Calendar.DAY_OF_MONTH);

                    if (array_data.get(i).endYear == startYear && array_data.get(i).endMonth == startMonth && array_data.get(i).endDay == startDay  && array_data.get(i).endHour == startHour && array_data.get(i).endMin == startMin) {
                        array_data.get(i).deleteData(mContext, array_data.get(i).packagename);




                        PackageManager pm=getPackageManager();
                        PackageInfo info= null;
                        try {
                            info = pm.getPackageInfo(array_data.get(i).packagename,0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        // Create an explicit intent for an Activity in your app
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID2);
                        NotificationCompat.Builder notification = notificationBuilder
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setSmallIcon(R.drawable.baselogo)

                                .setContentTitle("Odblokowano aplikacje")
                                .setContentText("Aplikacja "+info.applicationInfo.loadLabel(mContext.getPackageManager()).toString()+" została odblokowana")
                                .setPriority(NotificationManager.IMPORTANCE_HIGH)


                        //LED
                                .setLights(Color.RED, 3000, 3000)

                                .setCategory(Notification.CATEGORY_SERVICE);



                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

// notificationId is a unique int for each notification that you must define
                        notificationManager.notify(10, notification.build());












                    }
                    if (array_data.get(i).packagename.equals(currentApp) && sprawdzanie) {



                        Intent svc = new Intent(mContext, BlockedService.class);
                        svc.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        svc.putExtra("ARG1",array_data.get(i).packagename);

                        startService(svc);
                       // sprawdzanie=false;
                        showHomeScreen_prev();
                        showHomeScreen();
                     //   addOverlayView(array_data.get(i).packagename);
                        currentApp = "NULL";

                       // timer.cancel();
                        am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
                        am.killBackgroundProcesses(array_data.get(i).packagename);


                    }
                }


            }

        }, 0, 1000);  // every 6 seconds





    }
        return START_STICKY;
    }



    private void addOverlayView(String nazwa_apki) {

        final WindowManager.LayoutParams params;
        int layoutParamsType;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else {
            layoutParamsType = WindowManager.LayoutParams.TYPE_PHONE;
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
            showHomeScreen_prev();
            showHomeScreen();
            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    showHomeScreen_prev();
                    showHomeScreen();
                    sprawdzanie=true;

                    if(floatyView!=null) {
                        windowManager.removeView(floatyView);
                    }

                    floatyView = null;
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
            showHomeScreen_prev();
            showHomeScreen();
           /* new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    windowManager.addView(floatyView, params);


                }
            });*/


        }
        else {

        }
    }

    public String getForegroundApp() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else
            {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        return currentApp;
    }

    public boolean showHomeScreen(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);

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




    @Override
    public void onDestroy() {
       //super.onDestroy();

        Intent broadcastIntent = new Intent(this,RecieverCall.class);

        sendBroadcast(broadcastIntent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        return true;
    }
}