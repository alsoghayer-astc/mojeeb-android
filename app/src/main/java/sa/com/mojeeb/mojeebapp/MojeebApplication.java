package sa.com.mojeeb.mojeebapp;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.Twitter;

import sa.com.mojeeb.mojeebapp.utils.InstallationID;

public class MojeebApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Twitter.initialize(this);

        Log.e("APPLICATION","Installation ID: " + InstallationID.id(this));

    }
}