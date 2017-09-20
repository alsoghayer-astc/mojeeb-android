package sa.com.mojeeb.mojeebapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.Twitter;

public class MojeebApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Twitter.initialize(this);

    }
}
