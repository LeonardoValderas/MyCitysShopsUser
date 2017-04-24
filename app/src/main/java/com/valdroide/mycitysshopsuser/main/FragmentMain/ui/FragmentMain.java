package com.valdroide.mycitysshopsuser.main.FragmentMain.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainPresenter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogContact;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogMap;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogOffer;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.FragmentMainAdapter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivity;
import com.valdroide.mycitysshopsuser.utils.Utils;

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
    private static SubCategory subCategoryExtra;
    private ProgressDialog pDialog;
    private int position = 0;
    private Shop shopDialog;
    private static boolean isMyShop = false;
    //private AdRequest adRequest;
    private DateUserCity dateUserCity;

    public FragmentMain() {
    }

    public static FragmentMain newInstance(SubCategory subCategory, boolean isMyShops) {
        FragmentMain fragmentAction = new FragmentMain();
        try {
            Utils.writelogFile(fragmentAction.getActivity(), "Se instacia FragmentMain(FragmentMain)");
            if (!isMyShops) {
                Utils.writelogFile(fragmentAction.getActivity(), "isMyShops = false (FragmentMain)");
                subCategoryExtra = subCategory;
            }
            isMyShop = isMyShops;
        } catch (Exception e) {
            fragmentAction.setError(e.getMessage());
            Utils.writelogFile(fragmentAction.getActivity(), "catch error " + e.getMessage() + "(FragmentMain)");
        }
        return fragmentAction;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Utils.writelogFile(getActivity(), "Se inicia Injection(FragmentMain)");
        setupInjection();
        Utils.writelogFile(getActivity(), "Se inicia presenter Oncreate(FragmentMain)");
        presenter.onCreate();
        Utils.writelogFile(getActivity(), "Se inicia dialog Oncreate(FragmentMain)");
        initDialog();
        if (isMyShop) {
            Utils.writelogFile(getActivity(), "isMyShop true y getMyFavoriteShops(FragmentMain)");
            presenter.getMyFavoriteShops(getActivity());
        } else {
            Utils.writelogFile(getActivity(), "isMyShop false(FragmentMain)");
            if (subCategoryExtra != null) {
                Utils.writelogFile(getActivity(), "dialog show y getListShops(FragmentMain)");
                pDialog.show();
                presenter.getListShops(getActivity(), subCategoryExtra);
            }
        }

        initRecyclerViewAdapter();
        //  initSwipeRefreshLayout();
        // BannerAd();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Utils.writelogFile(getActivity(), "Se inicia ButterKnife(FragmentMain)");
        ButterKnife.bind(this, view);
        initSwipeRefreshLayout();
        Utils.writelogFile(getActivity(), "return view(FragmentMain)");
        return view;
    }

    public void initDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.process));
        pDialog.setCancelable(false);
    }

    public void initRecyclerViewAdapter() {
        Utils.writelogFile(getActivity(), "initRecyclerViewAdapter(FragmentMain)");
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "initRecyclerViewAdapter catch error " + e.getMessage() + "(FragmentMain)");
        }
    }

    private void setupInjection() {
        app = (MyCitysShopsUserApp) getActivity().getApplication();
        app.getFragmentMainComponent(this, this, this).inject(this);
    }

    public void verifySwipeRefresh() {
        Utils.writelogFile(getActivity(), "verifySwipeRefresh(FragmentMain)");
        try {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "verifySwipeRefresh catch error " + e.getMessage() + "(FragmentMain)");
        }
    }

    private void initSwipeRefreshLayout() {
        Utils.writelogFile(getActivity(), "initSwipeRefreshLayout(FragmentMain)");
        try {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (isMyShop)
                        verifySwipeRefresh();
                    else {
                        presenter.getDateUserCity(getActivity());
                        presenter.refreshLayout(getActivity(), dateUserCity);
                    }
                }
            });
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "initSwipeRefreshLayout catch error " + e.getMessage() + "(FragmentMain)");
        }
    }

//    public void BannerAd() {
//        adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("B52960D9E6A2A5833E82FEA8ACD4B80C")
//                .build();
//        mAdView.loadAd(adRequest);
//    }

    @Override
    public void setListShops(List<Shop> shops) {
        Utils.writelogFile(getActivity(), "setListShops " + shops.size() + "(FragmentMain)");
        if (pDialog.isShowing())
            pDialog.dismiss();
        adapter.setShop(shops);
        verifySwipeRefresh();
    }

    @Override
    public void setError(String mgs) {
        Utils.writelogFile(getActivity(), "setError " + mgs + "(FragmentMain)");
        if (pDialog.isShowing())
            pDialog.dismiss();
        Utils.showSnackBar(conteiner, mgs);
        verifySwipeRefresh();
    }

    @Override
    public void withoutChange() {
        Utils.writelogFile(getActivity(), "withoutChange(FragmentMain)");
        verifySwipeRefresh();
    }

    @Override
    public void callShops() {
        Utils.writelogFile(getActivity(), "callShops(FragmentMain)");
        if (subCategoryExtra != null) {
            Utils.writelogFile(getActivity(), "subCategoryExtra != null y getListShops(FragmentMain)");
            presenter.getListShops(getActivity(), subCategoryExtra);
        } else {
            Utils.writelogFile(getActivity(), "subCategoryExtra == null y Intent Navigation(FragmentMain)");
            startActivity(new Intent(getActivity(), NavigationActivity.class));
        }
    }

    @Override
    public void setDateUserCity(DateUserCity dateUserCity) {
        Utils.writelogFile(getActivity(), "setDateUserCity(FragmentMain)");
        this.dateUserCity = dateUserCity;
    }

    @Override
    public void isUpdate() {
        Utils.writelogFile(getActivity(), "isUpdate(FragmentMain)");
        adapter.setUpdateShop(position);
    }

    @Override
    public void followUnFollowSuccess(Shop shop) {
        Utils.writelogFile(getActivity(), "followSuccessUnFollow(FragmentMain)");
        adapter.setUpdateShop(position, shop);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClickFollowOrUnFollow(int position, Shop shop, boolean isFollow) {
        Utils.writelogFile(getActivity(), "onClickFollow(FragmentMain)");
        pDialog.show();
        this.position = position;
        presenter.onClickFollowOrUnFollow(getActivity(), shop, isFollow);
    }

    @Override
    public void onClickOffer(int position, Shop shop) {
        Utils.writelogFile(getActivity(), "onClickUnFollow y getListOffer y setUpdateOffer(FragmentMain)");
        pDialog.show();
        shopDialog = shop;
        this.position = position;
        presenter.getListOffer(getActivity(), shop.getID_SHOP_KEY());
        presenter.setUpdateOffer(getActivity(), position, shop);
    }

    @Override
    public void setListOffer(List<Offer> offers) {
        Utils.writelogFile(getActivity(), "setListOffer y DialogOffer(FragmentMain)");
        if (pDialog.isShowing())
            pDialog.dismiss();
        new DialogOffer(getActivity(), offers, shopDialog);
    }

    @Override
    public void onClickMap(Shop shop, View v) {
        Utils.writelogFile(getActivity(), "onClickMap y DialogMap(FragmentMain)");
        new DialogMap(getActivity(), this, shop, v);
    }

    @Override
    public void onClickContact(Shop shop) {
        Utils.writelogFile(getActivity(), "onClickContact y DialogNotification(FragmentMain)");
        new DialogContact(getActivity(), shop);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mAdView != null) {
//            mAdView.resume();
//        }
//    }


    @Override
    public void onDestroyView() {
        Utils.writelogFile(getActivity(), "onDestroyView(FragmentMain)");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        Utils.writelogFile(getActivity(), "onDestroy(FragmentMain)");
        if (pDialog.isShowing())
            pDialog.dismiss();
        presenter.onDestroy();
        super.onDestroy();
    }
}
