package com.valdroide.mycitysshopsuser.main.FragmentMain.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainPresenter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogContact;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogMap;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogOffer;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.FragmentMainAdapter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivity;
import com.valdroide.mycitysshopsuser.main.offer.ui.OfferActivity;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentMain extends Fragment implements FragmentMainView, OnItemClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.conteiner)
    FrameLayout conteiner;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    //    @Bind(R.id.adView)
//    AdView mAdView;
    @Inject
    FragmentMainAdapter adapter;
    @Inject
    FragmentMainPresenter presenter;

    MyCitysShopsUserApp app;
    private List<Shop> shopsList;
    private static String title = "";
    private static SubCategory subCategoryExtra;
    private ProgressDialog pDialog;
    private int position = 0;
    private Shop shopDialog;
    //private AdRequest adRequest;

    public FragmentMain() {
    }

    public static FragmentMain newInstance(SubCategory subCategory) {
        FragmentMain fragmentAction = new FragmentMain();
        subCategoryExtra = subCategory;
        title = subCategoryExtra.getSUBCATEGORY();
        return fragmentAction;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        setupInjection();
        presenter.onCreate();
        initDialog();
        if (subCategoryExtra != null) {
            pDialog.show();
            presenter.getListShops(subCategoryExtra);
        }
        initRecyclerViewAdapter();
        initSwipeRefreshLayout();
        // BannerAd();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void initDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
    }

    public void initRecyclerViewAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void setupInjection() {
        app = (MyCitysShopsUserApp) getActivity().getApplication();
        app.getFragmentMainComponent(this, this, this).inject(this);
    }

    public void verifySwipeRefresh() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getDateTable();
                // presenter.refreshLayout(getActivity(), date, cat, sub, clo, cont);
            }
        });
    }

//    public void BannerAd() {
//        adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("B52960D9E6A2A5833E82FEA8ACD4B80C")
//                .build();
//        mAdView.loadAd(adRequest);
//    }

//    @Override
//    public void onPause() {
//        if (mAdView != null) {
//            mAdView.pause();
//        }
//        super.onPause();
//    }

    @Override
    public void setListShops(List<Shop> shops) {
        this.shopsList = shops;
        if (pDialog.isShowing())
            pDialog.dismiss();
        adapter.setShop(shops);
        verifySwipeRefresh();
    }


    @Override
    public void setError(String mgs) {
        if (pDialog.isShowing())
            pDialog.dismiss();
        Utils.showSnackBar(conteiner, mgs);
        verifySwipeRefresh();
    }

    @Override
    public void withoutChange() {
        verifySwipeRefresh();
    }

    @Override
    public void callShops() {
        if (subCategoryExtra != null)
            presenter.getListShops(subCategoryExtra);
        else
            startActivity(new Intent(getActivity(), NavigationActivity.class));
    }

    @Override
    public void followSuccess(Shop shop) {
        adapter.setUpdateShop(position, shop);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void unFollowSuccess(Shop shop) {
        adapter.setUpdateShop(position, shop);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClickFollow(int position, Shop shop) {
        pDialog.show();
        this.position = position;
        presenter.onClickFollow(getActivity(), shop);
    }

    @Override
    public void onClickUnFollow(int position, Shop shop) {
        pDialog.show();
        this.position = position;
        presenter.onClickUnFollow(getActivity(), shop);
    }

    @Override
    public void onClickOffer(Shop shop) {
        pDialog.show();
        shopDialog = shop;
        presenter.getListOffer(shop.getID_SHOP_KEY());


//        Intent intent = new Intent(getActivity(), OfferActivity.class);
//        intent.putExtra("id_shop", shop.getID_SHOP_KEY());
//        intent.putExtra("url", shop.getURL_LOGO());
//        startActivity(intent);
    }

    @Override
    public void setListOffer(List<Offer> offers) {
        if (pDialog.isShowing())
            pDialog.dismiss();
        new DialogOffer(getActivity(), offers, shopDialog);
    }

    @Override
    public void onClickMap(Shop shop) {
//        FragmentManager fm = getFragmentManager();
//        DialogMap m = new DialogMap();
//        m.show(fm,"s" );


        new DialogMap(getActivity(), this, shop);
    }

    @Override
    public void onClickContact(Shop shop) {
        new DialogContact(getActivity(), shop);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mAdView != null) {
//            mAdView.resume();
//        }
//    }

//    @Override
//    public void setDateTable(List<DateTable> dateTable) {
//        for (int i = 0; i < dateTable.size(); i++) {
//            switch (dateTable.get(i).getTABLENAME()) {
//                case Utils.TABLE:
//                    date = dateTable.get(i).getDATE();
//                    break;
//                case Utils.CATEGORY:
//                    cat = dateTable.get(i).getDATE();
//                    break;
//                case Utils.SUBCATEGORY:
//                    sub = dateTable.get(i).getDATE();
//                    break;
//                case Utils.CLOTHES:
//                    clo = dateTable.get(i).getDATE();
//                    break;
//                case Utils.CONTACT:
//                    cont = dateTable.get(i).getDATE();
//                    break;
//            }
//        }
//    }

//    @Override
//    public void onClick(View view, int position) {
//        new DialogOffer(getActivity(), shopsList.get(position), title);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        if (pDialog.isShowing())
            pDialog.dismiss();
        presenter.onDestroy();
        super.onDestroy();
    }
}
