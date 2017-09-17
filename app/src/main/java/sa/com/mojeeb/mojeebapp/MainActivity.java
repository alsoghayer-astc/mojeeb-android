package sa.com.mojeeb.mojeebapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

import sa.com.mojeeb.mojeebapp.fragment.MyAccountFragment;
import sa.com.mojeeb.mojeebapp.fragment.TestFragment;
import sa.com.mojeeb.mojeebapp.observable.MyLocation;
import sa.com.mojeeb.mojeebapp.utils.LoginUtils;
import sa.com.mojeeb.mojeebapp.utils.SharedUtils;

public class MainActivity extends AppCompatActivity implements TestFragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {
    private static int RC_LOGIN_RESULT = 11;

    private FrameLayout fragmentContainer;
    private boolean isLoggedIn = false;
    private ProgressBar loginProgressBar;
    private FragmentManager supportFragmentManager;
    private Map<Integer,Fragment> fragments = new HashMap<>();

    private void setupFragmentsMap(){
        fragments.put(R.id.navigation_home,TestFragment.newInstance());
        fragments.put(R.id.navigation_my_account, MyAccountFragment.newInstance());
    }
    private Fragment getFragment(int id){
        return fragments.get(id);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFragment(getFragment(item.getItemId()));
                        return true;
                    case R.id.navigation_dashboard:
                        setFragment(getFragment(item.getItemId()));
                        return true;
                    case R.id.navigation_notifications:
                        setFragment(getFragment(item.getItemId()));
                        return true;
                    case R.id.navigation_my_account:
                        setFragment(getFragment(item.getItemId()));
                        return true;
                    case R.id.navigation_order_history:
                        setFragment(getFragment(item.getItemId()));
                        return true;
                }
                return false;
            };

    private void setFragment(Fragment fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragment).commit();
    }

    private void goToLogin(){
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivityForResult(loginIntent,RC_LOGIN_RESULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragmentsMap();
        setContentView(R.layout.activity_main);
        supportFragmentManager = getSupportFragmentManager();

        loginProgressBar = (ProgressBar)findViewById(R.id.login_progress_bar);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        MyLocation myLocation = MyLocation.newInstance(getBaseContext());
        SharedUtils.locationObservable = myLocation.subscribe();
        SharedUtils.myLocation = myLocation;
        LoginUtils.googleApiClient = mGoogleApiClient;
        LoginUtils.isLoggedIn().subscribe(i->loginStatusChanged(i));
    }

    private void loginStatusChanged(boolean isLoggedIn){
        if(isLoggedIn){
            //loginProgressBar.setVisibility(View.INVISIBLE);
            updateUI();
        }else
            goToLogin();
    }

    private void updateUI(){
       // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setFragment(getFragment(R.id.navigation_home));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_LOGIN_RESULT){
            if(data == null || !data.hasExtra("loginStatus"))
                goToLogin();
            else if( data.getBooleanExtra("loginStatus",false))
                updateUI();
            else
                goToLogin();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
