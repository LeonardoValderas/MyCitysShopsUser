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
import me.leolin.shortcutbadger.ShortcutBadger;

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
            setupInjection();
            Utils.writelogFile(this, "Se inicia presenter Oncreate(Splash)");
            presenter.onCreate();
            resetBadge();
            Utils.writelogFile(this, "isPlace Oncreate(Splash)");
            isPlace = isPlace();
            if (!isPlace) {
                Utils.writelogFile(this, "isPlace false y validateDatePlace(Splash)");
                Intent intent = getIntentExtra();
                presenter.validateDatePlace(this, intent);
            } else {
                Utils.writelogFile(this, "isPlace true y validateDateShop(Splash)");
                presenter.getToken(this);
            }
        } else {
            Utils.writelogFile(this, getString(R.string.error_file_log));
            setError(getString(R.string.error_file_log));
        }
    }

    public void resetBadge() {
        Utils.resetCounterBadge(this);
        ShortcutBadger.removeCount(this);
    }

    private boolean validateLogFile(Context context) {
        return Utils.validateLogFile(context);
    }

    public boolean isPlace() {
        Utils.writelogFile(this, "getBooleanExtra isPlace(Splash)");
        return getIntent().getBooleanExtra("isPlace", false);
    }

    public Intent getIntentExtra() {
        Intent intent = null;
        try {
            Utils.writelogFile(this, "getIntentExtra(Splash)");
            if (getIntent().getBooleanExtra("notification", false)) {
                Utils.writelogFile(this, "getBooleanExtra notification is true(Splash)");
                intent = fillIntent(true);
            } else if (getIntent().getBooleanExtra("draw", false)) {
                Utils.writelogFile(this, "getBooleanExtra draw is true is true(Splash)");
                intent = fillIntent(false);
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "getNotificationOrDrawExtra error: " + e.getMessage() + "(Splash)");
            setError(e.getMessage());
        }
        return intent;
    }

    private Intent fillIntent(boolean isNotificacion) {
        Intent intent = new Intent(this, NavigationActivity.class);
        if (isNotificacion) {
            intent.putExtra("notification", true);
            intent.putExtra("title", getIntent().getStringExtra("title"));
            intent.putExtra("messasge", getIntent().getStringExtra("messasge"));
            intent.putExtra("url_shop", getIntent().getStringExtra("url_shop"));
        } else
            intent.putExtra("draw", true);

        return intent;
    }

    private void setupInjection() {
        Utils.writelogFile(this, "Se inicia Injection(Splash)");
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getSplashActivityComponent(this, this).inject(this);
    }

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
            startActivity(new Intent(this, NavigationActivity.class));
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Splash)");
        }
    }

    @Override
    public void setError(String msg) {
        Utils.writelogFile(this, "setError " + msg + "(Splash)");
        hideProgressBar();
        try {
         if (msg.equalsIgnoreCase(getString(R.string.error_internet))) {
                textViewDownload.setText(msg);
            } else {
                presenter.sendEmail(this, "Error Splash, Email Automatico.");
                textViewDownload.setText(msg + "\n" + getString(R.string.sent_email_text));
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Splash)");
        }
    }

    @Override
    public void tokenSuccess() {
        presenter.validateDateShop(this, getIntentExtra());
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Splash)");
        presenter.onDestroy();
        super.onDestroy();
    }
}
