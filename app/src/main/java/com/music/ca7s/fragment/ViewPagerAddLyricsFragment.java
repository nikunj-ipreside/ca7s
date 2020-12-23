package com.music.ca7s.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPagerAddLyricsFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.et_lyrics)
    public EditText edtLyrics;
    @BindView(R.id.btnSave)
    Button btnSave;

    public static ViewPagerAddLyricsFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerAddLyricsFragment fragment = new ViewPagerAddLyricsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_v_addlyrics, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        edtLyrics.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                //  Toast.makeText(getActivity(),String.valueOf(wordsLength) + "/" + "5000",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        trim.replaceAll("\\s+", "");
        return trim.length();// separate string around spaces
    }

    private InputFilter filter;

    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[]{filter});
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSave)
    public void onViewClicked() {
        String trim = edtLyrics.getText().toString();
        trim = trim.replaceAll("\\s+", "");
        Log.i("TAG", "TRIM :-> " + trim.length());
        int wordsLength = trim.length();// words.length;
        // count == 0 means a new word is going to start
        if (edtLyrics.getText().length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_lyrics));

        } else if (trim.length() > 5000){
                // setCharLimit(et_lyrics, 2);
                if (Util.changeLanguage(homeActivity).equalsIgnoreCase("English")) {
                    homeActivity.showSnackBar(getView(), "Lyrics shall not exceed 5000 characters");
                } else {
                    homeActivity.showSnackBar(getView(), "As letras não devem exceder 5000 caracteres");
                }
            } else {
                AddMusicFragment.edtLyrics = edtLyrics.getText().toString();
                homeActivity.showSnackBar(getView(), getString(R.string.information_save));
            }

    }
}
