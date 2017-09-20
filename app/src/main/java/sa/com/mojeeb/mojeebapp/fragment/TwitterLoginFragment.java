package sa.com.mojeeb.mojeebapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import sa.com.mojeeb.mojeebapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TwitterLoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TwitterLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwitterLoginFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TwitterLoginButton loginButton;

    public TwitterLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TwitterLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TwitterLoginFragment newInstance() {
        TwitterLoginFragment fragment = new TwitterLoginFragment();
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ONACTIVITY_RESULT","RECIEVED" + requestCode);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_twitter_login, container, false);
        loginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e("TwiiterFragment","Twitter login success"+ result.data.getAuthToken());
                TwitterSession session = result.data;

                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        Log.e("TWITTER","SUCCESSFULLY RECIEVED EMAIL ADDRESSS: " + result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.e("TWITTER","FAILED:: EMAIL ADDRESSS: ");
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("TwiiterFragment","Twitter login FAILURE");
            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
