/*!*********************************************************************************
 * @file
 * @brief     Android UI timer.
 * @author    Andrzej Dziedzic
 *
 * @par Copyright
 * This code is the property of \n
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

import android.os.Handler;
import android.os.Message;

/**
 * Simple timer which works in UI thread
 */
public class UiTimer extends Handler
{
    private int      interval   = 0;
    private boolean  singleShot = false;
    private Listener listener   = null;

    /**
     * Set animation listener
     * @param l - listener
     */
    public void setListener(Listener l)
    {
        listener = l;
    }

    /**
     * Start timer. It will repeat timer events until call stop()
     * @param interval - interval in ms
     */
    public void start(int interval)
    {
        startTimer(interval, false);
    }

    /**
     * Start timer with delay. It will repeat timer events until call stop()
     * @param interval - interval in ms
     * @param delay - delay in ms
     */
    public void startDelayed(int interval, int delay)
    {
        this.interval = interval;
        final UiTimer t = new UiTimer();
        t.setListener(new UiTimer.Listener() {
            public void timeout()
            {
                startTimer();
            }
        });
        t.singleShot(delay);
    }

    /**
     * Call timer once after given timeout than stop
     * @param interval - delay in ms
     */
    public void singleShot(int interval)
    {
        startTimer(interval, true);
    }

    /**
     * Stop timer
     */
    public void stop()
    {
        interval = 0;
        removeMessages(0);
    }

    /**
     * Check timer state
     * @return true if timer is active
     */
    public boolean isActive()
    {
        return interval != 0;
    }

    @Override
    public void handleMessage(Message msg)
    {
        if (singleShot)
            stop();
        else
        {
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), this.interval);
        }
        if (listener != null)
            listener.timeout();
    }

    private void startTimer(int interval, boolean singleShot)
    {
        if (this.interval != 0)
            removeMessages(0);

        this.interval = interval;
        this.singleShot = singleShot;
        sendMessageDelayed(obtainMessage(0), this.interval);
    }

    private void startTimer()
    {
        startTimer(interval, false);
    }

    /**
     * Timer listener
     */
    public static interface Listener
    {
        /**
         * Timeout event
         */
        void timeout();
    }
}
