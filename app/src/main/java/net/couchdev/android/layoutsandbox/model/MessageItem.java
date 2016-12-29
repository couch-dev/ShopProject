package net.couchdev.android.layoutsandbox.model;

/**
 * Created by Tim on 26.12.2016.
 */

public class MessageItem {

    private String username;
    private String lastMessage;
    private int imageId;

    public MessageItem(String username, String lastMessage, int imageId){
        this.username = username;
        this.lastMessage = lastMessage;
        this.imageId = imageId;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getImageId() {
        return imageId;
    }
}
