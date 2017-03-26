package com.valdroide.mycitysshopsuser.main.splash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivity;
import com.valdroide.mycitysshopsuser.main.place.ui.PlaceActivity;
import com.valdroide.mycitysshopsuser.main.splash.SplashActivityPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements SplashActivityView {

    @Bind(R.id.imageViewLogo)
    ImageView imageViewLogo;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.activity_splash)
    LinearLayout conteiner;
    @Bind(R.id.textViewDownload)
    TextView textViewDownload;
    @Inject
    SplashActivityPresenter presenter;
    private boolean isPlace = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setupInjection();
        presenter.onCreate();
        isPlace = isPlace();
        if (!isPlace)
            presenter.validateDatePlace(this);
        else
            presenter.validateDateShop(this);
    }

    public boolean isPlace() {
        return getIntent().getBooleanExtra("isPlace", false);
    }

    private void setupInjection() {
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getSplashActivityComponent(this, this).inject(this);
    }

//    @Override
//    public void goToLog() {
//        progressBar.setVisibility(View.INVISIBLE);
//        startActivity(new Intent(this, LoginActivity.class));
//    }

    @Override
    public void goToPlace() {
        progressBar.setVisibility(View.INVISIBLE);
        startActivity(new Intent(this, PlaceActivity.class));
    }

    @Override
    public void goToNav() {
        progressBar.setVisibility(View.INVISIBLE);
        startActivity(new Intent(this, NavigationActivity.class));
    }

    @Override
    public void setError(String msg) {
        progressBar.setVisibility(View.INVISIBLE);
        textViewDownload.setText(msg);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
