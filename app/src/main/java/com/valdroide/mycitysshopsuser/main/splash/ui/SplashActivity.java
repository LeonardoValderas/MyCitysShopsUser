package com.valdroide.mycitysshopsuser.main.splash.ui;

import android.content.Context;
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
import com.valdroide.mycitysshopsuser.utils.Utils;

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
        if (validateLogFile(this)) {
            Utils.writelogFile(this, "Se inicia ButterKnife(Splash)");
            ButterKnife.bind(this);
            Utils.writelogFile(this, "Se inicia Injection(Splash)");
            setupInjection();
            Utils.writelogFile(this, "Se inicia presenter Oncreate(Splash)");
            presenter.onCreate();
            Utils.writelogFile(this, "Validate token Oncreate(Splash)");

            isPlace = isPlace();
            if (!isPlace) {
                Utils.writelogFile(this, "isPlace false y validateDatePlace(Splash)");
                presenter.validateDatePlace(this, getNotificationExtra());
            } else {
                Utils.writelogFile(this, "isPlace true y validateDateShop(Splash)");
                presenter.getToken(this);
            }
        } else {
            Utils.writelogFile(this, getString(R.string.error_file_log));
            setError(getString(R.string.error_file_log));
        }
    }

    private boolean validateLogFile(Context context) {
        return Utils.validateLogFile(context);
    }

    public boolean isPlace() {
        Utils.writelogFile(this, "getBooleanExtra isPlace(Splash)");
        return getIntent().getBooleanExtra("isPlace", false);
    }

    public Intent getNotificationExtra() {
        Intent intent = null;
        try {
            Utils.writelogFile(this, "getNotificationExtra(Splash)");
            if (getIntent().getBooleanExtra("notification", false)) {
                intent = new Intent(this, NavigationActivity.class);
                intent.putExtra("notification", true);
                intent.putExtra("messasge", getIntent().getStringExtra("messasge"));
                intent.putExtra("id_shop", getIntent().getIntExtra("id_shop", 0));
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "getNotificationExtra error: " + e.getMessage() + "(Splash)");
            setError(e.getMessage());
        }
        return intent;
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
        Utils.writelogFile(this, "goToPlace(Splash)");
        try {
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this, PlaceActivity.class));
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Splash)");
        }
    }

    @Override
    public void goToNav() {
        Utils.writelogFile(this, "goToNav(Splash)");
        try {
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this, NavigationActivity.class));
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Splash)");
        }
    }

    @Override
    public void setError(String msg) {
        Utils.writelogFile(this, "setError " + msg + "(Splash)");
        try {
            progressBar.setVisibility(View.INVISIBLE);
            presenter.sendEmail(this, "Error Splash, Email Automatico.");
            textViewDownload.setText(msg + "\nSe envió una email automaticamente a soporte. ");
        } catch (Exception e) {
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Splash)");
        }
    }

    @Override
    public void tokenSuccess() {
        presenter.validateDateShop(this, getNotificationExtra());
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Splash)");
        presenter.onDestroy();
        super.onDestroy();
    }
}
