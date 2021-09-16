package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapp.adaptery.CustomAppAdapter;
import com.example.myapp.adaptery.Model;
import com.example.myapp.adaptery.MultiAdapter;
import com.example.myapp.ui.home.HomeFragment;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MultiChoose#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultiChoose extends Fragment implements Serializable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mContext;
    ArrayList<Model> arrayOfApp = new ArrayList<Model>();
    // TODO: Rename and change types of parameters
    private String mParam1="";
    private String mParam2="";
    public ArrayList<String> Lista_apk=new ArrayList<>();

    public MultiChoose() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment MultiChoose.
     */
    // TODO: Rename and change types and number of parameters
    public static MultiChoose newInstance(String param1) {
        MultiChoose fragment = new MultiChoose();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);

        return fragment;
    }

    public static MultiChoose newInstance(String param1, String param2) {
        MultiChoose fragment = new MultiChoose();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Lista_apk = (ArrayList<String>)getArguments().getSerializable("key");
            if((String)getArguments().getSerializable("param")!=null)
            {
                mParam1 = (String)getArguments().getSerializable("param");
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        arrayOfApp = new ArrayList<Model>();
        final PackageManager pm = getActivity().getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        for (PackageInfo packageInfo : packages) {
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) == null) {
                // system apps

            } else {
                // user installed apps
                try {

                    Drawable dd=getContext().getPackageManager().getApplicationIcon(packageInfo.packageName);

                    Model x= new Model(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), dd, packageInfo.applicationInfo.packageName);
                    //Toast.makeText(getActivity(), "xx: "+packageInfo.applicationInfo.packageName, Toast.LENGTH_SHORT).show();

                        arrayOfApp.add(x);


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }


        }

        MultiAdapter adapter = new MultiAdapter(getActivity(), arrayOfApp);
        View root = inflater.inflate(R.layout.fragment_multi_choose, container, false);
        ListView lista_apek = (ListView)root.findViewById(R.id.lw__fr_multi_choose);
        lista_apek.setAdapter(adapter);
        if(mParam2!=""){
            adapter.setChecked(mParam2);
        }
        if(Lista_apk!=null) {
            for (String x : Lista_apk) {
                adapter.setChecked(x);
            }
        }
        Button button_anuluj= (Button)root.findViewById(R.id.btn_mutli_choose_anuluj);
        button_anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();

                if (count == 0) {
                    getActivity().onBackPressed();
                    //additional code
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        ArrayList<String> nazwy=new ArrayList<>();
        Button button_zapisz= (Button)root.findViewById(R.id.btn_mutli_choose_zapisz);
        button_zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sp = lista_apek.getCheckedItemPositions();
                Category_data xxx= new Category_data();
                xxx.addOnlyCategory(mContext,mParam1);
                int i=0;
                for(Model x:arrayOfApp)
                {
                    if(adapter.isChecked(i)){
                        Model user= (Model) lista_apek.getItemAtPosition(i);
                        nazwy.add(user.getFull_name());
                        xxx=new Category_data(user.getFull_name(),mParam1);
                        xxx.addData(mContext);
                    }
                    i++;
                }

                if(Lista_apk!=null) {
                    for (String x : Lista_apk) {
                        if(adapter.isCheckedString(x)){

                        }
                        else{
                            xxx.deletefromCategory(mContext,mParam1,x);
                        }
                    }
                }


                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();

                if (count == 0) {
                    getActivity().onBackPressed();
                    //additional code
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }





            }
        });
        Collections.sort(arrayOfApp, new Comparator<Model>() {
            public int compare(Model o1, Model o2) {
                Collator plCollator =Collator.getInstance(new Locale("pl", "PL"));
                return plCollator.compare(o1.getName(), o2.getName());
            }
        });
        return root;
    }
}