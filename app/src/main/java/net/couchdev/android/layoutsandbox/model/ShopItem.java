package net.couchdev.android.layoutsandbox.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Tim on 26.12.2016.
 */

public class ShopItem {

    private String title;
    private String user;
    private String brief;
    private String description;
    private ArrayList<String> categories;
    private Bitmap image;
    private double price;
    private String currency;
    private boolean isDelivery;
    private boolean isPickup;

    public ShopItem(String user, String title, String brief, String description,
                    ArrayList<String> categories, Bitmap imageId, double price, String currency,
                    boolean isDelivery, boolean isPickup){
        this.user = user;
        this.title = title;
        this.brief = brief;
        this.description = description;
        this.categories = categories;
        this.image = imageId;
        this.price = price;
        this.currency = currency;
        this.isDelivery = isDelivery;
        this.isPickup = isPickup;
    }

    public boolean isValid(){
        boolean isValid = (user != null && !user.isEmpty() && title != null && !title.isEmpty()
                && brief != null && !brief.isEmpty() && currency != null && !currency.isEmpty()
                && description != null && categories != null && !categories.isEmpty() && image != null
                && price > 0 && (isDelivery || isPickup));
        return isValid;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getBrief() {
        return brief;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public Bitmap getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public boolean isPickup() {
        return isPickup;
    }
}
