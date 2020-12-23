package com.music.ca7s.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fenchtose.tooltip.Tooltip;
import com.fenchtose.tooltip.TooltipAnimation;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.RecyclerItemTouchHelperUploaded;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.viewholder.UploadedMusicHolder;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.uploaded_music.UploadedDatum;
import com.music.ca7s.model.uploaded_music.UploadedPojo;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.utils.DebugLog;

import java.io.File;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;


public class UploadedMusicFragment extends BaseFragment implements RecyclerItemTouchHelperUploaded.RecyclerItemTouchHelperListener {

    public UploadedMusicFragment insance;
    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.rvUploadedSongs)
    RecyclerView rvUploadedSongs;
    private String sCookie;
    private ArrayList<Song> songList = new ArrayList<Song>();

    @BindView(R.id.txtNodata)
    TextView txtNodata;
    @BindView(R.id.view_tool)
    View view_tool;

    private  ProgressBar iv_progress;
    private int selectedMenuPosition;
    private boolean paused = false/*, playbackPaused = false*/;
    Runnable runnable;
    Handler handler;
    private long mLastClickTime = 0;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    public UploadedMusicAdapter mAdapter;
    private static String[] suffix = new String[]{"","k", "m", "b", "t"};
    private static int MAX_LENGTH = 4;

    private int tooltipColor;
    private int tooltipSize;
    private int tooltipPadding;

    private int tipSizeSmall;
    private int tipSizeRegular;
    private int tipRadius;
    private ViewGroup root;
    PopupWindow popUp;
    LinearLayout layout;
    ViewGroup.LayoutParams params;
    LinearLayout mainLayout;
    int page = 1;
    public Boolean isExpandable = true;

    TextView tv;
    int[] location = new int[2];

    public static UploadedMusicFragment newInstance() {

        Bundle args = new Bundle();

        UploadedMusicFragment fragment = new UploadedMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploaded_music, container, false);

        insance = this;
        root = (ViewGroup) view.findViewById(R.id.root);
        iv_progress = view.findViewById(R.id.iv_progress);
        unbinder = ButterKnife.bind(this, view);
        tv = new TextView(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        imgTopbarRight.setImageResource(R.drawable.ic_plus_white);
        txtTopbarTitle.setText(R.string.uploaded_music);
        Resources res = getResources();
        tooltipSize = res.getDimensionPixelOffset(R.dimen._200sdp);
        tooltipColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        tooltipPadding = res.getDimensionPixelOffset(R.dimen._18sdp);
        tipSizeSmall = res.getDimensionPixelSize(R.dimen._12sdp);
        tipSizeRegular = res.getDimensionPixelSize(R.dimen._16sdp);
        tipRadius = res.getDimensionPixelOffset(R.dimen._4sdp);
        if (AppLevelClass.getInstance().getTutorialPrefrences().getString(SharedPref.ADD_MUSIC_SCREEN).equalsIgnoreCase("1")) {

        } else {
            AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.ADD_MUSIC_SCREEN, "1");

            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.tooltip_layout);
            TextView tv_msg = dialog.findViewById(R.id.tooltip_text);
            tv_msg.setText(R.string.tap_add_music);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = 100;   //x position
            wmlp.y = 100;
            dialog.show();
        }
        LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
        rvUploadedSongs.setLayoutManager(lLayout);
         mAdapter = new UploadedMusicAdapter(getActivity(), songList);
        rvUploadedSongs.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperUploaded(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvUploadedSongs);
        rvUploadedSongs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (mAdapter.getItemCount() - 2)) {
                    if (isExpandable) {
                        page++;
                        callUploadedMusicApi();
                    }
                }
            }
        });
        //showStatusPopup(view_tool, point);
        callUploadedMusicApi();




    }

    private void showStatusPopup(final View view, Point p) {
        // Inflate the popup_layout.xml
        // RelativeLayout viewGroup = (RelativeLayout) view.findViewById(R.id.tooltip_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.tooltip_layout, null);
        // Creating the PopupWindow
        PopupWindow changeStatusPopUp = new PopupWindow(getActivity());
        changeStatusPopUp.setContentView(layout);
        changeStatusPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setFocusable(true);
        changeStatusPopUp.setBackgroundDrawable(null);
        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 20;
        int OFFSET_Y = 50;
        //Clear the default translucent background
        // Displaying the popup at the specified location, + offsets.
        changeStatusPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x, p.y + OFFSET_Y);
    }

    private void showTooltip(@NonNull View anchor, @StringRes int resId,
                             @Tooltip.Position int position, boolean autoAdjust,
                             @TooltipAnimation.Type int type, boolean hideWhenAnimating,
                             int width, int height) {
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.tooltip_textview, null);
        TextView textView = (TextView) contentView.getChildAt(0);
        textView.setText(resId);
        textView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        showTooltip(anchor, contentView, position, autoAdjust, type, hideWhenAnimating, tooltipColor);
    }

    private void showTooltip(@NonNull View anchor, @NonNull View content,
                             @Tooltip.Position int position, boolean autoAdjust,
                             @TooltipAnimation.Type int type, boolean hideWhenAnimating,
                             int tipColor) {

        new Tooltip.Builder(getActivity())
                .anchor(anchor, position)
                .animate(new TooltipAnimation(type, 500, hideWhenAnimating))
                .autoAdjust(autoAdjust)
                .content(content)
                .withTip(new Tooltip.Tip(tipSizeRegular, tipSizeRegular, tipColor))
                .into(root)
                .debug(true)
                .show();
    }

    private void callUploadedMusicApi() {
        isExpandable = false;
        iv_progress.setVisibility(View.VISIBLE);
//        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.PAGE, String.valueOf(page));
        NetworkCall.getInstance().callUploadedMusicApi(params, sCookie, new iResponseCallback<UploadedPojo>() {
            @Override
            public void sucess(UploadedPojo data) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<UploadedDatum> listData = data.getList().getData();
                    if (listData != null && !listData.isEmpty()){
                        isExpandable = true;
                    }else {
                        isExpandable = false;
                    }
                    if (page == 1) {
                        songList = new ArrayList<>();
                    }
                    for (int i = 0; i < listData.size(); i++) {
                        UploadedDatum trackData = listData.get(i);
                        Song song = new Song();
                        song.setUser_id(String.valueOf(trackData.getUserId()));
                        song.setSongID(trackData.getId() + "");
                        song.setSongTitle(trackData.getTitle());
                        song.setSongURL(trackData.getStreamUrl());
                        song.setSongNumber(trackData.getId() + "");
                        song.setSongImageUrl(trackData.getImageUrl());
                        song.setDownloades_song(trackData.getDownload_count());
                        song.setStremmed_song(trackData.getStream_count());
                        song.setFrom(AppConstants.FROM_UPLOAD_MUSIC);
                        song.setAlbumID(trackData.getAlbum_id());
                        song.setTrackType("1");
                        File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION+".nomedia");
                        File imagedirect = new File(getContext().getExternalCacheDir() + "/Images.nomedia");
                        if (myFile.exists()) {
                            song.setFrom(AppConstants.FROM_MY_MUSIC);
                            song.setSongPath(myFile.getPath());
                            song.setSongImagePath(imagedirect.getPath() + "/" + song.getSongID() + ".jpg");
                            song.setIsDownload(true);
                        } else {
                            song.setFrom(AppConstants.FROM_UPLOAD_MUSIC);
                            song.setIsDownload(false);
                            song.setSongPath(trackData.getStreamUrl());
                            song.setSongImagePath(song.getSongImageUrl());
                        }
                        if (trackData.getArtistName() != null)
                            song.setSongArtist(trackData.getArtistName());
                        else
                            song.setSongArtist("");
                        if (trackData.getLyrics() != null)
                            song.setLyrics(trackData.getLyrics());
                        else
                            song.setLyrics("");

                        songList.add(song);
                    }
                    setAdapter(songList);
                } else {
                    isExpandable = false;
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
                rvUploadedSongs.setVisibility(View.GONE);
                txtNodata.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Call<UploadedPojo> responseCall, Throwable T) {
                iv_progress.setVisibility(View.GONE);
                rvUploadedSongs.setVisibility(View.GONE);
                txtNodata.setVisibility(View.VISIBLE);
//                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setAdapter(ArrayList<Song> dataList) {
        if (dataList.size() > 0) {
            mAdapter.updateList(dataList);
            rvUploadedSongs.setVisibility(View.VISIBLE);
            txtNodata.setVisibility(View.GONE);
        } else {
            rvUploadedSongs.setVisibility(View.GONE);
            txtNodata.setVisibility(View.VISIBLE);

        }
    }


    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;
            case R.id.imgTopbarRight:
                homeActivity.openAddMusicFragment(FragmentState.REPLACE);
                break;
        }
    }

    private boolean checkMultipleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
//        DebugLog.e("onResume");



    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof UploadedMusicAdapter.MyViewHolder) {
            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
        }
    }

    public class UploadedMusicAdapter extends RecyclerView.Adapter<UploadedMusicAdapter.MyViewHolder> {
        private Context context;
        public List<Song> notificationData;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_song_image)
            public ImageView imgVPSongs;
            @BindView(R.id.tv_song_name)
            public TextView txtSongName;
            @BindView(R.id.txtSongContent)
            public TextView txtSongContent;
            @BindView(R.id.imgDot)
            public ImageView imgDot;
            @BindView(R.id.llRowRoot)
            public LinearLayout llRowRoot;
            @BindView(R.id.tv_downloaded)
            public TextView tv_downloaded;
            @BindView(R.id.tv_streaming)
            public TextView tv_streaming;

            @BindView(R.id.view_background)
            public RelativeLayout view_background;

            @BindView(R.id.ll_main)
            public LinearLayout ll_main;


            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }


        public UploadedMusicAdapter(Context context, List<Song> dataList) {
            this.context = context;
            this.notificationData = dataList;
        }

        public void updateList(ArrayList<Song> dataList){
            this.notificationData = dataList;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_uploaded_songs, parent, false);

            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Song data = notificationData.get(position);

            holder.txtSongName.setText(data.getSongTitle());
            if (data.getSongArtist() != null)
                holder.txtSongContent.setText(data.getSongArtist());
            else
                holder.txtSongContent.setVisibility(View.GONE);
               String playlistImage = data.getSongImageUrl();

                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }

                if (playlistImage != null && !playlistImage.isEmpty()) {
                    if (HomeActivity.isDataSaverEnabled()){

                    }else {
                        if (playlistImage != null && !playlistImage.isEmpty()) {
                            Glide.with(getActivity()).
                                    load(playlistImage)
                                    .placeholder(R.drawable.ic_top_placeholder)
                                    .error(R.drawable.ic_top_placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                    .into(holder.imgVPSongs);
                        }
                    }
                }

                if (data.getDownloades_song() != null) {
                        holder.tv_downloaded.setText("" + format(data.getDownloades_song()));
                }

                if (data.getStremmed_song() != null) {
                    holder.tv_streaming.setText("" + format(data.getStremmed_song()));
                }


            holder.imgDot.setVisibility(View.VISIBLE);
            holder.ll_main.setId(position);
            holder.imgDot.setId(position);



            holder.imgDot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int pos = v.getId();
                    removeItem(pos);
                }
            });

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int id = v.getId();
                    homeActivity.playSongWithPostion(id,songList,AppConstants.FROM_UPLOAD_MUSIC);
                }
            });
        }


        private String format(BigInteger number) {

            // apply compareTo() method
            BigInteger b2 = new BigInteger("999");
            int comparevalue = number.compareTo(b2);
            if (comparevalue == 1) {
                String r = new DecimalFormat("##0E0").format(number);
                r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
                while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
                    r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
                }
                return r;
            }
            else {
                return number.toString();
            }
        }

        @Override
        public int getItemCount() {
            return notificationData.size();
        }

        public void removeItem(int position) {
            callDeleteSongOne(position, notificationData.get(position).getSongID());
        }

    }

    private void callDeleteSongOne(final int position, String ID) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.TRACK_ID, ID);

        NetworkCall.getInstance().callUploadedSongDeleteOneApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    callUploadedMusicApi();
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
