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
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;

/**
 * A custom {@link EditText} that handles custom fonts and has its baseline always positioned at the
 * last line of text.
 */
public class MyEditText extends EditText{

    private static final String TAG = MyEditText.class.getSimpleName();

    public MyEditText(Context context) {
        super(context);
        init(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
        init(context);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
        init(context);
    }

    /**
     * Sets some initial attributes.
     */
    private void init(Context context){
        // TODO: setHintTextColor(context.getResources().getColor(R.color.colorTextHint));
        setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        setFocusable(false);
        scaleTextSize();
    }

    /**
     * Set the custom font according to the font found in <b>attrs</b>.
     * @param context The context of this view.
     * @param attrs The attribute set containing the font attribute.
     */
    private void setCustomFont(Context context, AttributeSet attrs) {
        /* TODO: TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        String customFont = a.getString(R.styleable.MyTextView_customFont);
        setCustomFont(context, customFont);
        a.recycle();*/
    }

    /**
     * Sets the font by finding its ttf file by name.
     * @param context The context tof this view.
     * @param fontname The actual name file specifying the font.
     */
    private void setCustomFont(Context context, String fontname){
        try{
            setTypeface(Typeface.createFromAsset(context.getAssets(),  "fonts/" + fontname));
        } catch(Exception e){
            Log.e(TAG, "Could not create custom font: " + e);
        }
    }

    @Override
    public int getBaseline() {
        // set the baseline to be always the last line
        Layout layout = getLayout();
        if (layout == null) {
            return super.getBaseline();
        }
        int baselineOffset = super.getBaseline() - layout.getLineBaseline(0);
        if(layout.getLineCount() > getMaxLines()){
            return baselineOffset + layout.getLineBaseline(getMaxLines()-1);
        }
        return baselineOffset + layout.getLineBaseline(layout.getLineCount()-1);
    }

    /**
     * Scale the text with the scaling factor.
     */
    private void scaleTextSize(){
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * AppConsts.SCALE);
    }
}
