package net.couchdev.android.layoutsandbox.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tim on 05.03.2017.
 */

public class ShopItemSerializable implements Serializable{

    private String title;
    private String user;
    private String brief;
    private String description;
    private ArrayList<String> categories;
    private double price;
    private String currency;
    private boolean isDelivery;
    private boolean isPickup;

    private ShopItemSerializable(String title, String user, String brief, String description,
                                 double price, String currency, ArrayList<String> categories,
                                 boolean isDelivery, boolean isPickup){
        this.title = title;
        this.user = user;
        this.brief = brief;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.categories = categories;
        this.isDelivery = isDelivery;
        this.isPickup = isPickup;
    }

    public static ShopItemSerializable fromShopItem(ShopItem shopItem){
        return new ShopItemSerializable(shopItem.getTitle(), shopItem.getUser(),
                shopItem.getBrief(), shopItem.getDescription(),
                shopItem.getPrice(), shopItem.getCurrency(),
                shopItem.getCategories(), shopItem.isDelivery(), shopItem.isPickup());
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
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
