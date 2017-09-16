package sa.com.mojeeb.mojeebapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import sa.com.mojeeb.mojeebapp.sa.com.mojeeb.mojeebapp.fragment.TestFragment;
import sa.com.mojeeb.mojeebapp.utils.LoginUtils;

public class MainActivity extends AppCompatActivity implements TestFragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {
    private static int RC_LOGIN_RESULT = 11;

    private FrameLayout fragmentContainer;
    private boolean isLoggedIn = false;
    private ProgressBar loginProgressBar;
    private FragmentManager supportFragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(TestFragment.newInstance());
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_my_account:
                    //mTextMessage.setText(R.string.title_my_account);
                    return true;
                case R.id.navigation_order_history:
                    //mTextMessage.setText(R.string.title_order_history);
                    return true;
            }
            return false;
        }
    };

    private void setFragment(Fragment fragment){
        supportFragmentManager.beginTransaction()
                .add(fragment,"SomeFragment").commit();
    }

    private void goToLogin(){
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivityForResult(loginIntent,RC_LOGIN_RESULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportFragmentManager = getSupportFragmentManager();

        loginProgressBar = (ProgressBar)findViewById(R.id.login_progress_bar);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
