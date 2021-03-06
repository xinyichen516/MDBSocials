package com.example.xinyichen.mdbsocial;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinyichen on 9/27/17.
 */

public class Social implements Serializable {
    String title;
    String firebaseImageUrl;
    String hostEmail;
    String date;
    String description;
    int numInterested;
    String key;
    long timestamp;

    Social(String title, String firebaseImageUrl, String date, String description) {
        this.title = title;
        this.firebaseImageUrl = firebaseImageUrl;
        this.date = date;
        this.numInterested = 0;
        this.description = description;
        this.timestamp = -1 * new Date().getTime();
    }

    public Social() {}

    public String getTitle() {
        return title;
    }

    public String getFirebaseImageUrl() {
        return firebaseImageUrl;
    }

    public String getDate() {
        return date;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public int getNumInterested() {
        return numInterested;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFirebaseImageUrl(String firebaseImageUrl) {
        this.firebaseImageUrl = firebaseImageUrl;
    }

}
