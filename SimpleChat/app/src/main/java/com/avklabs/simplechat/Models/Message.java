package com.avklabs.simplechat.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by avkadam on 3/4/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userID";
    public static final String BODY_KEY = "body";

    public String getUserId () {
        return getString(USER_ID_KEY);
    }

    public String getBody () {
        return getString(BODY_KEY);
    }

    public void setUserId (String userID) {
        put(USER_ID_KEY, userID);
    }

    public void setBody (String body) {
        put(BODY_KEY, body);
    }
}
