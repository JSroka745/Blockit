package com.example.myapp;

import android.content.Context;
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

public class Category_data implements Serializable {
    String name;
    String category;
    public Category_data(String name, String category){
        this.name=name;
        this.category=category;
    }

    public Category_data(){

    }

    public void addDefaultCategory(Context context){

            this.addOnlyCategory(context,"Default");


    }

    public  void deleteCategory(Context context,String category)
    {
        ArrayList<Category_data> arrayList=this.readData(context);
        ArrayList<Category_data> newarrayList=new ArrayList<Category_data>();
        //Block_data data;

        Block_data x=null;

        for(Category_data dat:arrayList)
        {

            if(dat.category.equals(category)){
                // x=dat;


            }

            else
            {
                newarrayList.add(dat);
            }

        }





        saveAfterDelete(context,newarrayList);
    }

    public  void deletefromCategory(Context context,String category,String name)
    {
        ArrayList<Category_data> arrayList=this.readData(context);
        ArrayList<Category_data> newarrayList=new ArrayList<Category_data>();

        for(Category_data dat:arrayList)
        {

            if(dat.category.equals(category)){
                if(dat.name.equals(name))
                {

                }
                else{
                    newarrayList.add(dat);
                }

            }

            else
            {
                newarrayList.add(dat);
            }

        }

        saveAfterDelete(context,newarrayList);
    }

    public void saveAfterDelete(Context context,ArrayList<Category_data> array) {

        if (array.size() == 0) {
            String path = context.getFilesDir().getAbsolutePath();
            final File suspend_f = new File(path + "/datacat.dat");
            boolean deleted = suspend_f.delete();
        }
        else{


        String path = context.getFilesDir().getAbsolutePath();
        final File suspend_f = new File(path + "/datacat.dat");
        int i = 0;


        for (Category_data dat : array) {

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            boolean keep = true;
            if (i == 0) {
                try {
                    fos = new FileOutputStream(suspend_f);
                    oos = new ObjectOutputStream(fos);

                    oos.writeObject(dat);
                    oos.flush();
                } catch (Exception e) {
                    keep = false;

                } finally {
                    try {
                        if (oos != null) oos.close();
                        if (fos != null) fos.close();
                        if (keep == false) suspend_f.delete();
                    } catch (Exception e) { /* do nothing */ }
                }
            } else {
                try {
                    fos = new FileOutputStream(suspend_f, true);
                    oos = new ObjectOutputStream(fos);

                    oos.writeObject(dat);
                    oos.flush();
                } catch (Exception e) {
                    keep = false;

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
    }

    public ArrayList<String> dajKategoriedlaApki(Context context,String name){
        ArrayList<Category_data> list=this.readData(context);
        ArrayList<String> slist= new ArrayList<String>();
        for(Category_data data : list)
        {
            if(data.name.equals(name) )
            {
                slist.add(data.category);
            }
        }
        return slist;
    }

    public ArrayList<String> getAppsByCategory(Context context,String category){
        ArrayList<Category_data> list=this.readData(context);
        ArrayList<String> slist= new ArrayList<String>();
        for(Category_data data : list)
        {
            if(data.getCategory().equals(category) && data.name!="")
            {
                slist.add(data.name);
            }
        }
        return slist;
    }



    public String getCategory(){
        return this.category;
    }

    public void addOnlyCategory(Context context,String category) {
        ArrayList<String> arrayList=this.getOnlyAllCategory(context);
        //Block_data data;
        Boolean x= false;
        for(String data:arrayList)
        {
            if(category.equals(data)){
                x=true;
            }

        }
        if(!x) {


            String path = context.getFilesDir().getAbsolutePath();
            final File suspend_f = new File(path + "/datacat.dat");

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            boolean keep = true;

            try {
                fos = new FileOutputStream(suspend_f, true);
                oos = new ObjectOutputStream(fos);
                Category_data cc=new Category_data("",category);
                oos.writeObject(cc);
                oos.flush();
            } catch (Exception e) {
                keep = false;

            } finally {
                try {
                    if (oos != null) oos.close();
                    if (fos != null) fos.close();
                    if (keep == false) suspend_f.delete();
                } catch (Exception e) { /* do nothing */ }
            }
        }
    }

    public ArrayList<String> getOnlyAllCategory(Context context)
    {
        ArrayList<String> array_data = new ArrayList<String>();
        String path = context.getFilesDir().getAbsolutePath();
        Boolean cont=true;
        final File suspend_f = new File(path + "/datacat.dat");
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
                    Category_data data=null;

                    try {
                        data=(Category_data) objectInStream.readObject();
                    }catch (ClassNotFoundException e) { e.printStackTrace(); }
                    catch (IOException e) { e.printStackTrace();}
                    if(data!=null)
                    {
                        if(!array_data.contains(data.category))
                        {
                            array_data.add(data.category);
                        }

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

    public ArrayList<Category_data> readData(Context context){
        ArrayList<Category_data> array_data = new ArrayList<Category_data>();


        String path = context.getFilesDir().getAbsolutePath();
        Boolean cont=true;
        final File suspend_f = new File(path + "/datacat.dat");

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
                    Category_data data=null;

                    try {
                        data=(Category_data) objectInStream.readObject();
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

    public void addData(Context context) {
        ArrayList<Category_data> arrayList=this.readData(context);
        //Block_data data;
        Boolean x= false;
        for(Category_data data:arrayList)
        {
            if(this.compare(data)){
                x=true;
            }

        }
        if(!x) {


            String path = context.getFilesDir().getAbsolutePath();
            final File suspend_f = new File(path + "/datacat.dat");

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

            } finally {
                try {
                    if (oos != null) oos.close();
                    if (fos != null) fos.close();
                    if (keep == false) suspend_f.delete();
                } catch (Exception e) { /* do nothing */ }
            }
        }
    }




    public boolean compare(Category_data data){
        if(data.name.equals(this.name) && (data.category.equals(this.category)))
        {return true; }
        else
        {return  false;}
    }

    public boolean compare_category(Category_data data){
        if(data.category.equals(this.category))
        {return true; }
        else
        {return  false;}
    }


}
