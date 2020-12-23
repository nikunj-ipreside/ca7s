package com.music.ca7s.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.music.ca7s.R;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.fragment.AddMusicFragment;
import com.music.ca7s.fragment.ChangePasswordFragment;
import com.music.ca7s.fragment.DiscoverFragment;
import com.music.ca7s.fragment.EditProfileFragment;
import com.music.ca7s.fragment.FavouriteFragment;
import com.music.ca7s.fragment.FollowersFragment;
import com.music.ca7s.fragment.FollowingFragment;
import com.music.ca7s.fragment.ForgotPasswordFragment;
import com.music.ca7s.fragment.LoginFragment;
import com.music.ca7s.fragment.MyMusicFragment;
import com.music.ca7s.fragment.NewSearchHistoryFragment;
import com.music.ca7s.fragment.NotificationFragment;
import com.music.ca7s.fragment.PlayListFragment;
import com.music.ca7s.fragment.ProfileFragment;
import com.music.ca7s.fragment.RequestFragment;
import com.music.ca7s.fragment.SearchFragment;
import com.music.ca7s.fragment.SearchHistoryFragment;
import com.music.ca7s.fragment.SettingsFragment;
import com.music.ca7s.fragment.SignUpFragment;
import com.music.ca7s.fragment.UploadedMusicFragment;
import com.music.ca7s.fragment.ViewProfileFragment;
import com.music.ca7s.fragment.WebviewFragment;
import com.music.ca7s.listener.iDateSelector;
import com.music.ca7s.listener.iNavigator;
import com.music.ca7s.model.PlayListModel;
import com.music.ca7s.model.profile.ProfileDatum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppNavigationActivity extends BaseAvtivity implements iNavigator {

    Date date;
    int y;
    int m;
    int d;
    Calendar newCalendar;
    DatePickerDialog datePickerDialog;


    @Override
    public void openDatePickerDialog(final iDateSelector callBack) {

        newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                y = year;
                m = monthOfYear;
                d = dayOfMonth;

                date = new Date(year - 1900, monthOfYear, dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String cdate = formatter.format(date);
//                DebugLog.e("Date " + cdate);
                callBack.onDateSelect(cdate);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void setCurrentFragmentName(String fragmentTAG) {
        super.setCurrentFragmentName(fragmentTAG);

    }

    @Override
    public void openHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finishAffinity();
    }

    public static void setStatusBarColored(AppCompatActivity context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//        {
            Window w = context.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(context);

            View view = new View(context);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.getLayoutParams().height = statusBarHeight;
            ((ViewGroup) w.getDecorView()).addView(view);
            view.setBackground(context.getResources().getDrawable(R.drawable.drawable_status_backgroud));
//        }
    }

    public static int getStatusBarHeight(AppCompatActivity context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void openHomeFragment(FragmentState fragmentState) {
        try {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentChange(new DiscoverFragment(), fragmentState);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void openAuthenticationActivity() {
        Intent i = new Intent(this, AuthenticationActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void openLoginFragment(FragmentState fragmentState) {
        fragmentChange(LoginFragment.newInstance(), fragmentState);
    }

    @Override
    public void openSignUpFragment(FragmentState fragmentState) {
        fragmentChange(SignUpFragment.newInstance(), fragmentState);

    }

    @Override
    public void openForgotPasswordFragment(FragmentState fragmentState) {
        fragmentChange(ForgotPasswordFragment.newInstance(), fragmentState);

    }

   /* @Override //delete this methoid later
    public void openNavigationDrawerFragment(FragmentState fragmentState) {
        fragmentChange(NavigationDrawerFragment.newInstance(), fragmentState);
    }*/


    @Override
    public void openFavouriteFragment(FragmentState fragmentState) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentChange(FavouriteFragment.newInstance(), fragmentState);

    }

    @Override
    public void openMyMusicFragment(FragmentState fragmentState) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentChange(MyMusicFragment.newInstance(), fragmentState);

    }

    @Override
    public void openSearchFragment(FragmentState fragmentState) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentChange(SearchFragment.newInstance(), fragmentState);
    }


    @Override
    public void openEditProfileFragment(List<ProfileDatum> profileData, FragmentState fragmentState) {
        fragmentChange(EditProfileFragment.newInstance(profileData), fragmentState);

    }

    @Override
    public void openViewProfileFragment(String sUserId, FragmentState fragmentState) {
        fragmentChange(ViewProfileFragment.newInstance(sUserId), fragmentState);

    }

    @Override
    public void openProfileFragment(FragmentState fragmentState) {
        fragmentChange(ProfileFragment.newInstance(), fragmentState);

    }


    @Override
    public void openPlayListFragment(PlayListModel listModel, FragmentState fragmentState) {
        fragmentChange(PlayListFragment.newInstance(listModel), fragmentState);

    }


    @Override
    public void openWebviewFragment(String sTitle, String webURL,FragmentState fragmentState) {
        fragmentChange(WebviewFragment.newInstance(sTitle,webURL), fragmentState);

    }

    @Override
    public void openFollowersFragment(FragmentState fragmentState) {
        fragmentChange(FollowersFragment.newInstance(), fragmentState);

    }

    @Override
    public void openFollowingFragment(FragmentState fragmentState) {
        fragmentChange(FollowingFragment.newInstance(), fragmentState);

    }

    @Override
    public void openNotificationFragment(boolean isMenuIcons, FragmentState fragmentState) {
        fragmentChange(NotificationFragment.newInstance(isMenuIcons), fragmentState);
    }

    @Override
    public void openAddMusicFragment(FragmentState fragmentState) {
        fragmentChange(AddMusicFragment.newInstance(), fragmentState);
    }

    @Override
    public void openRequestListFragment(FragmentState fragmentState) {
        fragmentChange(RequestFragment.newInstance(), fragmentState);

    }

    @Override
    public void openSearchHistoryFragment(String searchName, FragmentState fragmentState) {
        fragmentChange(SearchHistoryFragment.newInstance(searchName), fragmentState);
    }

    @Override
    public void openNewSearchHistoryFragment(String id,String searchName, FragmentState fragmentState) {
        fragmentChange(NewSearchHistoryFragment.newInstance(id,searchName), fragmentState);
    }

    @Override
    public void openUploadedMusicFragment(FragmentState fragmentState) {
        fragmentChange(UploadedMusicFragment.newInstance(), fragmentState);
    }

    @Override
    public void openSettingsFragment(FragmentState fragmentState) {
        fragmentChange(SettingsFragment.newInstance(), fragmentState);
    }

    @Override
    public void openChangePasswordFragment(FragmentState fragmentState) {
        fragmentChange(ChangePasswordFragment.newInstance(),fragmentState);
    }
}



