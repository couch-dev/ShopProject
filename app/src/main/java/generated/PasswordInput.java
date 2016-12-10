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
package generated;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;

public class PasswordInput extends TextInputLayout {

    public PasswordInput(Context context) {
        super(context);
        setPasswordVisibilityToggleEnabled(false);
    }

    public PasswordInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPasswordVisibilityToggleEnabled(false);
    }

    public PasswordInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPasswordVisibilityToggleEnabled(false);
    }

    public void showPassword(boolean show){
        // Store the current cursor position
        final int selection = getEditText().getSelectionEnd();

        if(show){
            getEditText().setTransformationMethod(null);
        } else {
            getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        // And restore the cursor position
        getEditText().setSelection(selection);
    }

}
