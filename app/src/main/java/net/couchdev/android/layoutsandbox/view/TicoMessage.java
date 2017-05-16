package net.couchdev.android.layoutsandbox.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;

/**
 * Created by Tim on 15.05.2017.
 */

public class TicoMessage extends RelativeLayout {

    private static TicoMessage instance;

    public TicoMessage(final Context context) {
        super(context);
        setBackgroundColor(context.getResources().getColor(R.color.tico_overlay));
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    instance.setVisibility(GONE);
                }
                return true;
            }
        });
        Point display = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(display);

        Drawable tico = context.getResources().getDrawable(R.drawable.tico_small);
        ImageView image = new ImageView(context);
        image.setId(123);
        image.setImageDrawable(tico);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        RelativeLayout.LayoutParams params = new LayoutParams(display.x/5, display.x/5);
        params.addRule(CENTER_VERTICAL);
        params.setMargins(display.x/20, 0, 0, 0);
        image.setLayoutParams(params);
        image.setVisibility(VISIBLE);
        addView(image);

        TextView textView = new TextView(context);
        textView.setId(456);
        textView.setBackgroundResource(R.drawable.tico_message_bg);
        textView.setPadding(20, 10, 10, 20);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_TOP, 123);
        params.addRule(ALIGN_BOTTOM, 123);
        params.addRule(RIGHT_OF, 123);
        params.setMargins(0, 0, display.x/20, 0);
        textView.setLayoutParams(params);
        addView(textView);
    }

    public static void show(Context context, String text){
        boolean notAdded = false;
        if(instance == null){
            instance = new TicoMessage(context);
            notAdded = true;
        }
        ((TextView) instance.findViewById(456)).setText(text);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        instance.setVisibility(VISIBLE);
        if(notAdded){
            ((Activity) instance.getContext()).getWindow().addContentView(instance, params);
        }
    }
}
