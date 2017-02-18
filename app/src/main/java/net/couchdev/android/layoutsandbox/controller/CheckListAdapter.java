package net.couchdev.android.layoutsandbox.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import net.couchdev.android.layoutsandbox.R;

import java.util.ArrayList;

/**
 * Created by Tim on 12.02.2017.
 */

public class CheckListAdapter extends ArrayAdapter<String> {

    private boolean[] checked;
    private int resource;

    public CheckListAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.resource = resource;
        checked = new boolean[objects.length];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
        }
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
        checkBox.setChecked(checked[position]);
        checkBox.setText(getItem(position));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked[position] = isChecked;
            }
        });
        return convertView;
    }

    public ArrayList<String> getCheckedItems(){
        ArrayList<String> items = new ArrayList<>();
        for(int i=0; i<checked.length; i++){
            if(checked[i]) {
                items.add(getItem(i));
            }
        }
        return items;
    }
}
