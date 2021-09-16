package com.example.myapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapp.adaptery.CustomAppAdapter;
import com.example.myapp.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM12 = "param1";
   // private static final String ARG_PARAM2 = "param2";
    public Context mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public boolean czas=true;
    public boolean zawsze=false;
    int endHour;
    int endMin;
    int startHour;
    int startMin;
    ArrayList<Aplikacja> arrayOfApp = new ArrayList<Aplikacja>();
    public CategoryInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment CategoryInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryInfo newInstance(String param1) {
        CategoryInfo fragment = new CategoryInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM12, param1);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM12);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void make_alert(String title,String message)
    {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                    }
                })
                .show();

    }

    public void blokada(ArrayList<String> lista_appek){
        new AlertDialog.Builder(getContext())
                .setTitle("Zablokuj kategorie")
                .setMessage("Na jak długo chcesz zablokować kategorie?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Zawsze", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for(String kazda_apka: lista_appek)
                        {
                            Block_data data= new Block_data(kazda_apka,0,0,0,0,0,true);
                            data.saveState(getContext());


                        }
                        int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();

                        if (count == 0) {
                            getActivity().onBackPressed();
                            //additional code
                        } else {
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }

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



                                                c.set(Calendar.YEAR, year);
                                                c.set(Calendar.MONTH, month);
                                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                                Date dateSpecified = c.getTime();
                                                if (dateSpecified.before(today)) {

                                                    Toast.makeText(getActivity(), "wybrano złą date, spróbuj jeszcze raz ", Toast.LENGTH_LONG).show();
                                                } else {


                                                    for(String kazda_apka:lista_appek)
                                                    {
                                                        Block_data data= new Block_data(kazda_apka,selectedHour,selectedMinute,year,month,dayOfMonth,false);
                                                        data.saveState(getContext());


                                                        //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                                                    }
                                                }

                                                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();

                                                if (count == 0) {
                                                    getActivity().onBackPressed();
                                                    //additional code
                                                } else {
                                                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                                                }

                                            }
                                        }, hour, minute, true);//Yes 24 hour time
                                        mTimePicker.setTitle("Wybierz godzinę");
                                        mTimePicker.show();

                                    }

                                }, myyear, mymonth, myday);//Yes 24 hour time
                                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                mDatePicker.setTitle("Wybierz datę");
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
        // Inflate the layout for this fragment
        Category_data category_data=new Category_data();
        ArrayList<String> lista_appek=category_data.getAppsByCategory(mContext,mParam1);

        View root = inflater.inflate(R.layout.fragment_category_info, container, false);
        ConstraintLayout layout= (ConstraintLayout)root.findViewById(R.id.jedna_kategoria);
        layout.setClickable(true);
        TextView tekst = (TextView)root.findViewById(R.id.tv_fr_category);
       // ImageView obrazek = (ImageView)root.findViewById(R.id.AppImg);
        tekst.setText(mParam1);
        ListView listview = (ListView)root.findViewById(R.id.fr_category_listview);
        for(String apka : lista_appek)
        {
            try {
                //packageInfo.packageName
                Drawable dd = getContext().getPackageManager().getApplicationIcon(apka);
                final PackageManager pm = getActivity().getPackageManager();
                PackageInfo info = pm.getPackageInfo(apka, 0);
                Aplikacja x = new Aplikacja(info.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), dd, "test") {
                    @Override
                    public View getView(LayoutInflater inflater, View convertView) {
                        return null;
                    }
                };
                //Toast.makeText(getActivity(), "xx: "+packageInfo.applicationInfo.packageName, Toast.LENGTH_SHORT).show();

                arrayOfApp.add(x);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            CustomAppAdapter adapter = new CustomAppAdapter(getActivity(), arrayOfApp,null);
            listview.setAdapter(adapter);
        }

        Button button_usun_kategorie = (Button)root.findViewById(R.id.button_fr_usun_kategorie);
        button_usun_kategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_data.deleteCategory(mContext,mParam1);
               // HomeFragment fragment = HomeFragment.newInstance();

                make_alert("Kategoria "+mParam1 + " została usunięta",null);

                //FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
               // fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                // fragmentTransaction.addToBackStack(null);
               // fragmentTransaction.commit();
                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();

                if (count == 0) {
                    getActivity().onBackPressed();
                    //additional code
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        Button button_dodaj_app = (Button)root.findViewById(R.id.button_fr_dodaj_app);
        button_dodaj_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiChoose fragment = MultiChoose.newInstance(mParam1);

                Bundle bundle = new Bundle();
                bundle.putString("param",mParam1);
                bundle.putSerializable("key", lista_appek);
                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                fragmentTransaction.commit();

            }
        });

        Button przycisk_zablokuj= (Button)root.findViewById(R.id.button_fr_zablokuj_kategorie);
        przycisk_zablokuj.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {

                                                     czas = false;
                                                     zawsze = false;
                                                     Block_data data1= new Block_data();
                                                     Boolean czy_zablokowana=false;
                                                     String nazwy="";
                                                     final PackageManager pm = getActivity().getPackageManager();
                                                     String stext="Aplikacja zablokowana";
                                                     String stext2="Aplikacja ";
                                                     String stext3=" jest zablokowana ";
                                                     int i=0;
                                                     for(String kazda_apka: lista_appek)
                                                     {
                                                         if(data1.czy_zablokowana(kazda_apka,mContext))
                                                         {
                                                             czy_zablokowana=true;
                                                             PackageInfo pinfo= null;
                                                             try {
                                                                 pinfo = pm.getPackageInfo(kazda_apka,0);
                                                                 if(i==0)
                                                                 {
                                                                     nazwy+=pinfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                                                                 }
                                                                 else {
                                                                     stext="Aplikacje zablokowane";
                                                                     stext2="Aplikacje ";
                                                                     stext3=" są zablokowane ";
                                                                     nazwy+=", "+pinfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                                                                 }
                                                             } catch (PackageManager.NameNotFoundException e) {
                                                                 e.printStackTrace();
                                                             }
                                                             i++;
                                                         }



                                                     }

                                                     if(czy_zablokowana==true)
                                                     {
                                                         new AlertDialog.Builder(getContext())
                                                                 .setTitle(stext)
                                                                 .setMessage(stext2+nazwy+ stext3+". Czy chcesz nałożyć nową blokade?")
                                                                 .setPositiveButton("Nowa blokada", new DialogInterface.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         blokada(lista_appek);

                                                                     }

                                                                 })
                                                                 .setNegativeButton("Anuluj", null )
                                                                 .show();
                                                     }

                                                     else{
                                                         blokada(lista_appek);
                                                     }



                                                     // Specifying a listener allows you to take an action before dismissing the dialog.
                                                     // The dialog is automatically dismissed when a dialog button is clicked.







                                                 }


                                             }


        );
        Button przycisk_odblokuj= (Button)root.findViewById(R.id.button_fr_odblokuj_kategorie);
        przycisk_odblokuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tekst="";
                Block_data x=new Block_data();
                final PackageManager pm = getActivity().getPackageManager();
                PackageInfo pinfo= null;

                try {
                    int i=0;
                    for(String app:lista_appek)
                    {
                        if(x.czy_zablokowana(app,mContext))
                        {
                            if(i==0)
                            {
                                pinfo = pm.getPackageInfo(app,0);
                                tekst+=pinfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                                x.deleteData(mContext, app);
                            }
                            else{
                                pinfo = pm.getPackageInfo(app,0);
                                tekst+=", "+pinfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                                x.deleteData(mContext, app);
                            }
                        }
                        else{

                        }


                    }

                    if(tekst.equals(""))
                    {
                        make_alert("Bład", "Aplikacje nie są zablokowane");
                    }
                    else{
                        make_alert("Odblokowano", "Odblokowano: "+tekst);
                    }



                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });


        return root;
    }
}