package com.music.ca7s.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.viewholder.FollowersHolder;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.genericbottomsheet.GenericBottomModel;
import com.music.ca7s.genericbottomsheet.GenericBottomSheetDialog;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.followers.FollowersDatum;
import com.music.ca7s.model.followers.FollowersPojo;
import com.music.ca7s.model.search_user.SearchUserDatum;
import com.music.ca7s.model.search_user.SearchUserPojo;
import com.music.ca7s.utils.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;

public class FollowingFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.rvFollowers)
    RecyclerView rvFollowers;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.edtSearchName)
    EditText edtSearchName;
    @BindView(R.id.imgSearchClose)
    ImageView imgSearchClose;
    @BindView(R.id.relativeViewSearch)
    RelativeLayout relativeViewSearch;
    private String sCookie;

    GenericAdapter<FollowersDatum, FollowersHolder> adapter;
    List<FollowersDatum> followersData;

    List<SearchUserDatum> searchUserData;
    GenericAdapter<SearchUserDatum, FollowersHolder> searchUSerAdapter;

    public static FollowingFragment newInstance() {

        Bundle args = new Bundle();

        FollowingFragment fragment = new FollowingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        unbinder = ButterKnife.bind(this, view);

        rvFollowers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftKeyboard();
                return false;
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftKeyboard();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        txtTopbarTitle.setText(getString(R.string.following));
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        imgTopbarRight.setImageResource(R.drawable.ic_search);
        homeActivity.lockDrawer();
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
        rvFollowers.setLayoutManager(lLayout);
        callFollowingListApi();

        edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 2) {
                    callSearchUserApi();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    // following api
    private void callFollowingListApi() {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));

        NetworkCall.getInstance().callgetFollowingListApi(params, sCookie, new iResponseCallback<FollowersPojo>() {
            @Override
            public void sucess(FollowersPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<FollowersDatum> dataList = data.getList().getData();

                    setAdapter(dataList);


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
            public void onError(Call<FollowersPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }

    // set following adapter
    private void setAdapter(List<FollowersDatum> dataList) {
        followersData = dataList;

        adapter = new GenericAdapter<FollowersDatum, FollowersHolder>(R.layout.row_followers, FollowersHolder.class, followersData) {
            @Override
            public Filter getFilter() {
                return null;
            }

            @Override
            public void loadMore() {

            }

            @Override
            public void setViewHolderData(FollowersHolder holder, final FollowersDatum data, final int position) {
                String playlistImage = data.getProfilePicture();
                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .transform(new CircleCrop())
                            .into(holder.imgFollowersPic);
//                    CropCircleTransformation(getActivity())
                }

                holder.txtName.setText(data.getFullName());
                holder.txtCity.setText(data.getUserCity());


                if (data.getUserStatus().equalsIgnoreCase("follow"))
                    holder.txtPending.setText(R.string.follow);
                else if (data.getUserStatus().equalsIgnoreCase("self"))
                    holder.txtPending.setText(R.string.self);
                else if (data.getUserStatus().equalsIgnoreCase("request"))
                    holder.txtPending.setText(R.string.request);
                else if (data.getUserStatus().equalsIgnoreCase("following"))
                    holder.txtPending.setText(R.string.following);

                holder.imgDot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                                homeActivity.showSnackBar(getView(), getString(R.string.under_development));
                        openBottomSheetDialog(getString(R.string.unfollow_friend), data.getId().toString(), position);

                    }
                });

                holder.llRowRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeActivity.openViewProfileFragment(data.getId().toString(), FragmentState.REPLACE);
                    }
                });
            }
        };

        rvFollowers.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // click actions
    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight, R.id.imgSearch, R.id.imgSearchClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;
            case R.id.imgTopbarRight:
                edtSearchName.setText("");
                relativeViewSearch.setVisibility(View.VISIBLE);
                edtSearchName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtSearchName, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.imgSearch:
                /*homeActivity.closeKeyboard(getView());

                if (edtSearchName.getText().toString().length() > 0) {
                    callSearchUserApi();

                } else
                    homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_search_name));*/
                break;
            case R.id.imgSearchClose:
                homeActivity.closeKeyboard(getView());
                relativeViewSearch.setVisibility(View.GONE);
                callFollowingListApi();
                break;
        }
    }
    public  void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText())
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
    // open orderBy dropdown
    private void openBottomSheetDialog(final String header, final String following_id, final int position) {
        List<GenericBottomModel> modelList = new ArrayList<>();
        String[] arrayAction = {getString(R.string.unfollow), getString(R.string.cancel)};
        for (int i = 0; i < 2; i++) {
            GenericBottomModel model = new GenericBottomModel();
            model.setId(i + "");
            model.setItemText(arrayAction[i]);
            modelList.add(model);
        }

        homeActivity.openBottomSheet(header, modelList, new GenericBottomSheetDialog.RecyclerItemClick() {
            @Override
            public void onItemClick(GenericBottomModel genericBottomModel) {

                if (genericBottomModel.getId().equals("0")) {
//                    DebugLog.e("call here remove friend api : " + following_id);
                    callUnfollowFriend(following_id, position);

                }
            }
        });
    }

    // unfollow friend
    private void callUnfollowFriend(String following_id, final int position) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FOLLOW_ID, following_id);

        NetworkCall.getInstance().callUnfollowApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    //homeActivity.showSnackBar(getView(), data.getMessage());
                    followersData.remove(position);
                    adapter.notifyDataSetChanged();

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

    // search api
    private void callSearchUserApi() {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.SEARCH_TEXT, edtSearchName.getText().toString());

        NetworkCall.getInstance().callSearchUserApi(params, sCookie, new iResponseCallback<SearchUserPojo>() {
            @Override
            public void sucess(SearchUserPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {

                    setSearchAdapter(data.getList().getData());

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
            public void onError(Call<SearchUserPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();

                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });


    }

    // set search adapter
    private void setSearchAdapter(List<SearchUserDatum> dataList) {
        searchUserData = dataList;
        searchUSerAdapter = new GenericAdapter<SearchUserDatum, FollowersHolder>(R.layout.row_followers, FollowersHolder.class, searchUserData) {
            @Override
            public Filter getFilter() {
                return null;
            }

            @Override
            public void loadMore() {

            }

            @Override
            public void setViewHolderData(FollowersHolder holder, final SearchUserDatum data, final int position) {
                String playlistImage = data.getProfilePicture();
                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .transform(new CircleCrop())
                            .into(holder.imgFollowersPic);
//                    CropCircleTransformation(getActivity())
                }

                holder.txtName.setText(data.getFullName());
                holder.txtCity.setText(data.getUserCity());
                if (data.getUserStatus().equalsIgnoreCase("follow"))
                    holder.txtPending.setText(R.string.follow);
                else if (data.getUserStatus().equalsIgnoreCase("self"))
                    holder.txtPending.setText(R.string.self);
                else if (data.getUserStatus().equalsIgnoreCase("request"))
                    holder.txtPending.setText(R.string.request);
                else if (data.getUserStatus().equalsIgnoreCase("following"))
                    holder.txtPending.setText(R.string.following);

                holder.imgDot.setVisibility(View.GONE);

                holder.txtPending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callFollowApi(data.getId().toString());
                    }
                });

                holder.llRowRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard();
                        homeActivity.openViewProfileFragment(data.getId().toString(), FragmentState.REPLACE);
                    }
                });
            }
        };
        rvFollowers.setAdapter(searchUSerAdapter);
    }

    //follow user api
    private void callFollowApi(String sFollowId) {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FOLLOW_ID, sFollowId);

        NetworkCall.getInstance().callFollowApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
               //     homeActivity.showSnackBar(getView(), data.getMessage());
                    callSearchUserApi();
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

}
