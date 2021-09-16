package com.example.myapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.location.Geocoder;
import android.os.CpuUsageInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Block_data implements Serializable {


    public String packagename;
    public Integer endMin;
    public Integer endHour;
    public Integer endYear;
    public Integer endMonth;
    public Integer endDay;

    public boolean na_stale;

    public Block_data(String packagename,Integer endHour, Integer endMin, Integer endYear,Integer endMonth,Integer endDay, boolean na_stale) {
        this.packagename = packagename;
        this.endHour = endHour;
        this.endMin = endMin;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.na_stale = na_stale;

    }

    public Block_data() {

    }




    public void deleteData(Context context, String name){
        ArrayList<Block_data> arrayList=this.readData(context);
        //Block_data data;

        Block_data x=null;
        int i=0;
        int j=-1;

        for(Block_data dat:arrayList)
        {

            if(dat.packagename.equals(name)){
               // x=dat;
                j=i;

            }
            i++;
        }

        if(j!=-1)
        {
            arrayList.remove(j);
        }

        else{
            Log.i("xxx", "Nie udało sie usunąć, brak danych do usuniecia");
        }

        saveAfterDelete(context,arrayList);
    }

    public boolean czy_zablokowana(String name, Context context){
        ArrayList<Block_data> lista=this.readData(context);
        for(Block_data data:lista)
        {
            if(data.packagename.equals(name))
            {
                return true;
            }
        }
        return  false;
    }

    public boolean czy_zablokowana_na_stale(String name, Context context){
        ArrayList<Block_data> lista=this.readData(context);
        for(Block_data data:lista)
        {
            if(data.packagename.equals(name) && data.na_stale==true)
            {
                return true;
            }
        }
        return  false;
    }

    public Block_data getData(String name, Context context)
    {
        ArrayList<Block_data> lista=this.readData(context);

        for(Block_data data:lista)
        {
            if(data.packagename.equals(name))
            {
                return data;
            }
        }
        return null;
    }


    public void saveAfterDelete(Context context,ArrayList<Block_data> array)
    {
        if(array.size()==0)
        {
            String path = context.getFilesDir().getAbsolutePath();
            final File suspend_f = new File(path + "/data.dat");
            boolean deleted = suspend_f.delete();
        }




            String path = context.getFilesDir().getAbsolutePath();
            Log.e("xx", path + "/data.txt");
            final File suspend_f = new File(path + "/data.dat");
            int i=0;
            for(Block_data dat:array) {
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                boolean keep = true;
                if(i==0)
                {
                try {
                    fos = new FileOutputStream(suspend_f);
                    oos = new ObjectOutputStream(fos);

                    oos.writeObject(dat);
                    oos.flush();
                } catch (Exception e) {
                    keep = false;
                    Log.e("MyAppName", "failed to suspend", e);
                } finally {
                    try {
                        if (oos != null) oos.close();
                        if (fos != null) fos.close();
                        if (keep == false) suspend_f.delete();
                    } catch (Exception e) { /* do nothing */ }
                }
            }

                else{
                    try {
                        fos = new FileOutputStream(suspend_f,true);
                        oos = new ObjectOutputStream(fos);

                        oos.writeObject(dat);
                        oos.flush();
                    } catch (Exception e) {
                        keep = false;
                        Log.e("MyAppName", "failed to suspend", e);
                    } finally {
                        try {
                            if (oos != null) oos.close();
                            if (fos != null) fos.close();
                            if (keep == false) suspend_f.delete();
                        } catch (Exception e) { /* do nothing */ }
                    }
                }
                i++;
            }


    }

    public ArrayList<Block_data> readData(Context context)
    {
        ArrayList<Block_data> array_data = new ArrayList<Block_data>();



        String path = context.getFilesDir().getAbsolutePath();
        Boolean cont=true;
        final File suspend_f = new File(path + "/data.dat");

        if( suspend_f.exists()) {
            FileInputStream inStream = null;
            try {
                inStream = new FileInputStream(suspend_f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ObjectInputStream objectInStream = null;
            try {


                while(cont){
                    objectInStream = new ObjectInputStream(inStream);
                    Block_data data=null;

                    try {
                        data=(Block_data) objectInStream.readObject();
                    }catch (ClassNotFoundException e) { e.printStackTrace(); }
                    catch (IOException e) { e.printStackTrace();}
                    if(data!=null)
                    {
                        array_data.add(data);
                    }
                    else{
                        cont=false;
                    }
                }
                try {
                    objectInStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return array_data;
    }

    public void saveState(Context context) {
        ArrayList<Block_data> arrayList=this.readData(context);
        //Block_data data;
        ;
        for(Block_data data:arrayList)
        {
            if(this.compare(data)){
                this.deleteData(context, data.packagename);
            }

        }



            String path = context.getFilesDir().getAbsolutePath();

            final File suspend_f = new File(path + "/data.dat");

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            boolean keep = true;

            try {
                fos = new FileOutputStream(suspend_f, true);
                oos = new ObjectOutputStream(fos);

                oos.writeObject(this);
                oos.flush();
            } catch (Exception e) {
                keep = false;
                Log.e("MyAppName", "failed to suspend", e);
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (fos != null) fos.close();
                    if (keep == false) suspend_f.delete();
                } catch (Exception e) { /* do nothing */ }
            }

    }

    public boolean compare(Block_data data){
        if(data.packagename.equals(this.packagename))
            {return true; }
        else
            {return  false;}
    }
}
