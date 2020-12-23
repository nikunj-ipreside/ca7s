package com.music.ca7s.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.viewholder.NavigationDrawerHolder;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.enumeration.NavigationItem;
import com.music.ca7s.listener.iNavigationItemClick;
import com.music.ca7s.model.NavigationDrawerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationDrawerFragment extends BaseFragment {

    public static iNavigationItemClick callback;
    private String user_id = "";

    public void setCallback(iNavigationItemClick callback) {
        this.callback = callback;
    }

    public static NavigationDrawerFragment newInstance(iNavigationItemClick callback) {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        fragment.setCallback(callback);
        return fragment;
    }

    public static NavigationDrawerFragment getnewInstance() {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        return view;
    }
    public static ImageView imgMenuProfile;
    public static TextView txtMenuName;
    public static TextView txtMenuUserName;
    public static TextView txtMenuCity;
    public static RecyclerView rvNavDrawer;
    public static  LinearLayout ll_profile;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgMenuProfile = view.findViewById(R.id.imgMenuProfile);
        txtMenuUserName = view.findViewById(R.id.txtMenuUserName);
        txtMenuName = view.findViewById(R.id.txtMenuName);
        txtMenuCity = view.findViewById(R.id.txtMenuCity);
        rvNavDrawer = view.findViewById(R.id.rvNavDrawer);
        ll_profile = view.findViewById(R.id.ll_profile);

        txtMenuName.setText(AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.FULL_NAME));
        txtMenuUserName.setText(AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.FULL_NAME));
        txtMenuUserName.setPaintFlags(txtMenuUserName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        txtMenuCity.setText(AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_CITY));
        uploadImageProfile(getContext());

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()){
                    homeActivity.openProfileFragment(FragmentState.REPLACE);
                }else {
                    homeActivity.closeDrawer();
                }

            }
        });
    }


    public void uploadImageProfile(Context context) {
        if(imgMenuProfile != null && txtMenuName != null &&  txtMenuUserName != null && context != null) {
            String name =  AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.FULL_NAME);
            String playlistImage = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.PROFILE_PICTURE);
            txtMenuName.setText(""+name);
            txtMenuUserName.setText(""+name);
            txtMenuUserName.setPaintFlags(txtMenuUserName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            if (playlistImage != null && !playlistImage.isEmpty()) {
                if (playlistImage.contains("/index.php")) {
                    playlistImage = playlistImage.replace("/index.php", "");
                }

                if (playlistImage.contains("/public/public")) {
                    playlistImage = playlistImage.replace("/public/public", "/public");
                }
                Glide.with(context)
                        .load(playlistImage)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .transform(new CircleCrop())
                        .into(imgMenuProfile);
            }
        }
        setAdapter(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setAdapter(Context context) {
        final List<NavigationDrawerModel> datalist = getNavigationData(context);
        GenericAdapter<NavigationDrawerModel, NavigationDrawerHolder> adapter = new GenericAdapter<NavigationDrawerModel,
                NavigationDrawerHolder>(R.layout.row_nav_drawer, NavigationDrawerHolder.class, datalist) {
            @Override
            public Filter getFilter() {
                return null;
            }

            @Override
            public void setViewHolderData(NavigationDrawerHolder holder, final NavigationDrawerModel data, int position) {

                if (data.getItemText() != null) {
                    holder.txtItemName.setText(data.getItemText());
                    holder.imgItemIcon.setImageResource(data.getItemImage());
                }

                if (position == datalist.size()){
                    String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
                    if(user_id.isEmpty()){
                        holder.imgItemIcon.setImageResource(R.drawable.export_download_gray);
                    }else {
                        holder.imgItemIcon.setImageResource(R.drawable.export_gray);
                    }
                }
                holder.llRowNavItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        callback.onNavigationItemSelect(data,view);

                    }
                });
            }

            @Override
            public void loadMore() {

            }
        };
        LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
        rvNavDrawer.setLayoutManager(lLayout);
        rvNavDrawer.setAdapter(adapter);
    }


    List<NavigationDrawerModel> getNavigationData(Context context) {
        List<NavigationDrawerModel> list = new ArrayList<>();
        List<String> listNavItem;
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);

        if(!user_id.isEmpty()) {
            listNavItem = Arrays.asList(context.getResources().getStringArray(R.array.nav_item));
        }else{
            listNavItem = Arrays.asList(context.getResources().getStringArray(R.array.nav_item2));

        }
            TypedArray drawableArray = AppLevelClass.getInstance().getResources().obtainTypedArray(R.array.nav_image);

        int i = -1;
        for (NavigationItem nav :
                NavigationItem.values()) {
            i += 1;
            NavigationDrawerModel model = new NavigationDrawerModel();
            model.setNavigationItem(nav);
            model.setItemImage(drawableArray.getResourceId(i, -1));
            model.setItemText(listNavItem.get(i));
            list.add(model);
        }
        return list;
    }

}
