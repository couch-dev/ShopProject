package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;

import java.util.ArrayList;

/**
 * Created by Tim on 26.12.2016.
 */

public class SliderItemAdapter extends ArrayAdapter<ShopItem> {

    private final boolean ownerVisible;
    private int resource;

    public SliderItemAdapter(Context context, int resource, ArrayList<ShopItem> objects,
                             boolean ownerVisible) {
        super(context, resource, objects);
        this.resource = resource;
        this.ownerVisible = ownerVisible;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
        }
        ShopItem item = getItem(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.itemImage);
        TextView title = (TextView) convertView.findViewById(R.id.itemTitle);
        TextView owner = (TextView) convertView.findViewById(R.id.itemOwner);
        TextView price = (TextView) convertView.findViewById(R.id.itemPrice);

        image.setImageBitmap(item.getImage());
        title.setText(item.getTitle());
        if(ownerVisible){
            owner.setText(item.getUser());
            owner.setVisibility(View.VISIBLE);
        } else{
            owner.setVisibility(View.GONE);
        }
        price.setText(item.getPrice() + item.getCurrency());

        return convertView;
    }
}
