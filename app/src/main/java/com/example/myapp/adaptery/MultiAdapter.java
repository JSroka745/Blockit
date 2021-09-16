package com.example.myapp.adaptery;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;

public class MultiAdapter extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final Activity context;

    public MultiAdapter(Activity context, List<Model> list) {
        super(context, R.layout.item_in_multichoose, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected ImageView icon;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.item_in_multichoose, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.multi_item_text);
            viewHolder.icon = (ImageView) view.findViewById(R.id.iv_multi_icon);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.multi_item_check);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            Model element = (Model) viewHolder.checkbox
                                    .getTag();
                            element.setSelected(buttonView.isChecked());

                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.icon.setImageDrawable(list.get(position).getIkona());
        holder.text.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }

    public boolean isChecked(int position) {
        return list.get(position).isSelected();
    }
    public boolean isCheckedString(String name) {

        for(Model x:list){
            if(x.getFull_name().equals(name))
            {

                if(x.isSelected()){
                    return true;
                }
            }
        }
        return false;
    }
    public void setChecked(String name) {
        for(Model x:list){
            if(x.getFull_name().equals(name))
            {

                x.setSelected(true);
            }
        }

    }


}