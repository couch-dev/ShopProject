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

import android.graphics.Point;
import android.util.Log;

public class AppConsts {

    public static float SCALE;
    public static int AUTO_SCROLL;

    private static final String TAG = AppConsts.class.getSimpleName();
    private static final Point REF_SCREEN = new Point(1440, 2392);
    private static final float REF_DPI = 640;
    private static final int SWIPE_AUTO_SCROLL = 25;

    /**
     * Set the devices screen size and dpi to scale all view accordingly.
     * @param screen The screen sizes (width, height).
     * @param dpi The devices screen density.
     */
    public static void setScale(Point screen, float dpi){
        float screenScale = (float) Math.max(screen.x, screen.y) / (float) REF_SCREEN.y;
        float dpiScale = AppConsts.REF_DPI / dpi;
        SCALE = screenScale * dpiScale;
        Log.d(TAG, screen.x + " x " + screen.y + ", dpi = " + dpi + "\nscreenScale = " + screenScale
                + ", dpiScale = " + dpiScale + ", scale = " + SCALE);

        AUTO_SCROLL = Math.round(SWIPE_AUTO_SCROLL * SCALE);
    }
}
