package com.music.ca7s.dialog;


import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.ca7s.R;
import com.music.ca7s.listener.iDialogClickCallback;
import com.music.ca7s.model.DialogModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommonFragmentDialog extends BaseFragmentDialog {


    Unbinder unbinder;
    @BindView(R.id.txtMessage)
    TextView txtMessage;
    @BindView(R.id.linearLine)
    LinearLayout linearLine;
    @BindView(R.id.btnOk)
    Button btnOk;

    DialogModel dialogModel;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    iDialogClickCallback callback;

    public void setCallback(iDialogClickCallback callback) {
        this.callback = callback;
    }

    public void setDialogModel(DialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }

    public static CommonFragmentDialog newInstance(iDialogClickCallback callback, DialogModel dialogModel) {
        CommonFragmentDialog fragment = new CommonFragmentDialog();
        fragment.callback = callback;
        fragment.setDialogModel(dialogModel);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_fragment_common, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtMessage.setText(dialogModel.getdDetails());

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

    @OnClick(R.id.btnOk)
    public void onClick() {
        callback.dialogButtonClick();
        dismiss();
    }
}