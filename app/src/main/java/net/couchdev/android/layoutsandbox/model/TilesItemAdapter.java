package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;
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

public class TilesItemAdapter extends ArrayAdapter<Pair<ShopItem, ShopItem>> {

    private final boolean ownerVisible;
    private int resource;

    public TilesItemAdapter(Context context, int resource, ArrayList<Pair<ShopItem, ShopItem>> objects,
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
        Pair<ShopItem, ShopItem> item = getItem(position);

        ImageView image1 = (ImageView) convertView.findViewById(R.id.itemImage1);
        TextView title1 = (TextView) convertView.findViewById(R.id.itemTitle1);
        TextView owner1 = (TextView) convertView.findViewById(R.id.itemOwner1);
        TextView price1 = (TextView) convertView.findViewById(R.id.itemPrice1);
        ImageView image2 = (ImageView) convertView.findViewById(R.id.itemImage2);
        TextView title2 = (TextView) convertView.findViewById(R.id.itemTitle2);
        TextView owner2 = (TextView) convertView.findViewById(R.id.itemOwner2);
        TextView price2 = (TextView) convertView.findViewById(R.id.itemPrice2);

        image1.setImageBitmap(item.first.getImage());
        title1.setText(item.first.getTitle());
        if(ownerVisible){
            owner1.setText(item.first.getUser());
            owner1.setVisibility(View.VISIBLE);
        } else{
            owner1.setVisibility(View.GONE);
        }
        price1.setText(item.first.getPrice() + item.first.getCurrency());
        if(item.second != null){
            image2.setImageBitmap(item.second.getImage());
            title2.setText(item.second.getTitle());
            if(ownerVisible){
                owner2.setText(item.second.getUser());
                owner2.setVisibility(View.VISIBLE);
            } else{
                owner2.setVisibility(View.GONE);
            }
            price2.setText(item.second.getPrice() + item.second.getCurrency());
        }

        return convertView;
    }
}
