package com.example.myapp;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Header extends Aplikacja {
    private final String         name;

    public Header(String name) {
        this.name = name;


    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return null;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.header, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.separator);
        text.setText(name);

        return view;
    }
}
