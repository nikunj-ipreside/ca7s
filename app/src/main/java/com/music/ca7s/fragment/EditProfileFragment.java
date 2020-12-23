package com.music.ca7s.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.listener.iDateSelector;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.edit_profile.EditProfilePojo;
import com.music.ca7s.model.getcity.GetCityData;
import com.music.ca7s.model.getcity.GetCityPojo;
import com.music.ca7s.model.profile.ProfileDatum;
import com.music.ca7s.model.profile.ProfilePicPojo;
import com.music.ca7s.utils.AppValidation;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class EditProfileFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.imgEdtProPic)
    ImageView imgEdtProPic;
    @BindView(R.id.txtChangePhoto)
    TextView txtChangePhoto;
    @BindView(R.id.edtProName)
    EditText edtProName;
    @BindView(R.id.edtProEmail)
    EditText edtProEmail;
    @BindView(R.id.edtProNumber)
    EditText edtProNumber;
    @BindView(R.id.spinnerCity)
    Spinner spinnerCity;
    @BindView(R.id.rbMale)
    RadioButton rbMale;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.rgMaleFemale)
    RadioGroup rgMaleFemale;
    @BindView(R.id.txtDateOfBirth)
    TextView txtDateOfBirth;

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_PERMISSION_CODE = 1;
    private String sCookie;

    String sBirthdate = "";

    public void setProfileData(List<ProfileDatum> profileData) {
        this.profileData = profileData;
    }

    List<ProfileDatum> profileData;

    public static EditProfileFragment newInstance(List<ProfileDatum> profileData) {

        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setProfileData(profileData);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        txtTopbarTitle.setText(R.string.edit_profile);
        imgTopbarRight.setImageResource(R.drawable.ic_save);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);

        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        homeActivity.lockDrawer();


        setProfile();

        callCityListApi();

    }

    private void setProfile() {
        String playlistImage = profileData.get(0).getProfilePicture();
        if (playlistImage.contains("/index.php")){
            playlistImage = playlistImage.replace("/index.php","");
        }
        if (playlistImage.contains("/public/public")){
            playlistImage = playlistImage.replace("/public/public","/public");
        }

        if (playlistImage != null && !playlistImage.isEmpty()) {
            Glide.with(getActivity())
                    .asBitmap()
                    .load(playlistImage)
                    .placeholder(R.drawable.ic_top_placeholder)
                    .transform(new CircleCrop())
                    .into(imgEdtProPic);
//            CropCircleTransformation(getActivity())
        }

        edtProName.setText(profileData.get(0).getFullName());
        edtProEmail.setText(profileData.get(0).getEmail());
        edtProNumber.setText(profileData.get(0).getMobileNumber());
        // edtCity.setText(profileData.get(0).getUserCity());
        sBirthdate = profileData.get(0).getBirthDate();

        if (profileData.get(0).getUserGender().equalsIgnoreCase(ApiParameter.MALE))
            rbMale.setChecked(true);
        else if (profileData.get(0).getUserGender().equalsIgnoreCase(ApiParameter.Female))
            rbFemale.setChecked(true);
        else
            rbMale.setChecked(true);


        if (profileData.get(0).getBirthDate().length() > 0) {
            txtDateOfBirth.setText(profileData.get(0).getBirthDate());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight, R.id.txtChangePhoto, R.id.txtDateOfBirth})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;
            case R.id.imgTopbarRight:
                homeActivity.closeKeyboard(getView());
                if (isValidate())
                    callEditProfileApi();
                break;
            case R.id.txtChangePhoto:
                homeActivity.closeKeyboard(getView());
                if (isReadStorageAllowed())
                    pickImage();
                break;
            case R.id.txtDateOfBirth:
                homeActivity.closeKeyboard(getView());

                iDateSelector iArriveDateSelect = new iDateSelector() {
                    @Override
                    public void onDateSelect(String date) {
                        sBirthdate = date;
                        txtDateOfBirth.setText(date);

                    }
                };
                homeActivity.openDatePickerDialog(iArriveDateSelect);
                view.setClickable(false);

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);

                    }
                }, 500);
                break;
        }
    }

    //Check permission is granted or not
    private boolean isReadStorageAllowed() {

        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_PERMISSION_CODE);
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == PICK_IMAGE_PERMISSION_CODE) {

            if (grantResults.length > 0) {
                boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean writeExternalFile = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (cameraPermission && readExternalFile && writeExternalFile) {
                    pickImage();
                } else {
                    homeActivity.showSnackBar(getView(), getString(R.string.to_upload_profile_pic_you_have_to_allow_storage_permission));
                }

            } else {
                homeActivity.showSnackBar(getView(), getString(R.string.to_upload_profile_pic_you_have_to_allow_storage_permission));
            }
        }
    }

    private void pickImage() {
        DebugLog.e("Pick Image");
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
        startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                if (bitmap != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(stream.toByteArray())
                            .transform(new CircleCrop())
                            .into(imgEdtProPic);

                            String encodeString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                            callUploadProfilePic(encodeString);
                        }
                    },1000);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void callUploadProfilePic(String encodeString) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.PROFILE_PICTURE, encodeString);

        NetworkCall.getInstance().callgetProfilePicApi(params, sCookie, new iResponseCallback<ProfilePicPojo>() {
            @Override
            public void sucess(ProfilePicPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
//                   String imageUrl =  data.getData().get(0).getAvatar().toString();
                           AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.PROFILE_PICTURE,
                            data.getData().get(0).getAvatar().toString());
                           NavigationDrawerFragment.getnewInstance().uploadImageProfile(homeActivity);
//                    homeActivity.setNavigationDrawer();
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.profile_image_updated));
                } else {
                    DebugLog.e("Status : " + data.getStatus());
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.profile_image_not_updated));
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<ProfilePicPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                if (T.getMessage() != null){
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }else {
                    homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
                }
            }
        });


    }

    private void callEditProfileApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FULL_NAME, edtProName.getText().toString());
        params.put(ApiParameter.EMAIL, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_EMAIL));
        params.put(ApiParameter.USER_CITY, spinnerCity.getSelectedItem().toString());
        params.put(ApiParameter.USER_PHONE, edtProNumber.getText().toString());
        params.put(ApiParameter.USER_BIRTHDATE, sBirthdate);
        params.put(ApiParameter.USER_GENDER, checkMale());

        NetworkCall.getInstance().callEditProfileApi(params, sCookie, new iResponseCallback<EditProfilePojo>() {
            @Override
            public void sucess(EditProfilePojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.FULL_NAME,
                            edtProName.getText().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_CITY,
                            spinnerCity.getSelectedItem().toString());
//                    homeActivity.setNavigationDrawer();
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.profile_updated_succesfully));
                } else {
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.nothing_to_change));
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<EditProfilePojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                if (T.getMessage() != null) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }else {
                    homeActivity.showSnackBar(getView(),homeActivity.getString(R.string.api_error_message));
                }
            }
        });
    }

    private boolean isValidate() {
        if (!AppValidation.isEmptyFieldValidate(edtProName.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_fullname));
            return false;
        } else if (!AppValidation.isEmptyFieldValidate(edtProEmail.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_email));
            return false;
        } else if (!AppValidation.isEmailValidate(edtProEmail.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_valid_email));
            return false;
        } else if (!AppValidation.isEmptyFieldValidate(edtProNumber.getText().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_number));
            return false;
        } else if (!AppValidation.isEmptyFieldValidate(spinnerCity.getSelectedItem().toString())) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_city));
            return false;
        } else if (sBirthdate.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_birthdate));
            return false;
        } else {
            return true;
        }
    }

    private void callCityListApi() {

        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callCityListApi(sCookie, new iResponseCallback<GetCityPojo>() {
            @Override
            public void sucess(GetCityPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    DebugLog.e("Success get cities");
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
        int Selected = 0;
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i).getName());
            if (data.get(i).getName().equals(profileData.get(0).getUserCity())) {
                Selected = i;
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spiner_dropdown, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(dataAdapter);
        spinnerCity.setSelection(Selected);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((TextView) parent.getChildAt(0)).setGravity(Gravity.LEFT);
                    ((TextView) parent.getChildAt(0)).setBackground(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String checkMale() {
        String result;
        if (rbMale.isChecked())
            result = ApiParameter.MALE;
        else
            result = ApiParameter.Female;
        return result;
    }

}
