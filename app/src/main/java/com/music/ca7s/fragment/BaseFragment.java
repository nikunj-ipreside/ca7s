package com.music.ca7s.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.music.ca7s.AppLevelClass;
import com.music.ca7s.SongcahngeListner;
import com.music.ca7s.activity.AuthenticationActivity;
import com.music.ca7s.activity.BaseAvtivity;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.contant.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment implements View.OnClickListener {

    public static HomeActivity homeActivity;
    AuthenticationActivity authActivity;
    public String fragmentTag="";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof HomeActivity) {
            homeActivity = (HomeActivity) context;

        }

        if (context instanceof AuthenticationActivity) {
            authActivity = (AuthenticationActivity) context;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        try{
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (!homeActivity.isFinishing()) {
//                        homeActivity.onResume();
//                    }
//                }
//            },1500);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        try{
//            homeActivity.initializeMusicService();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public List<String> getStringList(int norecord, String DisplayContent) {
//        Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
        List<String> datalist = new ArrayList<>();
        for (int i = 1; i <= norecord; i++) {
            datalist.add(DisplayContent);
        }
        return datalist;
    }

    public String geCooKie(){
        return AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
    }

    public Boolean isLogin(){
        Boolean isLogin = false;
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        if (!user_id.isEmpty()) {
            isLogin = true;
        }
        else {
            AppLevelClass.getInstance().getPreferanceHelper().putBoolean(SharedPref.IS_LOGIN,false);
            startActivity(new Intent(getContext(), AuthenticationActivity.class));
        }
        return isLogin;
    }


    public void loadImage(String url, ImageView imageView)   {
        try {
            ((BaseAvtivity) getContext()).loadImage(url,imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        homeActivity.initializeMusicService();
    }

    public void setFragment(Fragment fragment, String title){
        updateFragment(fragment, title);
    }

    public void updateFragment(Fragment fragment, String string) {
        fragmentTag = fragment.getClass().getSimpleName();
    }

    @Override
    public void onClick(View view) {

    }
}
