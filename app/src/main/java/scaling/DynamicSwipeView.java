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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * A class that alters its content and expands when the user swipes across it.
 */
public class DynamicSwipeView extends HorizontalScrollView {

    private static final String TAG = DynamicSwipeView.class.getSimpleName();

    private static final int PRESS_DELAY = 100;

    private RelativeLayout mainLayout;
    private int maxScroll;
    private int imageWidth;
    private int layoutWidth;
    private int spacing;
    private static int scrollViewWidth;
    private boolean userScroll;
    private boolean expanded;
    private boolean userClick;

    public DynamicSwipeView(Context context) {
        super(context);
        init();
    }

    public DynamicSwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicSwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initializes this view.
     */
    private void init(){
        final UiTimer pressTimer = new UiTimer();
        pressTimer.setListener(new UiTimer.Listener() {
            @Override
            public void timeout() {
                if(userClick){
                    onPress();
                }
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "ACTION_DOWN");
                        userScroll = true;
                        userClick = true;
                        pressTimer.singleShot(PRESS_DELAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "ACTION_UP");
                        userScroll = false;
                        if(userClick){
                            Log.d(TAG, "calling onClick()");
                            if(pressTimer.isActive()) {
                                onPress();
                            } else {
                                cancelClick();
                            }
                            callOnClick();
                        } else{
                            scrollBy(1, 0);
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        Log.d(TAG, "ACTION_CANCEL");
                        cancelClick();
                        return true;
                }
                return false;
            }
        });
        Log.d(TAG, "initialized");
    }

    /**
     * Cancels the click event.
     */
    private void cancelClick(){
        userClick = false;
        // TODO: mainLayout.setBackgroundColor(getContext().getResources().getColor(R.color.white));
    }

    /**
     * Called when the user presses on this {@link View}. After the press is released
     * {@link #callOnClick()} is called.
     */
    protected void onPress(){
        Log.d(TAG, "onPress()");
        // TODO: mainLayout.setBackgroundColor(getContext().getResources().getColor(R.color.light_gray));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildAt(0).getWidth() > 0){
            scrollViewWidth = getChildAt(0).getWidth();
            maxScroll = scrollViewWidth-layoutWidth;
            Log.d(TAG, "onMeasure(): measured width = " + scrollViewWidth);
        }
    }

    /**
     * Set the layout that should be altered on swipe and its initial width.
     * @param mainLayout Layout that can be altered.
     * @param width The width of the alterable layout.
     * @param imageWidth The width of the image that will fade on swipe.
     */
    public void setMainLayout(RelativeLayout mainLayout, int width, int imageWidth) {
        this.mainLayout = mainLayout;
        this.imageWidth = imageWidth;
        Log.d(TAG, "imageWidth = " + imageWidth);
        mainLayout.getLayoutParams().width = width;
        mainLayout.setMinimumWidth(width);
        layoutWidth = width;
        // TODO: ImageView image = (ImageView) mainLayout.findViewById(R.id.tourImage);
        // spacing = ((RelativeLayout.LayoutParams) image.getLayoutParams()).leftMargin;
        if(scrollViewWidth >= 0){
            post(new Runnable() {
                @Override
                public void run() {
                    scrollTo(1, 0);
                    scrollTo(0, 0);
                }
            });
        }
    }

    @Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        super.onScrollChanged(left, top, oldLeft, oldTop);

        Log.d(TAG, "onScrollChanged: maxScroll = " + maxScroll + ", left = " + left);
        if(maxScroll >= left && left >= 0) {
            cancelClick();

            /* TODO: ImageView image = (ImageView) mainLayout.findViewById(R.id.tourImage);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image.getLayoutParams();
            params.width = Math.max(imageWidth - left, 0);
            params.setMargins(spacing + left, params.topMargin, params.rightMargin, params.bottomMargin);
            if (params.width != 0){
                mainLayout.getLayoutParams().width = layoutWidth;
            }
            image.setLayoutParams(params);*/

            if (!userScroll && maxScroll > left && left > 0) {
                if (expanded && maxScroll > left && left >= maxScroll * 2 / 3 ||
                        !expanded && maxScroll > left && left >= maxScroll / 3) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            scrollBy(AppConsts.AUTO_SCROLL, 0);
                        }
                    });
                } else if (expanded && maxScroll * 2 / 3 > left && left > 0 ||
                        !expanded && maxScroll / 3 > left && left > 0) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            scrollBy(-AppConsts.AUTO_SCROLL, 0);
                        }
                    });
                }
            }
            if(left == maxScroll){
                expanded = true;
                Log.d(TAG, "expanded");
            } else if(left == 0){
                expanded = false;
                Log.d(TAG, "not expanded");
            }
        }
    }

    /**
     * Whether the alterable layout's last state was completely swiped to the left(expanded) or
     * completely swiped to the right.
     * @return {@code true} if the layout is or was last expanded,
     * {@code false} if the layout is or was last unexpanded.
     */
    public boolean isExpanded() {
        return expanded;
    }

}
