package com.example.myapp.adaptery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapp.Category_data;
import com.example.myapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends ArrayAdapter<Category_data> {

    Context mContext;
    LayoutInflater inflater;
    public List<Category_data> allCategoryItemlist;
    public ArrayList<Category_data> arraylist;

    public CategoryAdapter(@NonNull Context context, ArrayList<Category_data> data) {
        super(context,0,data);
        mContext = context;
        this.allCategoryItemlist = data;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Category_data>();
        this.arraylist.addAll(allCategoryItemlist);
    }

    @Override
    public Category_data getItem(int position) {
        return allCategoryItemlist.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false);
        }

        // Get the data item for this position
        Category_data app = getItem(position);

        // Lookup view for data population
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
        //TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time_blocked);
        //ImageView imIcon = (ImageView) convertView.findViewById(R.id.iv_blocked);
        // Populate the data into the template view using the data object
        tvCategory.setText(app.getCategory());
        return convertView;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        allCategoryItemlist.clear();
        if (charText.length() <= 1) {
            allCategoryItemlist.addAll(arraylist);
        } else {
            for (Category_data wp : arraylist) {
                if (wp.getCategory().toLowerCase(Locale.getDefault()).contains(charText)) {
                    allCategoryItemlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
