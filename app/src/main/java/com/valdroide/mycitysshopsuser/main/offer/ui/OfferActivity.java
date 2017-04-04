package com.valdroide.mycitysshopsuser.main.offer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.main.offer.OfferActivityPresenter;
import com.valdroide.mycitysshopsuser.main.offer.ui.adapters.OfferActivityAdapter;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferActivity extends AppCompatActivity implements OfferActivityView {

    //    @Bind(R.id.recyclerView)
//    RecyclerView recyclerView;
    @Inject
    OfferActivityPresenter presenter;
    @Inject
    OfferActivityAdapter adapter;
    @Inject
    List<Offer> offerList;
    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    @Bind(R.id.container)
    CoordinatorLayout container;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ProgressDialog pDialog;
    private MyCitysShopsUserApp app;
    private int id_shop;
    private String url = "";
//    @Bind(R.id.tvOverview)
//    TextView tvOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);


        Utils.writelogFile(this, "Se inicia ButterKnife(Splash)");


        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Offer)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Offer)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(Offer)");
        initToolBar();
        Utils.writelogFile(this, "Se initDialog(Offer)");
        initDialog();
        try {
            Utils.writelogFile(this, "getIntExtra id_shop y getStringExtra url(Offer)");
            id_shop = getIntent().getIntExtra("id_shop", 0);
            url = getIntent().getStringExtra("url");
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Offer)");
        }
        setImageViewShop(url);
        Utils.writelogFile(this, "dialog show(Offer)");
        pDialog.show();
        Utils.writelogFile(this, " presenter.getListOffer(Offer)");
        presenter.getListOffer(id_shop);
    }

    public void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PROMOS");
    }

    public void setImageViewShop(String url) {
        Utils.writelogFile(this, "Se picasso (Offer)");
        Utils.setPicasso(this, url, R.mipmap.ic_launcher, imageViewShop);
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
    }

    public void initRecyclerViewAdapter() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
    }

    private void setupInjection() {
        app = (MyCitysShopsUserApp) getApplication();
        app.getOfferActivityComponent(this, this).inject(this);
    }

    @Override
    public void setListOffer(List<Offer> offers) {
        Utils.writelogFile(this, "setListOffer " + offers.size() + " (Offer)");
        //      adapter.setOffer(offers);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void setError(String mgs) {
        Utils.writelogFile(this, "setError " + mgs + " (Offer)");
        Utils.showSnackBar(container, mgs);
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Offer)");
        presenter.onDestroy();
        pDialog.dismiss();
        super.onDestroy();
    }
}
