package net.couchdev.android.layoutsandbox.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import net.couchdev.android.layoutsandbox.R;

import java.util.ArrayList;

/**
 * Created by Tim on 12.02.2017.
 */

public class SelectCategoriesDialog extends AlertDialog {

    private ResultListener listener;

    protected SelectCategoriesDialog(Context context, ResultListener resultListener) {
        super(context);
        this.listener = resultListener;
    }

    @Override
    public void show() {
        Builder builder = new Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("Select categories");
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_list, null);
        ListView list = (ListView) layout.findViewById(R.id.listDialog);
        String[] categories = getContext().getResources().getStringArray(R.array.categories);
        final CheckListAdapter adapter = new CheckListAdapter(getContext(), R.layout.item_checkable, categories);
        list.setAdapter(adapter);
        builder.setView(layout);
        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    listener.onResult(adapter.getCheckedItems());
                }
            }
        });
        builder.create().show();
    }

    public interface ResultListener{
        void onResult(ArrayList<String> result);
    }
}
