package com.music.ca7s.dialog;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.edit_profile.EditProfilePojo;
import com.music.ca7s.model.getcity.GetCityData;
import com.music.ca7s.model.getcity.GetCityPojo;
import com.music.ca7s.utils.AppValidation;
import com.music.ca7s.utils.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class UserNameRegisterFragmentDialog extends BaseFragmentDialog {

    Unbinder unbinder;
    @BindView(R.id.edtYourName)
    TextInputEditText edtYourName;
    @BindView(R.id.edtUserName)
    TextInputEditText edtUserName;
    @BindView(R.id.edtCity)
    TextInputEditText edtCity;
    @BindView(R.id.linearEditField)
    LinearLayout linearEditField;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    HomeActivity homeActivity;
    View view;
    String sCookie;
    @BindView(R.id.spinnerCity)
    Spinner spinnerCity;


    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static UserNameRegisterFragmentDialog newInstance(HomeActivity homeActivity, View view) {
        UserNameRegisterFragmentDialog fragment = new UserNameRegisterFragmentDialog();
        fragment.setHomeActivity(homeActivity);
        fragment.setView(view);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.dialog_fragment_username_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        callCityListApi();
/*
        edtUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (AppValidation.isEmptyFieldValidate(edtUserName.getText().toString())) {
                        callCheckUserNameApi();
                    }
                }
            }
        });
*/


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSubmit)
    public void onClick() {
        if (isValidate())
            callEditProfileUserNameApi();
    }

    private boolean isValidate() {
        if (!AppValidation.isEmptyFieldValidate(edtYourName.getText().toString())) {
            homeActivity.showSnackBar(view, getString(R.string.validate_your_name));
            return false;
        } else {
            if (!AppValidation.isEmptyFieldValidate(edtUserName.getText().toString())) {
                homeActivity.showSnackBar(view, getString(R.string.validate_user_name));
                return false;
            } else {
                if (!AppValidation.isEmptyFieldValidate(spinnerCity.getSelectedItem().toString()) || spinnerCity.getSelectedItem().toString().equalsIgnoreCase("Select City")) {
                    homeActivity.showSnackBar(view, getString(R.string.validate_city));
                    return false;
                }
            }
        }
        return true;
    }

    private void callCheckUserNameApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_NAME, edtUserName.getText().toString());

        NetworkCall.getInstance().callUserNameApi(params, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (!data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    homeActivity.showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
//                DebugLog.e("Message : " + baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(view, getString(R.string.api_error_message));
//                DebugLog.e("Throwable : " + T.toString());
            }
        });
    }

    private void callEditProfileUserNameApi() {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_NAME, edtUserName.getText().toString());
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FULL_NAME, edtYourName.getText().toString());
        params.put(ApiParameter.EMAIL, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_EMAIL));
        params.put(ApiParameter.USER_CITY, spinnerCity.getSelectedItem().toString());

        NetworkCall.getInstance().callEditProfileApi(params, sCookie, new iResponseCallback<EditProfilePojo>() {
            @Override
            public void sucess(EditProfilePojo data) {
                homeActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.IS_UPDATE, AppConstants.sTrue);
                    homeActivity.callSlideMenuApi();
                    dismiss();
                } else {
                    homeActivity.showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();

                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
//                DebugLog.e("Message : " + baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<EditProfilePojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(view, getString(R.string.api_error_message));
//                DebugLog.e("Throwable : " + T.toString());
            }
        });
    }


    private void callCityListApi() {

        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callCityListApi(sCookie, new iResponseCallback<GetCityPojo>() {
            @Override
            public void sucess(GetCityPojo data) {
                homeActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
//                    DebugLog.e("Success get cities");
                    addItemsOnSpinner2(data.getList().getData());
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
            public void onError(Call<GetCityPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }

    public void addItemsOnSpinner2(List<GetCityData> data) {
        List<String> list = new ArrayList<String>();
        list.add(homeActivity.getString(R.string.select_city));
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spiner_dropdown, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(dataAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    ((TextView) parent.getChildAt(0)).setGravity(Gravity.LEFT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}