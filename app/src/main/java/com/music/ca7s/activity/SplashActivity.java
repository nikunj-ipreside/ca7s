package com.music.ca7s.activity;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.music.ca7s.R;

public class SplashActivity extends AppNavigationActivity {

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            openHomeActivity();

        }
    };
    Handler handler;
    private ImageView iv_splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        iv_splash_logo = findViewById(R.id.iv_splash_logo);

        Glide.with(SplashActivity.this)
                .load(R.drawable.splash_logo_new)
                .error(R.drawable.splash_logo_new)
                .placeholder(R.drawable.splash_logo_new)
                .into(iv_splash_logo);

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler();
        handler.postDelayed(runnable, 2500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
