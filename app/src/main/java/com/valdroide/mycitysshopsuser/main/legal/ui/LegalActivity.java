package com.valdroide.mycitysshopsuser.main.legal.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.main.legal.LegalActivityPresenter;
import com.valdroide.mycitysshopsuser.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LegalActivity extends AppCompatActivity implements LegalActivityView {

    @Bind(R.id.lineaConteiner)
    CoordinatorLayout lineaConteiner;
    @Bind(R.id.textViewMCS)
    TextView textViewMCS;
    @Bind(R.id.textViewLegal)
    TextView textViewLegal;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    LegalActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        ButterKnife.bind(this);
        setupInjection();
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(support)");
        initToolBar();
        presenter.getDataLegal(this);
    }

    public void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.legal);
        Utils.applyFontForToolbarTitle(this, toolbar);
    }

    private void setupInjection() {
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getLegalActivityComponent(this, this).inject(this);
    }

    @Override
    public void setLegal(String about, String legal) {
        textViewMCS.setText(about);
        textViewMCS.setTypeface(Utils.setFontExoTextView(this));
        textViewLegal.setText(legal);
        textViewLegal.setTypeface(Utils.setFontExoTextView(this));
    }

    @Override
    public void setError(String error) {
        Utils.showSnackBar(lineaConteiner, error);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
