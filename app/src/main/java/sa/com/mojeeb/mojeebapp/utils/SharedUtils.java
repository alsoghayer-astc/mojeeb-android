package sa.com.mojeeb.mojeebapp.utils;

import android.location.Location;

import io.reactivex.Observable;
import sa.com.mojeeb.mojeebapp.observable.MyLocation;

public class SharedUtils {
    public static Observable<Location> locationObservable;
    public static MyLocation myLocation;
}
