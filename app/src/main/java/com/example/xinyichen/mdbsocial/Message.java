package com.example.xinyichen.mdbsocial;

/**
 * Created by xinyichen on 9/27/17.
 */

import java.io.Serializable;

public class Message implements Serializable{
    String message;
    String firebaseImageUrl;

    public Message(String message, String firebaseImageUrl) {
        this.message = message;
        this.firebaseImageUrl = firebaseImageUrl;
    }

    public Message() {}

    public String getMessage() {
        return message;
    }

    public String getFirebaseImageUrl() {
        return firebaseImageUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFirebaseImageUrl(String firebaseImageUrl) {
        this.firebaseImageUrl = firebaseImageUrl;
    }


}
