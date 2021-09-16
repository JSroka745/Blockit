package com.example.myapp.adaptery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.Aplikacja;
import com.example.myapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BlockedAppAdapter extends ArrayAdapter<Aplikacja> {

        Context mContext;
        LayoutInflater inflater;
        public List<Aplikacja> allAppItemlist;
        public ArrayList<Aplikacja> arraylist;

        public BlockedAppAdapter(Context context, ArrayList<Aplikacja> app) {
            super(context, 0, app);
            mContext = context;
            this.allAppItemlist = app;
            inflater = LayoutInflater.from(mContext);
            this.arraylist = new ArrayList<Aplikacja>();
            this.arraylist.addAll(allAppItemlist);

        }



    @Override
        public Aplikacja getItem(int position) {
            return allAppItemlist.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.blokced_item_app, parent, false);
            }

            // Get the data item for this position
            Aplikacja app = getItem(position);

            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name_blocked);
            TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time_blocked);
            ImageView imIcon = (ImageView) convertView.findViewById(R.id.iv_blocked);
            // Populate the data into the template view using the data object
            tvName.setText(app.getName());
            imIcon.setImageDrawable(app.getIcon());
            if(app.getna_stale())
            {
                tvTime.setText("na stałe");
            }


            else{
                Calendar mcurrentTime = Calendar.getInstance();
                int startHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int startMin = mcurrentTime.get(Calendar.MINUTE);
                int startYear=mcurrentTime.get(Calendar.YEAR);
                int startMonth=mcurrentTime.get(Calendar.MONTH);
                int startDay=mcurrentTime.get(Calendar.DAY_OF_MONTH);
                int endYear=app.getendYear();
                int endMonth=app.getendMonth();
                int endDay=app.getendDay();
                int endHour=app.getendHour();
                int endMin=app.getendMin();
                int pozostalyYear=0;
                int pozostalyMonth=0;
                int pozostalyDay=0;
                int pozostalyHour=0;
                int pozostalyMin=0;
                String tekst_do_tv="";

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
                    if(pozostalyYear>1)
                    {
                        tekst_do_tv+=pozostalyYear+"l ";
                    }

                    else{
                        tekst_do_tv+=pozostalyYear+"r ";
                    }

                }

                if(pozostalyMonth!=0)
                {
                    tekst_do_tv+=pozostalyMonth+"m ";
                }

                if(pozostalyDay!=0)
                {
                    tekst_do_tv+=pozostalyDay+"d ";
                }

                if(pozostalyHour!=0)
                {
                    tekst_do_tv+=pozostalyHour+"godz ";
                }

                else{
                    tekst_do_tv+="0 godz ";
                }

                if(pozostalyMin!=0)
                {
                    if(pozostalyMin<10)
                    {
                        tekst_do_tv+="0"+pozostalyMin+"min ";
                    }
                    else if(pozostalyMin==0)
                    {
                        tekst_do_tv+="0 min";
                    }
                    else{
                        tekst_do_tv+=pozostalyMin+"min ";
                    }
                }




                tvTime.setText(tekst_do_tv);
            }



            // Return the completed view to render on screen
            return convertView;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            allAppItemlist.clear();
            if (charText.length() <= 1) {
                allAppItemlist.addAll(arraylist);
            } else {
                for (Aplikacja wp : arraylist) {
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        allAppItemlist.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

}
