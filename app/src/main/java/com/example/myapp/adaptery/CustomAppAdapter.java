package com.example.myapp.adaptery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Aplikacja;
import com.example.myapp.R;

public class CustomAppAdapter extends ArrayAdapter<Aplikacja> {
    List<String> mOriginalValues;
    Context mContext;
    LayoutInflater inflater;
    public List<Aplikacja> allAppItemlist;
    public ArrayList<Aplikacja> arraylist;
    public ArrayList<Integer> blocked;
    public ArrayList<Integer> blocked_new;

    public CustomAppAdapter(Context context, ArrayList<Aplikacja> app, ArrayList<Integer> blocked) {
        super(context, 0, app);
        mContext = context;
        this.allAppItemlist = app;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Aplikacja>();
        this.arraylist.addAll(allAppItemlist);
        this.blocked=blocked;
        this.blocked_new=new ArrayList<Integer>();
        if(blocked!=null){
            this.blocked_new.addAll(blocked);
        }


    }

    boolean zawiera(Aplikacja app)
    {
        for (Aplikacja x:allAppItemlist)
        {
            if(x.getName().equals(app.getName()))
            {
                return true;
            }
        }
        return false;
    }




    public static class ViewHolder {
        public  View View; }


    @Override
    public Aplikacja getItem(int position) {
        return allAppItemlist.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // Check if an existing view is being reused, otherwise inflate the view
        if (blocked!=null && blocked.contains(position)) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header, parent,false);
            holder.View=getItem(position).getView(LayoutInflater.from(getContext()), convertView);
            convertView.setTag(holder);
            return convertView;
        }
        else{
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_app, parent, false);
        }
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_app, parent, false);
        // Get the data item for this position
        Aplikacja app = getItem(position);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView imIcon = (ImageView) convertView.findViewById(R.id.imIcon);
        // Populate the data into the template view using the data object
        tvName.setText(app.getName());
        imIcon.setImageDrawable(app.getIcon());

        // Return the completed view to render on screen
        return convertView;
    }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        allAppItemlist.clear();
        blocked.clear();
        if (charText.length() <= 1) {
            allAppItemlist.addAll(arraylist);
            blocked.addAll(blocked_new);
        } else {
            int zz=0;
            for (Aplikacja wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    if(wp.getFullName()==null)
                    {
                       // allAppItemlist.add(wp);
                      //  allAppItemlist.indexOf(wp);
                       // blocked.add(allAppItemlist.indexOf(wp));
                    }

                    else if(zawiera(wp))
                    {

                    }
                    else {
                        allAppItemlist.add(wp);
                    }
                                    }
                zz++;
            }

        }
        notifyDataSetChanged();
    }
}