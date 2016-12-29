package net.couchdev.android.layoutsandbox.model;

import java.util.ArrayList;

/**
 * Created by Tim on 26.12.2016.
 */

public class ShopItem {

    private String title;
    private String user;
    private String brief;
    private String description;
    private Price price;
    private ArrayList<String> tags;
    private int imageId;

    public ShopItem(String user, String title, String brief, String description,
                    Price price, ArrayList<String> tags, int imageId){
        this.user = user;
        this.title = title;
        this.brief = brief;
        this.description = description;
        this.price = price;
        this.tags = tags;
        this.imageId = imageId;
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

    public Price getPrice() {
        return price;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public int getImageId() {
        return imageId;
    }
}
