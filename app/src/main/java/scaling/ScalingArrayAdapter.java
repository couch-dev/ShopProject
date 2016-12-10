/*!*********************************************************************************
 * @file
 * @brief
 * @author    Tim Reimer <reimer@bury.com>
 *
 * @par Copyright
 * This code is the property of
 *
 *     Bury GmbH & Co. KG
 *     Robert-Koch-Str. 1-7
 *     D-32584 Loehne
 *
 *     Bury Sp. z o.o.
 *     ul. Wspolna 2A
 *     PL 35-205 Rzeszow
 *
 * @par Hints
 * For history information see the commit comments in the code repository.
 *
 **********************************************************************************/
package scaling;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

public class ScalingArrayAdapter<T> extends ArrayAdapter<T> {

    private static final String TAG = ScalingArrayAdapter.class.getSimpleName();

    protected static boolean scaled[];

    public ScalingArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
        scaled = new boolean[objects.length];
    }

    public ScalingArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        scaled = new boolean[objects.size()];
    }

    protected void scaleContent(View view, int position){
        if(!scaled[position]){
            scaleContent(view, AppConsts.SCALE);
            scaled[position] = true;
        }
    }

    protected void scaleContent(View view){
        scaleContent(view, AppConsts.SCALE);
    }

    private void scaleContent(View view, float scale){
        if(view != null) {
            String tag;
            if(view.getParent() != null){
                tag = TAG + " scaling " + view.getParent().getClass().getSimpleName() + "/" + view.getClass().getSimpleName();
            } else{
                tag = TAG + " scaling " + view.getClass().getSimpleName();
            }
            if(view instanceof Button){
                Button button = (Button) view;
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.getTextSize() * scale);
                view = button;
            }
            view.setPadding(Math.round(view.getPaddingLeft() * scale), Math.round(view.getPaddingTop() * scale),
                    Math.round(view.getPaddingRight() * scale), Math.round(view.getPaddingBottom() * scale));
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null) {
                if (params.width > 0) {
                    params.width = Math.round(params.width * scale);
                }
                if (params.height > 0) {
                    params.height = Math.round(params.height * scale);
                }
                Log.d(tag, "width = " + params.width + ", height = " + params.height);
                if (params instanceof RelativeLayout.LayoutParams) {
                    RelativeLayout.LayoutParams relParams = (RelativeLayout.LayoutParams) params;
                    relParams.setMargins(Math.round(relParams.leftMargin * scale), Math.round(relParams.topMargin * scale),
                            Math.round(relParams.rightMargin * scale), Math.round(relParams.bottomMargin * scale));
                    params = relParams;
                    //Log.d(tag, "margins scaled");
                }
                if (params instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams linParams = (LinearLayout.LayoutParams) params;
                    linParams.setMargins(Math.round(linParams.leftMargin * scale), Math.round(linParams.topMargin * scale),
                            Math.round(linParams.rightMargin * scale), Math.round(linParams.bottomMargin * scale));
                    params = linParams;
                    //Log.d(tag, "margins scaled");
                }
                view.setLayoutParams(params);
            } else{
                //Log.d(tag, "params is null");
            }
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                //Log.d(tag, "is ViewGroup with " + group.getChildCount() + " children");
                for (int i = 0; i < group.getChildCount(); i++) {
                    scaleContent(group.getChildAt(i), scale);
                }
                if (view instanceof ListView) {
                   // Log.d(tag, "is ListView");
                }
            } else {
                //Log.d(tag, "scale end");
            }
        } else{
            Log.d(TAG + "scaling", "view is null");
        }
    }
}
