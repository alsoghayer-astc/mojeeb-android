package sa.com.mojeeb.mojeebapp.utils;

import android.location.Location;

import com.facebook.AccessToken;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.common.api.GoogleApiClient;

import io.reactivex.Observable;
import sa.com.mojeeb.mojeebapp.observable.MyLocation;

public class LoginUtils {
    public static GoogleApiClient googleApiClient;


    /**
     * checks if a user is logged in or not
     * **Currently only checks Facebook login**
     * @return isLoggedIn
     */
    public static Observable<Boolean> isLoggedIn(){
        AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
        if(currentAccessToken != null && !currentAccessToken.isExpired())
            return verifyToken(currentAccessToken.getToken());

        if(googleApiClient != null && googleApiClient.isConnected()){
            //Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            return Observable.just(true);
        }
        return Observable.just(false);
    }

    /**
     * TODO: backend verification
     * @param token
     * @return
     */
    private static Observable<Boolean> verifyToken(String token){
        return Observable.just(true);
    }
}
