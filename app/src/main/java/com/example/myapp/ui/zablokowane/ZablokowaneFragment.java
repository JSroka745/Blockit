package com.example.myapp.ui.zablokowane;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapp.Aplikacja;
import com.example.myapp.Block_data;
import com.example.myapp.adaptery.BlockedAppAdapter;
import com.example.myapp.R;

import java.util.ArrayList;

public class ZablokowaneFragment extends Fragment {

    private ZablkowaneViewModel galleryViewModel;
    ArrayList<Aplikacja> arrayOfApp = new ArrayList<Aplikacja>();
    Context mContext;
    ArrayList<Block_data> array;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Block_data data = new Block_data();
        array = data.readData(mContext);
        for (Block_data dane : array) {
            try {
                //packageInfo.packageName
                Drawable dd = getContext().getPackageManager().getApplicationIcon(dane.packagename);
                final PackageManager pm = getActivity().getPackageManager();
                PackageInfo info = pm.getPackageInfo(dane.packagename, 0);
                Aplikacja x = new Aplikacja(info.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), dd, dane.endHour, dane.endMin, dane.endYear,dane.endMonth,dane.endDay, dane.na_stale) {
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
        }
        BlockedAppAdapter adapter = new BlockedAppAdapter(getActivity(), arrayOfApp);
       // galleryViewModel = new ViewModelProvider(this).get(ZablkowaneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_zablokowane, container, false);

        ListView lista = (ListView) root.findViewById(R.id.ListaApek_zablokowane);
        lista.setAdapter(adapter);
        SearchView search = (SearchView) root.findViewById(R.id.SearchView_zablokowane);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "klikniety: " + adapter.getItem(position).getName(),
                        Toast.LENGTH_SHORT).show();





            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;


            }


            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() <= 0) {
                    adapter.filter("0");
                } else {
                    adapter.filter(newText.toString());
                }
                return false;


            }
        });

        return root;
    }
}