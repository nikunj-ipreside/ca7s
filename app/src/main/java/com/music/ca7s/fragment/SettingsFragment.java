package com.music.ca7s.fragment;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.genericbottomsheet.GenericBottomModel;
import com.music.ca7s.genericbottomsheet.GenericBottomSheetDialog;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.general_settings.GeneralSettingsPojo;
import com.music.ca7s.receiver.ConnectivityReceiver;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.music.ca7s.AppLevelClass.isDataSaverDialogShowing;
import static com.music.ca7s.contant.SharedPref.LANGUAGE;

/**
 * Created by Dhara on 04-09-2018.
 */

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.txtSelectLanguage)
    TextView txtSelectLanguage;
    @BindView(R.id.txtLanguage)
    TextView txtLanguage;
    @BindView(R.id.switchNotificatio)
    Switch switchNotificatio;
    @BindView(R.id.txtChangePassword)
    TextView txtChangePassword;
    @BindView(R.id.switchPrivateAcc)
    Switch switchPrivateAcc;
    @BindView(R.id.switcheconomic)
    Switch switcheconomic;
    @BindView(R.id.txtClearSearchHistory)
    TextView txtClearSearchHistory;
    @BindView(R.id.lib_notification)
    LinearLayout lib_notification;
    @BindView(R.id.lib_private_account)
    LinearLayout lib_private_account;

    Unbinder unbinder;

    @BindView(R.id.lib_language)
    LinearLayout lib_language;

    String languageSettings = "";

    private ImageView ivWelcome;

    private String user_id = "";


    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);

        String isDataModeOn = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.DATA_MODE);
        switcheconomic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if (isChecked){
                       showDataModeDialog(getString(R.string.do_you_want_to_enable));
                   }else {
                       AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DATA_MODE, "false");
                       AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DONT_SHOW, "false");
                   }
            }
        });

        if (ConnectivityReceiver.getNetworkType(homeActivity) == ConnectivityManager.TYPE_MOBILE && HomeActivity.isDataSaverEnabled()){
            switcheconomic.setChecked(true);
        }else {
            switcheconomic.setChecked(false);
        }

        initUI();

        if(!user_id.isEmpty()) {
            callGeneralSettingsApi(1, 0);
            switchPrivateAcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    callGeneralSettingsApi(0, 0);
                }
            });
            switchNotificatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    callGeneralSettingsApi(0, 0);
                }
            });
        }
    }

    private void initUI() {
        imgTopbarRight.setVisibility(View.GONE);
        txtTopbarTitle.setText(R.string.settings);

        if(user_id.isEmpty()) {
            lib_notification.setVisibility(View.INVISIBLE);
            lib_private_account.setVisibility(View.INVISIBLE);
            txtChangePassword.setVisibility(View.INVISIBLE);
            txtClearSearchHistory.setVisibility(View.INVISIBLE);

        }

        if (Util.changeLanguage(homeActivity).equalsIgnoreCase("Portuguese")) {
            txtLanguage.setText(R.string.Portuguese);
        }else{
            txtLanguage.setText(R.string.english);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight, R.id.lib_language, R.id.txtChangePassword, R.id.txtClearSearchHistory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.openDrawer();
                break;
            case R.id.imgTopbarRight:
                break;
            case R.id.lib_language:
                openBottomSheetDialog(getString(R.string.select_language));
                break;
            case R.id.txtChangePassword:
                homeActivity.openChangePasswordFragment(FragmentState.REPLACE);
                break;
            case R.id.txtClearSearchHistory:
                showAlertDoilog();
                break;
        }
    }

    private void openBottomSheetDialog(String header) {
        Util.changeLanguage(homeActivity);
        List<GenericBottomModel> modelList = new ArrayList<>();
        List<String> listStatusItem = Arrays.asList(getResources().getStringArray(R.array.lang));
        for (int i = 0; i < listStatusItem.size(); i++) {
            GenericBottomModel model = new GenericBottomModel();
            model.setId(i + "");
            model.setItemText(listStatusItem.get(i));
            modelList.add(model);

        }
        homeActivity.openBottomSheet(header, modelList, new GenericBottomSheetDialog.RecyclerItemClick() {
            @Override
            public void onItemClick(GenericBottomModel genericBottomModel) {

                if (user_id.isEmpty()) {
                    if (genericBottomModel.getItemText().equalsIgnoreCase(getResources().getString(R.string.english))) {
                        txtLanguage.setText(R.string.english);
                        AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.LANGUAGE, "English");

                    } else {
                        txtLanguage.setText(R.string.Portuguese);
                        AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.LANGUAGE, "Portuguese");
                    }

                    if (Util.changeLanguage(homeActivity).equalsIgnoreCase("Portuguese")) {
                        txtLanguage.setText(R.string.Portuguese);
                    } else {
                        txtLanguage.setText(R.string.english);
                    }
                    getActivity().recreate();

                } else {
                    if (genericBottomModel.getItemText().equalsIgnoreCase(getResources().getString(R.string.english))) {
                        languageSettings = "English";
                        txtLanguage.setText(R.string.english);
                    } else {
                        languageSettings = "Portuguese";
                        txtLanguage.setText(R.string.Portuguese);
                    }
                    callGeneralSettingsApi(0, 3);
                }
            }

        });
    }

    private void showAlertDoilog() {
        //Uncomment the below code to Set the message and title from the strings.xml file
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        builder.setMessage(R.string.are_you_sure_yuo_want_to_delete_search_history)
                .setCancelable(false)
                .setPositiveButton(R.string.yes_, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callClearAllHistoryApi();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.no_, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getString(R.string.alert));
        alert.show();
    }

    private void callGeneralSettingsApi(final int flag, final int lang) {
        String notificationSettings = "0";
        if (switchNotificatio.isChecked())
            notificationSettings = "0";
        else
            notificationSettings = "1";

        String privateAccount = "0";
        if (switchPrivateAcc.isChecked())
            privateAccount = "1";
        else
            privateAccount = "0";

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));

        if (flag == 0) {
            params.put(ApiParameter.NOTIFICATION_SETTING, notificationSettings);
            params.put(ApiParameter.LANGUAGE_SETTING, languageSettings);
            params.put(ApiParameter.PRIVATE_ACCOUNT, privateAccount);
        }

        Log.d("language",languageSettings);

        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callGeneralSettigsApi(params, sCookie, new iResponseCallback<GeneralSettingsPojo>() {
            @Override
            public void sucess(GeneralSettingsPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    Log.d("language",data.getData().get(0).getLanguageSetting());
                    AppLevelClass.getInstance().getTutorialPrefrences().putString(LANGUAGE, data.getData().get(0).getLanguageSetting());
                    if (lang == 3) {
                        Util.changeLanguage(homeActivity.getBaseContext());
                        getActivity().recreate();

                    }

                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<GeneralSettingsPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }

    private void callClearAllHistoryApi() {

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));

        DebugLog.e("User email:" + AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_EMAIL));

        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callClearAllHistoryApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());

            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }

    public void showDataModeDialog(String message) {
        final CheckBox dontShowAgain;
//Dialog code
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LayoutInflater adbInflater = LayoutInflater.from(getContext());
        View eulaLayout = adbInflater.inflate(R.layout.data_saver_layout, null);
        dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
        dontShowAgain.setVisibility(View.GONE);
        adb.setView(eulaLayout);
        adb.setTitle(getString(R.string.attention));
        adb.setMessage(Html.fromHtml(message));
        adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "false";
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DATA_MODE, "true");
//                if (dontShowAgain.isChecked()) checkBoxResult = "true";
//                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DONT_SHOW, checkBoxResult);
                isDataSaverDialogShowing = false;
                dialog.dismiss();
                return;
            }
        });

        adb.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "false";
                String checkStatus = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.DATA_MODE);
                if (!checkStatus.toString().equalsIgnoreCase("true")){
                    switcheconomic.setChecked(false);
                }
                isDataSaverDialogShowing = false;
                dialog.dismiss();
                return;
            }
        });
        String skipMessage = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.DONT_SHOW);
//        if (!skipMessage.equals("true")) {
            if (!isDataSaverDialogShowing) {
                adb.show();
                isDataSaverDialogShowing = true;
//            }
        }
    }

}
