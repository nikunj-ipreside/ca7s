package com.music.ca7s.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.utils.AppValidation;
import com.music.ca7s.utils.DebugLog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by Dhara on 04-09-2018.
 */

public class ChangePasswordFragment extends BaseFragment {

    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.edtOldPassword)
    TextInputEditText edtOldPassword;
    @BindView(R.id.edtNewPassword)
    TextInputEditText edtNewPassword;
    @BindView(R.id.edtCoonfirmPassword)
    TextInputEditText edtCoonfirmPassword;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    Unbinder unbinder;

    public static ChangePasswordFragment newInstance() {

        Bundle args = new Bundle();

        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_passwors, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    private void initUI() {
        imgTopbarRight.setVisibility(View.GONE);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        txtTopbarTitle.setText(R.string.change_password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close_player, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;
            case R.id.btnSubmit:
                if (isValidate())
                    callChangePasswordApi();
                break;
        }
    }


    private void callChangePasswordApi() {

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.EMAIL, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_EMAIL));
        params.put(ApiParameter.CURRENT_PASSWORD, edtOldPassword.getText().toString().trim());
        params.put(ApiParameter.NEW_PASSWORD, edtNewPassword.getText().toString().trim());
        params.put(ApiParameter.CONFIRM_PASSWORD, edtCoonfirmPassword.getText().toString().trim());

        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callChangePasswordApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    homeActivity.onBackPressed();
                    homeActivity.showSnackBar(getView(), data.getMessage());
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
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }

    private boolean isValidate() {

        if (!AppValidation.isEmptyFieldValidate(edtOldPassword.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.please_enter_your_current_password));
            return false;
        }else if (!AppValidation.isEmptyFieldValidate(edtNewPassword.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.please_enter_your_current_password));
            return false;
        } else if (!AppValidation.isPasswordValidate(edtNewPassword.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_password));
            return false;
        } else if (!AppValidation.isEmptyFieldValidate(edtCoonfirmPassword.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_confirm_pass));
            return false;
        } else if (!AppValidation.isPasswordValidate(edtCoonfirmPassword.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_valid_confirm_pass));
            return false;
        } else if (!edtNewPassword.getText().toString().equals(edtCoonfirmPassword.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_match_pass));
            return false;
        } else {
            return true;
        }
    }

}

