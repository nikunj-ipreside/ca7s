package com.music.ca7s.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.music.ca7s.R;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.dialog.CommonFragmentDialog;
import com.music.ca7s.listener.iDialogClickCallback;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.DialogModel;
import com.music.ca7s.model.forgotpass.ForgotPassPojo;
import com.music.ca7s.utils.AppValidation;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;

import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class ForgotPasswordFragment extends BaseFragment implements iDialogClickCallback {


    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;


    public static ForgotPasswordFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        imgTopbarRight.setVisibility(View.GONE);
        txtTopbarTitle.setText(R.string.forgot_password_topbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.iv_close_player, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                authActivity.onBackPressed();
                break;
            case R.id.btnSubmit:
                if (isValidate())
//                    DebugLog.e("True.....................");
                    callForgotPasswordApi();
                break;
        }
    }

    private void callForgotPasswordApi() {
        authActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.EMAIL, edtEmail.getText().toString());
        String selectedLanguage = Util.changeLanguage(getContext());
        if (selectedLanguage == null || selectedLanguage.isEmpty()) {
            String language = Locale.getDefault().getLanguage();
            if (language.toString().equalsIgnoreCase("pt") || language.toString().equalsIgnoreCase("pt-br")) {
                selectedLanguage = "pt-br";
            } else {
                selectedLanguage = "en";
            }
        }else {
            if (selectedLanguage.toString().equalsIgnoreCase("Portuguese")) {
                selectedLanguage = "pt-br";
            } else {
                selectedLanguage = "en";
            }
        }
        params.put(ApiParameter.LANGUAGE, selectedLanguage);


        NetworkCall.getInstance().callForgotPasswordApi(params, new iResponseCallback<ForgotPassPojo>() {
            @Override
            public void sucess(ForgotPassPojo data) {
                authActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                // authActivity.showSnackBar(getView(), data.getMessage());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    openDialogForgotPass(data.getMessage());
                } else {
                    if (data.getMessage() != null && !data.getMessage().isEmpty()) {
                        openDialogForgotPass(data.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                authActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    authActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
//                DebugLog.e("Message : " + baseModel.getMessage().toString());

            }

            @Override
            public void onError(Call<ForgotPassPojo> responseCall, Throwable T) {
                authActivity.hideProgressDialog();
                authActivity.showSnackBar(getView(), getString(R.string.api_error_message));
//                DebugLog.e("Throwable : " + T.toString());
            }
        });

    }

    private boolean isValidate() {
        if (!AppValidation.isEmptyFieldValidate(edtEmail.getText().toString())) {
            authActivity.showSnackBar(getView(), getString(R.string.validate_empty_email));
            return false;
        } else {
            if (!AppValidation.isEmailValidate(edtEmail.getText().toString())) {
                authActivity.showSnackBar(getView(), getString(R.string.validate_valid_email));
                return false;
            }
        }
        return true;
    }

    private void openDialogForgotPass(String sMessage) {
        DialogModel model = new DialogModel();
        model.setdDetails(sMessage);
        CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(this, model);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }


    @Override
    public void dialogButtonClick() {
        authActivity.onBackPressed();
    }
}
