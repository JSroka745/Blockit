package com.example.myapp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.content.pm.PackageInfo;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.Aplikacja;
import com.example.myapp.AppInfo;
import com.example.myapp.Category_data;
import com.example.myapp.adaptery.CustomAppAdapter;
import com.example.myapp.Header;
import com.example.myapp.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    PackageManager pm;
    ArrayList<Aplikacja> arrayOfApp = new ArrayList<Aplikacja>();


    private HomeViewModel homeViewModel;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();


        return fragment;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onStart() {


        super.onStart();
    }


    private boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        final PackageManager pm = getActivity().getPackageManager();
        arrayOfApp = new ArrayList<Aplikacja>();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        ArrayList<String> arraylist= new ArrayList<String>();
        Category_data category_data=new Category_data();
        arraylist=category_data.getOnlyAllCategory(getContext());
        ArrayList<String> arraylist_final= new ArrayList<String>();
        for(String category : arraylist)
        {
            arraylist_final.add(",");
            arraylist_final.add(category);
            ArrayList<String> puf=category_data.getAppsByCategory(getContext(),category);
            if(puf.size()!=0 && puf.get(0).equals(""))
            {
                puf.remove(0);

            }
            arraylist_final.addAll(puf);

        }

        boolean przycinek=false;
        int licz=0;
        ArrayList<Integer> blocked=new ArrayList<>();
        for(String text : arraylist_final)
        {

            if(text.equals(","))
            {
                przycinek=true;
            }
            else if(przycinek==true)
            {
                blocked.add(licz);
                arrayOfApp.add(new Header(text));
                przycinek=false;
                licz++;
            }

            else {
                try {
                    PackageInfo pinfo= pm.getPackageInfo(text,0);
                    Drawable dd=getContext().getPackageManager().getApplicationIcon(pinfo.packageName);
                    Aplikacja x= new Aplikacja(pinfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), dd, pinfo.applicationInfo.packageName) {
                        @Override
                        public View getView(LayoutInflater inflater, View convertView) {
                            return null;
                        }
                    };
                    arrayOfApp.add(x);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                licz++;
            }

        }

        arrayOfApp.add(new Header("Aplikacje"));
        blocked.add(licz);
        for (PackageInfo packageInfo : packages) {
            //  if (isSystemPackage(packageInfo)) {
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) == null) {
                // system apps

            } else {
                // user installed apps
                try {

                    Drawable dd=getContext().getPackageManager().getApplicationIcon(packageInfo.packageName);

                    Aplikacja x= new Aplikacja(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), dd, packageInfo.applicationInfo.packageName) {
                        @Override
                        public View getView(LayoutInflater inflater, View convertView) {
                            return null;
                        }
                    };
                    //Toast.makeText(getActivity(), "xx: "+packageInfo.applicationInfo.packageName, Toast.LENGTH_SHORT).show();
                    boolean zawiera=false;
                    for(Aplikacja app:arrayOfApp)
                    {
                        if(app.getName().equals(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString()))
                        {   zawiera=true;
                            break;
                        }
                    }
                    if(zawiera)
                    {

                    }
                    else {
                        arrayOfApp.add(x);
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }


        }
        ArrayList<Integer> blocked_sort=new ArrayList<>();
        blocked_sort.addAll(blocked);
        blocked_sort.add(arrayOfApp.size());
        int poprzednia= blocked.get(0);
        int przed_poprzednia=0;

        for(int i=0;i<blocked_sort.size();i++)
        {
            if(i==0){
                poprzednia=blocked_sort.get(i);
            }
            else if(i==1)
            {
                przed_poprzednia=poprzednia;
                poprzednia=blocked_sort.get(i);
                Collections.sort(arrayOfApp.subList(przed_poprzednia+1,poprzednia), new Comparator<Aplikacja>() {
                    public int compare(Aplikacja o1, Aplikacja o2) {
                        Collator plCollator =Collator.getInstance(new Locale("pl", "PL"));
                        //return o1.getName().compareTo(o2.getName());
                        return  plCollator.compare(o1.getName(), o2.getName());
                    }
            });
        }

            else{
                przed_poprzednia=poprzednia;
                poprzednia=blocked_sort.get(i);
                Collections.sort(arrayOfApp.subList(przed_poprzednia+1,poprzednia), new Comparator<Aplikacja>() {
                    public int compare(Aplikacja o1, Aplikacja o2) {
                        Collator plCollator =Collator.getInstance(new Locale("pl", "PL"));
                        //return o1.getName().compareTo(o2.getName());
                        return  plCollator.compare(o1.getName(), o2.getName());
                    }
                });
            }
        }

        CustomAppAdapter adapter = new CustomAppAdapter(getActivity(), arrayOfApp, blocked);






        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lista_apek = (ListView)root.findViewById(R.id.ListaApek);
        lista_apek.setAdapter(adapter);
        SearchView search = (SearchView)root.findViewById(R.id.SearchView);
        lista_apek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (blocked.contains(position)) {

                }

                else{



                //.................................

                AppInfo fragment = AppInfo.newInstance(adapter.getItem(position).getFullName());
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.strona_glowna, fragment).addToBackStack(null);
                fragmentTransaction.commit();


            }
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // search.clearFocus();


                return false;


            }



            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length()<=0)
                {
                    adapter.filter("0");
                }
                else {
                    adapter.filter(newText.toString());
                }
                return false;


            }
        });




        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //  textView.setText(s);

            }
        });
        return root;


    }

}