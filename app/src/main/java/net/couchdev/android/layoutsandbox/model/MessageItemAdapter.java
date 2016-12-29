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

public class MessageItemAdapter extends ArrayAdapter<MessageItem> {

    private int resource;

    public MessageItemAdapter(Context context, int resource, ArrayList<MessageItem> objects,
                              boolean ownerVisible) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
        }
        MessageItem item = getItem(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.profileImage);
        TextView username = (TextView) convertView.findViewById(R.id.usernameText);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessageText);

        image.setImageResource(item.getImageId());
        username.setText(item.getUsername());
        lastMessage.setText(item.getLastMessage());

        return convertView;
    }
}
