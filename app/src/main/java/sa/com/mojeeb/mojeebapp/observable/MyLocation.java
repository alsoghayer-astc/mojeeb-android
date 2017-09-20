package sa.com.mojeeb.mojeebapp.observable;

import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MyLocation {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private Context context;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    BehaviorSubject<Location> locationSubject;

    protected void setContext(Context context){
        this.context = context;
    }
    protected MyLocation(){
        locationSubject = BehaviorSubject.create();
    }

    public static MyLocation newInstance(Context context) {
        MyLocation myLocation = new MyLocation();
        myLocation.setContext(context);
        myLocation.createLocationRequest();
        myLocation.startLocationUpdates();
        return myLocation;
    }

    public void highAccuracy(){

    }

    @SuppressLint("MissingPermission")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener((Location location)-> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Log.e("MyLocation","Location Changed");
                    }
                });
    }

    public Observable<Location> subscribe(){
        return this.locationSubject.share();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.e("LocationIpdate","Location updated");
                    locationSubject.onNext(location);
                }
            };
        };

         mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback ,null);
    }
}
