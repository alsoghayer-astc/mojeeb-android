package sa.com.mojeeb.mojeebapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MojeebApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();



//        AppEventsLogger.activateApp(this);
    }
}
