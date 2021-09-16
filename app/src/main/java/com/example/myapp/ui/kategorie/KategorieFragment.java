package com.example.myapp.ui.kategorie;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.AppInfo;
import com.example.myapp.Block_data;
import com.example.myapp.MultiChoose;
import com.example.myapp.adaptery.CategoryAdapter;
import com.example.myapp.CategoryInfo;
import com.example.myapp.Category_data;
import com.example.myapp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class KategorieFragment extends Fragment {

    private KategorieViewModel slideshowViewModel;
    ArrayList<Category_data> arrayOfApp = new ArrayList<Category_data>();
    Context mContext;
    ArrayList<String> array;


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

    public void make_alert(String title,String message)
    {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                    }
                })
                .show();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Category_data data = new Category_data();
        array = data.getOnlyAllCategory(mContext);
        for (String dane : array) {
            Category_data x=new Category_data("",dane);
            arrayOfApp.add(x);

        }
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), arrayOfApp);
        slideshowViewModel = new ViewModelProvider(this).get(KategorieViewModel.class);
        View root = inflater.inflate(R.layout.fragment_kategorie, container, false);
        ListView lista = (ListView) root.findViewById(R.id.ListaKategorii);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "klikniety: " ,
                        Toast.LENGTH_SHORT).show();

                CategoryInfo fragment = CategoryInfo.newInstance(adapter.getItem(position).getCategory());

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                //((ViewGroup)(getView().getParent())).getId()


                fragmentTransaction.commit();



            }
        });

        Button btn_dodaj_kategorie= (Button)root.findViewById(R.id.fr_dodaj_kategorie);
        btn_dodaj_kategorie.setOnClickListener(new View.OnClickListener() {
            String m_Text;
            @Override
            public void onClick(View v) {



                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Nazwa kategorii");
                        builder.setMessage("Podaj nazwe dla kategorii");
// Set up the input
                        final EditText input = new EditText(mContext);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

// Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text = input.getText().toString();
                                Category_data c=new Category_data();
                                ArrayList<String> arr= new ArrayList<>();
                                arr.addAll(c.getOnlyAllCategory(mContext));
                                Boolean czy=false;
                                for(String z : arr){
                                    if(z.equals(m_Text))
                                    {
                                        czy=true;
                                    }
                                }
                                if(czy){
                                    make_alert("Kategoria "+m_Text+" już istnieje", null);

                                }
                                else{
                                    MultiChoose fragment = MultiChoose.newInstance(m_Text);

                                    FragmentTransaction fragmentTransaction = getActivity()
                                            .getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                                    fragmentTransaction.commit();
                                }



                                //getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();





        }});




        return root;


    }
}