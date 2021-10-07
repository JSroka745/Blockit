package com.example.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapp.ui.home.HomeFragment;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppInfo extends Fragment {
    PackageManager pm;
    ArrayList<Aplikacja> arrayOfApp = new ArrayList<Aplikacja>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    //private static final Drawable ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private Drawable mParam2;
    public boolean czas=true;
    public boolean zawsze=false;
    private OnAppInfoListener mCallback;
    public Context mContext;
    int endHour;
    int endMin;
    int startHour;
    int startMin;
    PackageInfo klikneta_appka;
    String text;
    TextToSpeech tts;
    private static final String AD_UNIT_ID = "ca-app-pub-2281213420760655/8519439761";
    private InterstitialAd interstitialAd;


    public AppInfo() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment AppInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static AppInfo newInstance(String param1) {
        AppInfo fragment = new AppInfo();
        Bundle args = new Bundle();
       // mContext=context;
        args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    public interface OnAppInfoListener {
        void messageFromAppifo(String text);
    }





    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //  mParam2 = getArguments().get
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }

        tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(new Locale("pl"));
                    tts.setSpeechRate((float) 0.7);
                }
            }


        });


    }

    public void make_alert(String title,String message)
    {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)


                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().getSupportFragmentManager().popBackStack();

                    }
                })
                .show();

    }




    @Override
    public void onPause() {
        // TODO Auto-generated method stub

        if(tts != null){

            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }








    private boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static void openApp(Context context, String appPackageName) {
        if (context == null) {

            return;
        }
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appPackageName);
        if (intent != null) {
            context.startActivity(intent);
        }else{

        }
    }




    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                       // Intent intent = new Intent(this, MainActivity.class);
                      //  MainActivity x= new MainActivity();
                        HomeFragment x=new HomeFragment();
                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), x);
                        fragmentTransaction.commit();
                        //startActivity(intent);
                    }
                }
            });

    public void blokada(){
        new AlertDialog.Builder(getContext())
                .setTitle("Zablokuj aplikacje")
                .setMessage("Na jak długo chcesz zablokować aplikacje?")

                .setPositiveButton("Zawsze", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Block_data data= new Block_data(klikneta_appka.packageName,0,0,0,0,0,true);
                        data.saveState(getContext());
                        getActivity().getSupportFragmentManager().popBackStack();
                        // Continue with delete operation
                    }
                })

                .setNeutralButton("Czasowo",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {


                                Calendar mcurrentTime = Calendar.getInstance();
                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                int minute = mcurrentTime.get(Calendar.MINUTE);



                                int myyear = mcurrentTime.get(Calendar.YEAR);
                                int mymonth = mcurrentTime.get(Calendar.MONTH);
                                int myday = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                                int day=0;
                                DatePickerDialog mDatePicker;

                                mDatePicker = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {



                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {






                                        TimePickerDialog mTimePicker;
                                        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                                endHour=selectedHour;
                                                endMin =selectedMinute;

                                                int pozostalyHour;
                                                int pozostalyMin;
                                                Calendar mcurrentTime = Calendar.getInstance();
                                                startHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                                startMin = mcurrentTime.get(Calendar.MINUTE);

                                                Calendar c = Calendar.getInstance();
                                                c.set(Calendar.HOUR_OF_DAY, selectedHour);
                                                c.set(Calendar.MINUTE, selectedMinute);
                                                c.set(Calendar.SECOND, 0);
                                                c.set(Calendar.MILLISECOND, 0);
                                                Date today = mcurrentTime.getTime();
                                                long todayInMillis = c.getTimeInMillis();


                                                c.set(Calendar.YEAR, year);
                                                c.set(Calendar.MONTH, month);
                                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                                Date dateSpecified = c.getTime();
                                                if (dateSpecified.before(today)) {

                                                    Toast.makeText(getActivity(), "wybrano złą date, spróbuj jeszcze raz ", Toast.LENGTH_LONG).show();
                                                } else {


                                                    Block_data data= new Block_data(klikneta_appka.packageName,selectedHour,selectedMinute,year,month,dayOfMonth,false);
                                                    data.saveState(getContext());

                                                    getActivity().getSupportFragmentManager().popBackStack();
                                                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                                                }



                                            }
                                        }, hour, minute, true);//Yes 24 hour time
                                        mTimePicker.setTitle("Wybierz godzine");
                                        mTimePicker.show();

                                    }

                                }, myyear, mymonth, myday);//Yes 24 hour time
                                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                mDatePicker.setTitle("Wybierz date");
                                mDatePicker.show();



                            }
                        })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Anuluj", null )



                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        final PackageManager pm = getActivity().getPackageManager();
        View root = inflater.inflate(R.layout.fragment_app_info, container, false);
        ConstraintLayout layout= (ConstraintLayout)root.findViewById(R.id.app_info);
        layout.setClickable(true);
        TextView tekst = (TextView)root.findViewById(R.id.textView2);
        ImageView obrazek = (ImageView)root.findViewById(R.id.AppImg);

        try {
            klikneta_appka=pm.getPackageInfo(mParam1,0);
            tekst.setText(klikneta_appka.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
            Drawable ikona=getContext().getPackageManager().getApplicationIcon(klikneta_appka.packageName);
            obrazek.setImageDrawable(ikona);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        Button przycisk= (Button)root.findViewById(R.id.button_wlacz);
        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageInfo klikneta_appka=pm.getPackageInfo(mParam1,0);
                    openApp(getContext(), klikneta_appka.packageName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });


        ImageView ikona= (ImageView)root.findViewById(R.id.AppImg);
        ikona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                try {
                    klikneta_appka=pm.getPackageInfo(mParam1,0);
                    Uri packageURI = Uri.parse("package:" +klikneta_appka.packageName);
                    Category_data xx= new Category_data();
                    String kategorie="";
                    String tekst="Kliknięta aplikacja to " +klikneta_appka.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                    ArrayList<String> lista_kategorii=xx.getOnlyAllCategory(mContext);

                    Boolean kat=false;
                    for(String cat:lista_kategorii)
                    {
                        ArrayList<String> aList=xx.getAppsByCategory(mContext,cat);
                        if(aList.contains(klikneta_appka.packageName)){
                            kategorie+=cat;
                            kategorie+="... ";

                            kat=true;
                        }
                    }
                    if(kat)
                    {
                        tekst+="Znajduje sie w kategorii "+kategorie;
                    }
                    else{
                        tekst+=". Nie znajduje się obecnie w żadnej kategorii";
                    }

                    Block_data dat= new Block_data();
                    if(dat.czy_zablokowana(klikneta_appka.packageName,mContext)){

                        if(dat.czy_zablokowana_na_stale(klikneta_appka.packageName,mContext))
                        {
                            tekst+=". Aplikacja jest zablokowana na stałe";
                        }


                        else{
                            Block_data app=dat.getData(klikneta_appka.packageName,mContext);
                            Calendar mcurrentTime = Calendar.getInstance();
                            int startHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int startMin = mcurrentTime.get(Calendar.MINUTE);
                            int startYear=mcurrentTime.get(Calendar.YEAR);
                            int startMonth=mcurrentTime.get(Calendar.MONTH);
                            int startDay=mcurrentTime.get(Calendar.DAY_OF_MONTH);
                            int endYear=app.endYear;
                            int endMonth=app.endMonth;
                            int endDay=app.endDay;
                            int endHour=app.endHour;
                            int endMin=app.endMin;
                            int pozostalyYear=0;
                            int pozostalyMonth=0;
                            int pozostalyDay=0;
                            int pozostalyHour=0;
                            int pozostalyMin=0;

                            tekst+=". Aplikacja będzie zablokowana przez ";
                            if(endMin-startMin>=0)
                            {

                                pozostalyMin=endMin-startMin;
                            }
                            else{
                                endHour--;
                                endMin+=60;
                                pozostalyMin=endMin-startMin;
                            }

                            if(endHour-startHour>=0)
                            {
                                pozostalyHour=endHour-startHour;
                            }
                            else{
                                endHour+=24;
                                endDay--;
                                pozostalyHour=endHour-startHour;
                            }

                            if(endDay-startDay>=0)
                            {
                                pozostalyDay=endDay-startDay;
                            }

                            else{
                                if(endMonth ==1 ||endMonth ==3 || endMonth ==5 || endMonth ==7 || endMonth ==8 || endMonth ==10 || endMonth ==12){
                                    endMonth--;
                                    endDay+=31;
                                }

                                else if(endMonth ==2){
                                    if(endYear%100==0)
                                    {
                                        if(endYear%400==0)
                                        {
                                            endDay+=29;
                                            endYear--;
                                        }

                                        else{
                                            endDay+=29;
                                            endYear--;
                                        }
                                    }
                                    else{
                                        if(endYear%4==0)
                                        {
                                            endDay+=29;
                                            endYear--;
                                        }

                                        else{
                                            endDay+=28;
                                            endYear--;
                                        }
                                    }
                                }

                                else if(endMonth ==4 ||endMonth ==6 || endMonth ==9 || endMonth ==11){
                                    endMonth--;
                                    endDay+=30;
                                }
                                pozostalyDay=endDay-startDay;
                            }


                            if(endMonth-startMonth>=0)
                            {
                                pozostalyMonth=endMonth-startMonth;
                            }

                            else{
                                endMonth+=12;
                                endYear--;
                                pozostalyMonth=endMonth-startMonth;
                            }

                            if(endYear-startYear>0)
                            {
                                pozostalyYear=endYear-startYear;
                                if(pozostalyYear==0)
                                {

                                }
                                else if(pozostalyYear==1){
                                tekst+=" jeden rok,";
                            }
                                else
                                {
                                    tekst+=pozostalyYear+" lata,";
                                }



                            }
                            if(pozostalyMonth==0)
                            {

                            }

                            else if(pozostalyMonth==1)
                            {
                                tekst+=" jeden miesiąc, ";
                            }

                            else if(pozostalyMonth>1&& pozostalyMonth<5){
                                tekst+=pozostalyMonth+" miesiące, ";
                            }

                            else{
                                tekst+=pozostalyMonth+" miesięcy, ";
                            }


                            if(pozostalyDay==0)
                            {

                            }
                            else if(pozostalyDay==1)
                            {
                                tekst+=" jeden dzień, ";
                            }

                            else{
                                tekst+=pozostalyDay+" dni, ";
                            }

                            if(pozostalyHour==0)
                            {

                            }

                            else if(pozostalyHour==1)
                            {
                                tekst+="jedną godzine, ";
                            }

                            else if(pozostalyHour==2)
                            {
                                tekst+="dwie godziny, ";
                            }
                            else if(pozostalyHour>1 && pozostalyHour<5)
                            {
                                tekst+=pozostalyHour+" godziny, ";
                            }
                            else{
                                tekst+=pozostalyHour+" godzin, ";
                            }



                            if(pozostalyMin==0)
                            {

                            }
                            else if(pozostalyMin==1)
                            {
                                tekst+=" jedną minute.";
                            }
                            else if(pozostalyMin==2)
                            {
                                tekst+=" dwie minuty.";
                            }

                            else if(pozostalyMin>1 && pozostalyMin<5)
                            {
                                tekst+=pozostalyMin+" minuty.";
                            }

                            else
                            {
                                tekst+=pozostalyMin+" minut.";
                            }





                        }
                    }
                    else{
                        tekst+=". Aplikacja nie jest zablokowana";
                    }


                    tts.speak(tekst,TextToSpeech.QUEUE_FLUSH,null,"x");


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }



            }
        });


        Button przycisk_usun= (Button)root.findViewById(R.id.button_usun);
        przycisk_usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    klikneta_appka=pm.getPackageInfo(mParam1,0);
                    Uri packageURI = Uri.parse("package:" +klikneta_appka.packageName); // replace with your package name
                    Category_data cat= new Category_data();
                    ArrayList<String> kk= new ArrayList<>();
                    kk.addAll(cat.dajKategoriedlaApki(mContext,klikneta_appka.packageName));
                    for(String x : kk)
                    {
                        cat.deletefromCategory(mContext,x, klikneta_appka.packageName);
                    }
                    Block_data bl=new Block_data();
                    if(bl.czy_zablokowana(klikneta_appka.packageName, mContext)){
                        bl.deleteData(mContext, klikneta_appka.packageName);
                    }

                    Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                    uninstallIntent.setData(packageURI);
                    uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                    // uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    someActivityResultLauncher.launch(uninstallIntent);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                        }
                    }
                });



        Button przycisk_usun_z_kateogrii= (Button)root.findViewById(R.id.button_usun_z_kategorii);
        przycisk_usun_z_kateogrii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    klikneta_appka=pm.getPackageInfo(mParam1,0);
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                    // builderSingle.setIcon(R.drawable.ic_launcher);
                    builderSingle.setTitle("Wybierz kategorie: ");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
                    Category_data x=new Category_data();
                    ArrayList<String> list=x.dajKategoriedlaApki(mContext,klikneta_appka.packageName);
                    for (String dane : list)
                    {
                        arrayAdapter.add(dane);
                    }


                    builderSingle.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(mContext);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("usunięto z kategorii");
                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {

                                    x.deletefromCategory(mContext,strName, klikneta_appka.packageName);


                                    dialog.dismiss();
                                    HomeFragment fragment = HomeFragment.newInstance();

                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                    fragmentTransaction.replace(R.id.strona_glowna, fragment);
                                    // fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }

                            });
                            builderInner.show();
                        }
                    });
                    //---------------------------------------------
                    builderSingle.show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });




        Button przycisk_odblokuj= (Button)root.findViewById(R.id.button_odblokuj);
        przycisk_odblokuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    klikneta_appka=pm.getPackageInfo(mParam1,0);
                    Uri packageURI = Uri.parse("package:" +klikneta_appka.packageName); // replace with your package name
                    Block_data x=new Block_data();
                    if( x.czy_zablokowana(klikneta_appka.packageName,mContext))
                    {
                        x.deleteData(mContext, klikneta_appka.packageName);
                        // uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        make_alert("Aplikacja odblokowana","Aplikacja "+ (klikneta_appka.applicationInfo.loadLabel(getActivity().getPackageManager()).toString())+ " została odblokowana");
                    }
                    else{
                        make_alert("Błąd","Aplikacja "+ (klikneta_appka.applicationInfo.loadLabel(getActivity().getPackageManager()).toString())+ " nie była zablokowana");
                    }




                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                /*
                Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                intent.setData(Uri.parse("package:" +arrayOfApp.get(Integer.parseInt(mParam1)).getFullName()));
                startActivity(intent);*/

            }
        });


        Button przycisk_kategoria= (Button)root.findViewById(R.id.button_kategorie);
        przycisk_kategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    klikneta_appka=pm.getPackageInfo(mParam1,0);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


                new AlertDialog.Builder(getContext())
                        .setTitle("Wybierz kategorie")
                        .setMessage("Chcesz stworzyć nową kategorie czy dodać do istniejącej?")


                        .setPositiveButton("Dodaj do istniejącej", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //----------------------------------------------------
                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                               // builderSingle.setIcon(R.drawable.ic_launcher);
                                builderSingle.setTitle("Wybierz kategorie");

                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
                                Category_data x=new Category_data();
                                ArrayList<String> list=x.getOnlyAllCategory(mContext);
                                for (String dane : list)
                                {
                                    arrayAdapter.add(dane);
                                }


                                builderSingle.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String strName = arrayAdapter.getItem(which);
                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(mContext);
                                        builderInner.setMessage(strName);
                                        Category_data xx=new Category_data();
                                        ArrayList<String>kati=new ArrayList<>();
                                        kati.addAll(xx.dajKategoriedlaApki(mContext,klikneta_appka.packageName));
                                        Boolean nalezy=false;
                                        for(String z :kati)
                                        {
                                            if(z.equals(strName))
                                            {
                                                nalezy=true;
                                            }
                                        }
                                        if(nalezy)
                                        {
                                            builderInner.setTitle("Aplikacja już należy do kategorii");
                                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,int which) {


                                                }

                                            });
                                        }
                                        else{
                                            builderInner.setTitle("Dodano do kategorii");
                                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,int which) {

                                                    Category_data category_data=new Category_data(klikneta_appka.packageName,strName);

                                                    category_data.addData(mContext);
                                                    dialog.dismiss();
                                                    HomeFragment fragment = HomeFragment.newInstance();

                                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                    fragmentTransaction.replace(R.id.strona_glowna, fragment);
                                                    // fragmentTransaction.addToBackStack(null);
                                                    fragmentTransaction.commit();
                                                }

                                            });
                                        }


                                        builderInner.show();
                                    }
                                });
                                //---------------------------------------------
                                builderSingle.show();


                            }
                        })

                        .setNeutralButton("Stwórz nową kategorie",
                                new DialogInterface.OnClickListener()
                                {
                                    String m_Text;
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle("Nazwa kategorii");

// Set up the input
                                        final EditText input = new EditText(mContext);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                                        builder.setView(input);

// Set up the buttons
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text = input.getText().toString();
                                                Category_data c=new Category_data();
                                                ArrayList<String> arr= new ArrayList<>();
                                                arr.addAll(c.getOnlyAllCategory(mContext));
                                                Boolean czy=false;
                                                for(String z : arr){
                                                    if(z.equals(m_Text))
                                                    {
                                                        czy=true;
                                                    }
                                                }
                                                if(czy){
                                                    make_alert("Kategoria "+m_Text+" już istnieje", null);
                                                }
                                                else{

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                    builder.setTitle("Czy chcesz dodać wiecej aplikacji do tej kategorii?");
                                                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {


                                                            MultiChoose fragment = MultiChoose.newInstance(m_Text, klikneta_appka.packageName);

                                                            FragmentTransaction fragmentTransaction = getActivity()
                                                                    .getSupportFragmentManager().beginTransaction();
                                                            fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment).addToBackStack("zkate");
                                                            fragmentTransaction.commit();


                                                            //getActivity().getSupportFragmentManager().popBackStack();
                                                        }
                                                    });
                                                    builder.setNeutralButton("Nie", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Category_data x=new Category_data(klikneta_appka.packageName, m_Text);
                                                            x.addData(mContext);
                                                            HomeFragment z=new HomeFragment();
                                                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                            fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), z);
                                                            fragmentTransaction.commit();

                                                        }
                                                    });
                                                    builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                                    builder.show();
                                                }



                                                

                                            }
                                        });
                                        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                        builder.show();
                                    }
                                })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Anuluj", null )



                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        Button przycisk_zablokuj= (Button)root.findViewById(R.id.button_zablokuj);
        przycisk_zablokuj.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {

                                                     czas = false;
                                                     zawsze = false;
                                                     klikneta_appka= null;
                                                     try {
                                                         klikneta_appka = pm.getPackageInfo(mParam1,0);
                                                     } catch (PackageManager.NameNotFoundException e) {
                                                         e.printStackTrace();
                                                     }
                                                     Block_data block_data=new Block_data();
                                                     if(block_data.czy_zablokowana(klikneta_appka.packageName,mContext)==true)
                                                     {
                                                         new AlertDialog.Builder(getContext())
                                                                 .setTitle("Aplikacja jest już zablokowana")
                                                                 .setMessage("Czy chcesz nałożyć na nią nową blokade?")
                                                                 .setPositiveButton("Nowa blokada", new DialogInterface.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         blokada();

                                                                     }

                                                                 })
                                                                 .setNegativeButton("Anuluj", null )
                                                                 .show();
                                                     }

                                                     else{
                                                         blokada();
                                                     }

                                                 }


                                             }

        );


        return root;

    }

}