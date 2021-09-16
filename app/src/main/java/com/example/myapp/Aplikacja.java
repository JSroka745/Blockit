package com.example.myapp;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public abstract class Aplikacja {

    //declare private data instead of public to ensure the privacy of data field of each class
    private String name;
    private String full_name;
    //private String hometown;
    private Drawable icon;
    public Integer startHour;
    public Integer startMin;
    public Integer endMin;
    public Integer endHour;
    public Integer endYear;
    public Integer endMonth;
    public Integer endDay;
    public boolean na_stale;

    public Aplikacja(String name, Drawable icon, String full_name) {
        this.name = name;
        this.icon = icon;
        this.full_name=full_name;
    }



    public Aplikacja(String name, Drawable icon,Integer endHour, Integer endMin, Integer endYear,Integer endMonth, Integer endDay, boolean na_stale) {
        this.name = name;
        this.icon = icon;
        this.endHour = endHour;
        this.endMin = endMin;
        this.endYear = endYear;
        this.endMonth=endMonth;
        this.endDay=endDay;
        this.na_stale = na_stale;
    }

    public Aplikacja() {

    }

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    //retrieve user's name
    public String getName(){
        return name;
    }
    public String getFullName(){
        return full_name;
    }



    public Integer getendHour(){
        return endHour;
    }
    public Integer getendMin(){
        return endMin;
    }
    public Integer getendDay(){
        return endDay;
    }
    public Integer getendYear(){
        return endYear;
    }
    public Integer getendMonth(){
        return endMonth;
    }
    public Boolean getna_stale(){
        return na_stale;
    }
    public Drawable getIcon(){ return icon; }

    public abstract View getView(LayoutInflater inflater, View convertView);



}