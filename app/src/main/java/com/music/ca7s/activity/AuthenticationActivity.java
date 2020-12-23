package com.music.ca7s.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.FrameLayout;

import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.fragment.LoginFragment;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticationActivity extends AppNavigationActivity {

    @BindView(R.id.fragment_container_main)
    FrameLayout fragmentContainer;

    Boolean isInternetOn = false;

    String frag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColored(this);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        AppLevelClass.getInstance().getBus().register(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            frag = extras.getString("frag");
        }

        if (AppLevelClass.getInstance().getPreferanceHelper().getBoolean(SharedPref.IS_LOGIN)) {
            openHomeActivity();
        } else {
//            openLoginFragment(FragmentState.REPLACE);
            LoginFragment login = new LoginFragment();
            Bundle b = new Bundle();
            b.putString("frag",frag);
            login.setArguments(b);
//            fragmentChange(LoginFragment.newInstance(), FragmentState.REPLACE);
            fragmentChange(login,FragmentState.REPLACE);

        }

//        openSignUpFragment(FragmentState.REPLACE);
    }


    @Override
    public void onBackPressed() {
        closeKeyboard(fragmentContainer);
        int i = getSupportFragmentManager().getBackStackEntryCount();
        if (i > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();//Close App here
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
//        super.onSaveInstanceState(outState);
    }

    @Subscribe
    public void isNetworkAvailableOrNot(Boolean isConnected) {
        if (isConnected) {
            if (dialog != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });
            }
            isInternetOn = false;

        } else {
            if (!isInternetOn) {
                isInternetOn = true;
                showProgressDialog(getString(R.string.network_error_message));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLevelClass.getInstance().getBus().unregister(this);

    }
}
