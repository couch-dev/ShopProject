package net.couchdev.android.layoutsandbox.model;

import java.util.Locale;

/**
 * Created by Tim on 26.12.2016.
 */

public enum Currency {
    EUR,
    USD,
    GBP;

    @Override
    public String toString() {
        switch(valueOf(name())){
            case EUR:
                return java.util.Currency.getInstance(Locale.GERMANY).getSymbol();
            case USD:
                return java.util.Currency.getInstance(Locale.US).getSymbol();
            case GBP:
                return java.util.Currency.getInstance(Locale.UK).getSymbol();
        }
        return "";
    }
}
