package com.example.myapp.adaptery;

import android.graphics.drawable.Drawable;

public class Model {


        private String name;
        private String full_name;
        private boolean selected;
        private Drawable ikona;

        public Model(String name,Drawable ikona,String full_name) {
            this.name = name;
            this.ikona=ikona;
            this.full_name=full_name;
            selected = false;
        }

        public String getName() {
            return name;
        }

        public Drawable getIkona(){
            return  ikona;
        }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setIkona(Drawable ikona)
        {
            this.ikona=ikona;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }


}
