package com.music.ca7s.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.login.LoginDatum;
import com.music.ca7s.model.login.LoginPojo;
import com.music.ca7s.model.login.SocialLoginData;
import com.music.ca7s.model.login.SocialLoginPojo;
import com.music.ca7s.utils.AppValidation;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class LoginFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtPassword)
    TextInputEditText edtPassword;
    @BindView(R.id.txtForgotPass)
    TextView txtForgotPass;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.txtLoginFb)
    TextView txtLoginFb;
    @BindView(R.id.txtSignUp)
    TextView txtSignUp;
    @BindView(R.id.close)
    ImageView close;
   /* @BindView(R.id.login_button)
    LoginButton loginButton;*/

   private String frag = "";
    private static final String AUTH_TYPE = "rerequest";

    private CallbackManager mCallbackManager;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();


        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle b = this.getArguments();

        if(b != null){

            frag = b.getString("frag");
        }

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.music.ca7s",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txtForgotPass, R.id.btnLogin, R.id.txtLoginFb, R.id.txtSignUp,R.id.close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtForgotPass:
                authActivity.openForgotPasswordFragment(FragmentState.REPLACE);
                break;
            case R.id.btnLogin:
                if (isValidate())
//                    DebugLog.e("True.....................");
                    callLoginApi();
                break;
            case R.id.txtLoginFb:
//                authActivity.showSnackBar(getView(), getString(R.string.under_development));
                loginWithFacecook();
                break;
            case R.id.txtSignUp:
                authActivity.openSignUpFragment(FragmentState.REPLACE);
                break;
            case R.id.close:
//                authActivity.openHomeActivity();
                if(frag.equals("yes")){
                FragmentManager fm = getFragmentManager();
//                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                } else {
                    getActivity().finish();
                }
                break;
        }
    }

    // validation method
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
                    /*if (!AppValidation.isPasswordValidate(edtPassword.getText().toString())) {
                        authActivity.showSnackBar(getView(), getString(R.string.validate_password));
                        return false;
                    }*/
                }
            }
        }
        return true;
    }

    // login api method
    private void callLoginApi() {
        authActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.EMAIL, edtEmail.getText().toString());
//        params.put(ApiParameter.EMAIL, "nd.dhokia@gmail.com");
        params.put(ApiParameter.USER_PASSWORD, edtPassword.getText().toString());
//        params.put(ApiParameter.USER_PASSWORD, "111111");

        NetworkCall.getInstance().callLoginApi(params, new iResponseCallback<LoginPojo>() {
            @Override
            public void sucess(LoginPojo data) {
                authActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<LoginDatum> loginData = data.getData();
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_ID, loginData.get(0).getUserId().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_EMAIL, loginData.get(0).getEmail().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.IS_UPDATE, data.getIsUpdate().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putBoolean(SharedPref.IS_LOGIN, true);

//                    homeActivity.openHomeFragment(FragmentState.REPLACE);
                    authActivity.openHomeActivity();
//                    getActivity().finish();
//                    getFragmentManager().popBackStack();

                } else {
                    authActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                authActivity.hideProgressDialog();
                try {
                    if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                        authActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                DebugLog.e("Message : " + baseModel.getMessage().toString());

            }

            @Override
            public void onError(Call<LoginPojo> responseCall, Throwable T) {
                authActivity.hideProgressDialog();
                authActivity.showSnackBar(getView(), getString(R.string.api_error_message));
//                DebugLog.e("Throwable : " + T.toString());
            }
        });
    }

    // login with facebook api
    private void loginWithFacecook() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                DebugLog.e("facebook: response : : " + response.toString());
                                try {
                                    String id = "",email = null,name="",user_name="";
                                    if (object.has("id")){
                                        id = object.getString("id");
                                    }

                                    if (object.has("email")){
                                        email = object.getString("email");
                                    }

                                    if (object.has("name")){
                                        name = object.getString("name");
                                        user_name = object.getString("name").trim();
                                    }
                                    hitSocialApi(id,email,name,user_name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
//                                    DebugLog.e("facebook: JSONException : : " + e.toString());

                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
//                DebugLog.e("facebook:onCancel");
                authActivity.showSnackBar(getView(), getString(R.string.cancle_facebook_login));

            }

            @Override
            public void onError(FacebookException error) {
//                DebugLog.e("facebook:onError" + error);
                authActivity.showSnackBar(getView(), error.toString());
            }
        });
    }

    private void hitSocialApi(String id, String email, String name, String user_name) {
        if (email != null && !email.isEmpty()) {
            HashMap<String, String> params = new HashMap<>();
            params.put(ApiParameter.SOCIAL_ID, id);
            params.put(ApiParameter.EMAIL, email);
            params.put(ApiParameter.NAME, name);
            params.put(ApiParameter.USER_NAME, user_name);
            callSocialLoginApi(params);
        }else {
            openEmailDialog(id,name,user_name);
        }
    }

    private void openEmailDialog(String id, String name, String user_name) {
        final Dialog dialog = new Dialog(authActivity);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setContentView(R.layout.dialog_facebook_email);
        dialog.show();
        final TextView editText = dialog.findViewById(R.id.et_name);
        Button btn_add = dialog.findViewById(R.id.btn_add);
        ImageView iv_close = dialog.findViewById(R.id.iv_close_dialog);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                String email = editText.getText().toString().trim();
//                if (!AppValidation.isEmptyFieldValidate(email)) {
//                    editText.requestFocus();
//                    authActivity.showSnackBar(getView(), getString(R.string.validate_empty_email));
//                    return;
//                } else {
//                    if (!AppValidation.isEmailValidate(email)) {
//                        editText.requestFocus();
//                        authActivity.showSnackBar(getView(), getString(R.string.validate_valid_email));
//                        return ;
//                    } else {
//                        Util.hideKeyboard(authActivity);
//                        HashMap<String, String> params = new HashMap<>();
//                        params.put(ApiParameter.SOCIAL_ID, id);
//                        params.put(ApiParameter.EMAIL, email);
//                        params.put(ApiParameter.NAME, name);
//                        params.put(ApiParameter.USER_NAME, user_name);
//                        callSocialLoginApi(params);
//                        dialog.dismiss();
//                    }
//                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // social login api
    private void callSocialLoginApi(HashMap<String, String> params) {
        authActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callSocialLoginApi(params, new iResponseCallback<SocialLoginPojo>() {
            @Override
            public void sucess(SocialLoginPojo data) {
                authActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    SocialLoginData loginData = data.getData();

                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_ID, loginData.getUserId().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_EMAIL, loginData.getEmail().toString());
                    try {
                        AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.IS_UPDATE, data.getIsUpdate().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.IS_UPDATE, AppConstants.sFalse);
                    }

                    AppLevelClass.getInstance().getPreferanceHelper().putBoolean(SharedPref.IS_LOGIN, true);

//                    startActivity(new Intent(getActivity(), HomeActivity.class));
//                    startActivity(new Intent(getActivity(), SplashActivity.class));

                    authActivity.openHomeActivity();
                } else {
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
            public void onError(Call<SocialLoginPojo> responseCall, Throwable T) {
                authActivity.hideProgressDialog();
                authActivity.showSnackBar(getView(), getString(R.string.api_error_message));
//                DebugLog.e("Throwable : " + T.toString());
            }
        });

    }

}
