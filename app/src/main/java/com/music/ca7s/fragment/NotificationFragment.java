package com.music.ca7s.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.RecyclerItemTouchHelper;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.notification.NotificPojo;
import com.music.ca7s.model.notification.NotificationData;
import com.music.ca7s.utils.DebugLog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class NotificationFragment extends BaseFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.rvNotification)
    RecyclerView rvNotification;

    @BindView(R.id.txtNodata)
    TextView txtNodata;

    private NotificationAdapter mAdapter;

    boolean isMenuIcon = true;
    private String sCookie;

    public void setMenuIcon(boolean menuIcon) {
        isMenuIcon = menuIcon;
    }

    public static NotificationFragment newInstance(boolean isMenuIcon) {
        NotificationFragment fragment = new NotificationFragment();
        fragment.setMenuIcon(isMenuIcon);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtTopbarTitle.setText(R.string.notification);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        if (isMenuIcon) {
            imgTopbarLeft.setImageResource(R.drawable.ic_menu);
            homeActivity.unlockDrawer();
        } else {
            homeActivity.lockDrawer();
            imgTopbarLeft.setImageResource(R.drawable.ic_back);
        }
        imgTopbarRight.setImageResource(R.drawable.ic_clear);

        callNotificationApi();
    }

    private void callNotificationApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));

        NetworkCall.getInstance().callNotificationApi(params, sCookie, new iResponseCallback<NotificPojo>() {
            @Override
            public void sucess(NotificPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<NotificationData> dataList = data.getList().getData();
                    if (dataList != null && !dataList.isEmpty()) {
                        imgTopbarRight.setVisibility(View.VISIBLE);
                        txtNodata.setVisibility(View.GONE);
                        rvNotification.setVisibility(View.VISIBLE);
                        setAdapter(dataList);
                    }else {
                        imgTopbarRight.setVisibility(View.GONE);
                        rvNotification.setVisibility(View.GONE);
                        txtNodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    imgTopbarRight.setVisibility(View.GONE);
                    rvNotification.setVisibility(View.GONE);
                    txtNodata.setVisibility(View.VISIBLE);
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                imgTopbarRight.setVisibility(View.GONE);
                homeActivity.hideProgressDialog();
                rvNotification.setVisibility(View.GONE);
                txtNodata.setVisibility(View.VISIBLE);
                if (baseModel != null &&baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<NotificPojo> responseCall, Throwable T) {
                imgTopbarRight.setVisibility(View.GONE);
                homeActivity.hideProgressDialog();
                rvNotification.setVisibility(View.GONE);
                txtNodata.setVisibility(View.VISIBLE);
                if (T.getMessage() != null && !T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }
            }
        });

    }

    private void setAdapter(List<NotificationData> dataList) {
        if (dataList.size() > 0) {
            rvNotification.setVisibility(View.VISIBLE);
            txtNodata.setVisibility(View.GONE);
            mAdapter = new NotificationAdapter(getActivity(), dataList);
            LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
            rvNotification.setLayoutManager(lLayout);
            rvNotification.setAdapter(mAdapter);
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvNotification);
        } else {
            rvNotification.setVisibility(View.GONE);
            txtNodata.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationAdapter.MyViewHolder) {
            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                if (isMenuIcon) {
                    homeActivity.openDrawer();
                } else {
                    homeActivity.onBackPressed();
                }
                break;
            case R.id.imgTopbarRight:
                if (mAdapter != null)
                    new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                            .setTitle(R.string.alert)
                            .setMessage(R.string.would_you_like_to_delete_this_notification)
                            .setNegativeButton(R.string.no_, null)
                            .setPositiveButton(R.string.yes_, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    callDeleteNotificationAll();
                                }
                            }).create().show();

                break;
        }
    }

    /*********Adapter***********/
    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
        private Context context;
        public List<NotificationData> notificationData;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout viewBackground, viewForeground;
            public TextView txtTitle, txtMessage, txtDate;
            public ImageView imgLogo;

            public MyViewHolder(View view) {
                super(view);
                txtTitle = view.findViewById(R.id.txtTitle);
                viewBackground = view.findViewById(R.id.view_background);
                viewForeground = view.findViewById(R.id.view_foreground);
                imgLogo = view.findViewById(R.id.imgLogo);
                txtMessage = view.findViewById(R.id.txtMessage);
                txtDate = view.findViewById(R.id.txtDate);

                txtMessage.setVisibility(View.VISIBLE);
                txtDate.setVisibility(View.VISIBLE);
            }
        }


        public NotificationAdapter(Context context, List<NotificationData> dataList) {
            this.context = context;
            this.notificationData = dataList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_notification, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.txtTitle.setText(notificationData.get(position).getTitle());
            holder.txtMessage.setText(notificationData.get(position).getMessage());
            holder.txtDate.setText(notificationData.get(position).getCreatedAt());
            if (HomeActivity.isDataSaverEnabled()){

            }else {
                String playlistImage = notificationData.get(position).getPath();
                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(getActivity())
                            .load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(holder.imgLogo);
                }
            }

        }

        @Override
        public int getItemCount() {
            return notificationData.size();
        }

        public void removeItem(int position) {
            callDeleteNotificationOne(notificationData.get(position).getId().toString(), position);
        }

    }

    // delete one notification
    private void callDeleteNotificationOne(String messageID, final int position) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.MESSAGE_ID, messageID);

        NetworkCall.getInstance().callNotificationDeleteOneApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    mAdapter.notificationData.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    callNotificationApi();
                } else {
                    imgTopbarRight.setVisibility(View.GONE);
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

    // clear all
    private void callDeleteNotificationAll() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));

        NetworkCall.getInstance().callNotificationDeleteAllApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    if (mAdapter != null) {
                        mAdapter = null;
                        rvNotification.setAdapter(mAdapter);
                    }
                    callNotificationApi();
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


}
