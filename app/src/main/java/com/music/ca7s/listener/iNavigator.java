package com.music.ca7s.listener;

import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.PlayListModel;
import com.music.ca7s.model.SongPlayingModel;
import com.music.ca7s.model.profile.ProfileDatum;

import java.util.ArrayList;
import java.util.List;

public interface iNavigator {

    void openDatePickerDialog(iDateSelector callBack);

    void openHomeActivity();

    void openHomeFragment(FragmentState fragmentState);

    void openAuthenticationActivity();

    void openLoginFragment(FragmentState fragmentState);

    void openSignUpFragment(FragmentState fragmentState);

    void openForgotPasswordFragment(FragmentState fragmentState);

//    void openNavigationDrawerFragment(FragmentState fragmentState); //delete this methoid later

    void openFavouriteFragment(FragmentState fragmentState);

    void openMyMusicFragment(FragmentState fragmentState);

    void openSearchFragment(FragmentState fragmentState);

    void openEditProfileFragment(List<ProfileDatum> profileData, FragmentState fragmentState);

    void openViewProfileFragment(String sUserId, FragmentState fragmentState);

    void openProfileFragment(FragmentState fragmentState);

    void openPlayListFragment(PlayListModel listModel, FragmentState fragmentState);

    void openWebviewFragment(String sTitle, String webURL, FragmentState fragmentState);

    void openFollowersFragment(FragmentState fragmentState);

    void openFollowingFragment(FragmentState fragmentState);

    void openNotificationFragment(boolean isMenuIcons, FragmentState fragmentState);

    void openAddMusicFragment(FragmentState fragmentState);

    void openRequestListFragment(FragmentState fragmentState);

    void openSearchHistoryFragment(String searchName, FragmentState fragmentState);

    void openNewSearchHistoryFragment(String id,String searchName, FragmentState fragmentState);

    void openUploadedMusicFragment(FragmentState fragmentState);

    void openSettingsFragment(FragmentState fragmentState);

    void openChangePasswordFragment(FragmentState fragmentState);
}
