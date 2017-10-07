package sa.com.mojeeb.mojeebapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import sa.com.mojeeb.mojeebapp.fragment.FacebookLoginFragment;
import sa.com.mojeeb.mojeebapp.fragment.TwitterLoginFragment;
import sa.com.mojeeb.mojeebapp.utils.LoginUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity implements TwitterLoginFragment.OnFragmentInteractionListener, FacebookLoginFragment.OnFragmentInteractionListener,GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_GOOGLE_SIGN_IN = 150;
    private static final String TWITTER_FRAGMENT_TAG = "TWITTER_LOGIN_FRAGMENT";
    private static final String FACEBOOK_FRAGMENT_TAG = "FACEBOOK_LOGIN_FRAGMENT";
    private FirebaseAuth mAuth;


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        findViewById(R.id.facebook_fragment_container);

        mGoogleApiClient = LoginUtils.googleApiClient;

        findViewById(R.id.google_sign_in_button).setOnClickListener((View v)-> initiateGoogleSignIn());
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> attemptLogin());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        setupFacebookLoginFragment();
        setupTwitterLoginFragment();
    }

    private void setupTwitterLoginFragment(){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        TwitterLoginFragment twitterLoginFragment = TwitterLoginFragment.newInstance();
        fragmentTransaction.add(R.id.twitter_fragment_container,twitterLoginFragment,TWITTER_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }


    private void setupFacebookLoginFragment(){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        FacebookLoginFragment facebookLoginFragment = FacebookLoginFragment.newInstance();
        fragmentTransaction.add(R.id.facebook_fragment_container,facebookLoginFragment,"FACEBOOK_LOGIN_FRAGMENT");
        fragmentTransaction.commit();
    }


    private void initiateGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ACTIVITY RESULT","Got Result:" + requestCode);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE){
            Fragment twitterLoginFragment = getSupportFragmentManager().findFragmentByTag(TWITTER_FRAGMENT_TAG);
            twitterLoginFragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void finishActivityWithSuccessLogin(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("loginStatus", true);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e("LoginActivity", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("GOOGLESIGNIGN","Token is: " + acct.getIdToken());
            Toast.makeText(this.getBaseContext(), "Hello: " + acct.getDisplayName(),Toast.LENGTH_LONG)
                .show();
            //TODO: Save login for future app initializations, it's not saved automatically like facebook
            finishActivityWithSuccessLogin();
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.e("LoginActivity","Google sign in completed");
//        if (requestCode == RC_GOOGLE_SIGN_IN) {
//            Log.e("LoginActivity","Google sign in completed");
//        }
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        //TODO: Implement
        showProgress(true);
//        io.reactivex.Observable.timer(1000, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();


        mAuth.signInWithEmailAndPassword(mEmailView.getText().toString(), mPasswordView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN_ACTIVITY", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            finishActivityWithSuccessLogin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN_ACTIVITY", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                         //   updateUI(null);
                            showProgress(false);
                        }
                    }
                });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERROR #######",connectionResult.getErrorMessage());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

