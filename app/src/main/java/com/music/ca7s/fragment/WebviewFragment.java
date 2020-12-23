package com.music.ca7s.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WebviewFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.loading_spinner)
    ProgressBar loadingSpinner;
    @BindView(R.id.relativceProgress)
    RelativeLayout relativceProgress;

    boolean loadingFinished = true;
    boolean redirect = false;
    private ProgressDialog progressDialog;
    private String webURL = "https://www.ca7s.com/ca7s/terms_condition";

    String sTitle;

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }
    public void setswebURL(String webURL) {
        this.webURL = webURL;
    }

    public static WebviewFragment newInstance(String sTitle, String webURL) {
//        Bundle args = new Bundle();
        WebviewFragment fragment = new WebviewFragment();
        fragment.setsTitle(sTitle);
        fragment.setswebURL(webURL);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        imgTopbarRight.setVisibility(View.GONE);
        txtTopbarTitle.setText(sTitle);

        callWebviewMethod(webURL);

    }


    private void callWebviewMethod(String url) {

        webView.setWebViewClient(new myWebClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setSupportZoom(true);
        webView.setClickable(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webView.loadUrl(url);
    }


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
            }
            super.onPageStarted(view, url, favicon);
            loadingFinished = false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub

            if (!redirect) {
                loadingFinished = true;
            }
            if (loadingFinished && !redirect && relativceProgress!=null) {
                relativceProgress.setVisibility(View.GONE);
            } else {
                redirect = false;
            }
            super.onPageFinished(view, url);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_close_player)
    public void onViewClicked() {
        homeActivity.openDrawer();
    }
}
