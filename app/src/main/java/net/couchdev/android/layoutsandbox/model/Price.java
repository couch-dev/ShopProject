package net.couchdev.android.layoutsandbox.model;

/**
 * Created by Tim on 26.12.2016.
 */

public class Price {

    private int cent;
    private Currency currency;

    public Price(int cent, Currency currency){
        this.cent = cent;
        this.currency = currency;
    }

    @Override
    public String toString() {
        double price = cent/10.0;
        return price + currency.toString();
    }
}
