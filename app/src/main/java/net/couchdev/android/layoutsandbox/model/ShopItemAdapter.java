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
import net.couchdev.android.layoutsandbox.tools.Tools;

import java.util.ArrayList;

/**
 * Created by Tim on 26.12.2016.
 */

public class ShopItemAdapter extends ArrayAdapter<ShopItem> {

    private int resource;

    public ShopItemAdapter(Context context, int resource, ArrayList<ShopItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
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
        TextView brief = (TextView) convertView.findViewById(R.id.itemBrief);
        TextView price = (TextView) convertView.findViewById(R.id.itemPrice);
        TextView tags = (TextView) convertView.findViewById(R.id.itemTags);

        image.setImageBitmap(item.getImage());
        title.setText(item.getTitle());
        owner.setText(item.getUser());
        brief.setText(item.getBrief());
        price.setText(item.getPrice() + item.getCurrency());
        tags.setText(Tools.enumerate(item.getCategories(), ", "));

        return convertView;
    }
}
