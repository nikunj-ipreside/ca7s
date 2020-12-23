package com.music.ca7s.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.music.ca7s.model.signup.SignUpPojo;
import com.music.ca7s.utils.AppValidation;
import com.music.ca7s.utils.DebugLog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class SignUpFragment extends BaseFragment implements iDialogClickCallback {

    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtPassword)
    TextInputEditText edtPassword;
    @BindView(R.id.edtConfirmPassword)
    TextInputEditText edtConfirmPassword;
    @BindView(R.id.chkTerms)
    CheckBox chkTerms;
    @BindView(R.id.btnSingUp)
    Button btnSingUp;
    @BindView(R.id.txtSignIn)
    TextView txtSignIn;
    Unbinder unbinder;

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        edtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (AppValidation.isEmailValidate(edtEmail.getText().toString())) {
                        callCheckUserEmailApi();
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.chkTerms, R.id.btnSingUp, R.id.txtSignIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chkTerms:
                break;
            case R.id.btnSingUp:
                if (isValidate())
                    callSignUpApi();
//                    DebugLog.e("True.....................");

                break;
            case R.id.txtSignIn:
                authActivity.onBackPressed();
                break;
        }
    }

    private void callCheckUserEmailApi() {
        authActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.EMAIL, edtEmail.getText().toString());


        NetworkCall.getInstance().callCheckEmailApi(params, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                authActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (!data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    authActivity.showSnackBar(getView(), data.getMessage());
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
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                authActivity.hideProgressDialog();
                authActivity.showSnackBar(getView(), getString(R.string.api_error_message));
//                DebugLog.e("Throwable : " + T.toString());
            }
        });

    }

    private void callSignUpApi() {
        authActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.EMAIL, edtEmail.getText().toString());
        params.put(ApiParameter.USER_PASSWORD, edtPassword.getText().toString());


        NetworkCall.getInstance().callSignUpApi(params, new iResponseCallback<SignUpPojo>() {
            @Override
            public void sucess(SignUpPojo data) {
                authActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                authActivity.showSnackBar(getView(), data.getMessage());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    openDialogSingUp(data.getNote());
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
            public void onError(Call<SignUpPojo> responseCall, Throwable T) {
                authActivity.hideProgressDialog();
                authActivity.showSnackBar(getView(), getString(R.string.api_error_message));
//                DebugLog.e("Throwable : " + T.toString());
            }
        });

    }

    private void openDialogSingUp(String sMessage) {
        DialogModel model = new DialogModel();
        model.setdDetails(sMessage);
        CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(this, model);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }

    private boolean isValidate() {
        if (!AppValidation.isEmptyFieldValidate(edtEmail.getText().toString())) {
            authActivity.showSnackBar(getView(), getString(R.string.validate_empty_email));
            return false;
        } else {
            if (!AppValidation.isEmailValidate(edtEmail.getText().toString())) {
                authActivity.showSnackBar(getView(), getString(R.string.validate_valid_email));
                return false;
            } else {
                if (!AppValidation.isEmptyFieldValidate(edtPassword.getText().toString())) {
                    authActivity.showSnackBar(getView(), getString(R.string.validate_empty_password));
                    return false;
                } else {
                    if (!AppValidation.isPasswordValidate(edtPassword.getText().toString())) {
                        authActivity.showSnackBar(getView(), getString(R.string.validate_password));
                        return false;
                    } else {
                        if (!AppValidation.isEmptyFieldValidate(edtConfirmPassword.getText().toString())) {
                            authActivity.showSnackBar(getView(), getString(R.string.validate_empty_confirm_pass));
                            return false;
                        } else {
//                            if (!AppValidation.isPasswordValidate(edtConfirmPassword.getText().toString())) {
//                                authActivity.showSnackBar(getView(), getString(R.string.validate_valid_confirm_pass));
//                                return false;
//                            } else {
                                if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                                    authActivity.showSnackBar(getView(), getString(R.string.validate_match_pass));
                                    return false;
                                } else {
                                    if (!chkTerms.isChecked()) {
                                        authActivity.showSnackBar(getView(), getString(R.string.validate_privacy_terms));
                                        return false;
                                    }
                                }
                            }
//                        }
                    }
                }
            }
        }
        return true;
    }


    @Override
    public void dialogButtonClick() {
        authActivity.onBackPressed();
    }
}
