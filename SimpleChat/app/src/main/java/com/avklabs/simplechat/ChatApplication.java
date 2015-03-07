package com.avklabs.simplechat;

import android.app.Application;

import com.avklabs.simplechat.Models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by avkadam on 3/4/15.
 */
public class ChatApplication extends Application {
    final static String PARSE_APPLICATION_ID = "myLeODObP3Ns9FkB3qxSs8U05welW5TwHL54KbHH";
    final static String PARSE_CLIENT_ID = "UVr8l4bkaU38MIHcUDrZUzBfxP8KwOAarILoQC0A";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        // register parse classes
        ParseObject.registerSubclass(Message.class);

        // initialize once all classes are registered
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_ID);
    }
}
