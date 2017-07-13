package com.valdroide.mycitysshopsuser.main.FragmentMain.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainPresenter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogContact;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogMap;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.DialogOffer;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.OnClickContact;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.FragmentMainAdapter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivity;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentMain extends Fragment implements FragmentMainView, OnItemClickListener, OnClickContact {

    private static final int PERMISSION_CALL = 110;
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
    // private AdRequest adRequest;
    //    private AdRequest adRequestVideo;
    static FragmentMain fragmentAction;
    private int permissionCheck;
    private String dateContact;
    private boolean isRegister = false;
//    private RewardedVideoAd mAd;
//    private boolean mIsRewardedVideoLoading;
//    private final Object mLock = new Object();

    public FragmentMain() {
    }

    public static FragmentMain newInstance(SubCategory subCategory, boolean isMyShops) {
        if (fragmentAction == null)
            createFragment();
        else {
            fragmentAction = null;
            createFragment();
        }
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

    public static void createFragment() {
        fragmentAction = new FragmentMain();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        setupInjection();
        register();
        initDialog();
        if (isMyShop) {
            Utils.writelogFile(getActivity(), "isMyShop true y getMyFavoriteShops(FragmentMain)");
            getMyFavoritesShops();
        } else {
            Utils.writelogFile(getActivity(), "isMyShop false(FragmentMain)");
            if (subCategoryExtra != null) {
                Utils.writelogFile(getActivity(), "dialog show y getListShops(FragmentMain)");
                getListShops();
            }
        }
        initRecyclerViewAdapter();
    }

    private void getMyFavoritesShops() {
        showProgressDialog();
        presenter.getMyFavoriteShops(getActivity());
    }

    private void getListShops() {
        showProgressDialog();
        presenter.getListShops(getActivity(), subCategoryExtra);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Utils.writelogFile(getActivity(), "Se inicia ButterKnife(FragmentMain)");
        ButterKnife.bind(this, view);
        initSwipeRefreshLayout();
        return view;
    }

    public void register() {
        Utils.writelogFile(getActivity(), "register(FragmentMain)");
        if (!isRegister) {
            presenter.onCreate();
            isRegister = true;
        }
    }

    public void unregister() {
        Utils.writelogFile(getActivity(), "unregister(FragmentMain)");
        if (isRegister) {
            presenter.onDestroy();
            isRegister = false;
        }
    }

    public void initDialog() {
        Utils.writelogFile(getActivity(), "Se inicia dialog Oncreate(FragmentMain)");
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
        Utils.writelogFile(getActivity(), "Se inicia Injection(FragmentMain)");
        app = (MyCitysShopsUserApp) getActivity().getApplication();
        app.getFragmentMainComponent(this, this, this).inject(this);
    }

    public void verifySwipeRefresh() {
        Utils.writelogFile(getActivity(), "verifySwipeRefresh(FragmentMain)");
        try {
            if (mSwipeRefreshLayout != null) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
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
                    Utils.writelogFile(getActivity(), "onRefresh(FragmentMain)");
                    presenter.refreshLayout(getActivity(), isMyShop, false);

                }
            });
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "initSwipeRefreshLayout catch error " + e.getMessage() + "(FragmentMain)");
        }
    }

    @Override
    public void setListShops(List<Shop> shops) {
        Utils.writelogFile(getActivity(), "setListShops " + shops.size() + "(FragmentMain)");
        adapter.setShop(shops);
        verifySwipeRefresh();
    }

    @Override
    public void setError(String mgs) {
        Utils.writelogFile(getActivity(), "setError " + mgs + "(FragmentMain)");
        verifySwipeRefresh();
        Utils.showSnackBar(conteiner, mgs);
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
    public void callMyShops() {
        Utils.writelogFile(getActivity(), "callMyShops(FragmentMain)");
        presenter.getMyFavoriteShops(getActivity());
    }

    @Override
    public void isUpdate() {
        Utils.writelogFile(getActivity(), "isUpdate(FragmentMain)");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (pDialog != null)
            if (pDialog.isShowing())
                pDialog.dismiss();
    }

    @Override
    public void followUnFollowSuccess(Shop shop) {
        Utils.writelogFile(getActivity(), "followSuccessUnFollow(FragmentMain)");
        adapter.setUpdateShop(position, shop);
    }

    @Override
    public void onClickFollowOrUnFollow(int position, Shop shop, boolean isFollow) {
        Utils.writelogFile(getActivity(), "onClickFollow(FragmentMain)");
        showProgressDialog();
        this.position = position;
        presenter.onClickFollowOrUnFollow(getActivity(), shop, isMyShop, isFollow);
    }

    @Override
    public void onClickOffer(int position, Shop shop) {
        Utils.writelogFile(getActivity(), "onClickUnFollow y getListOffer y setUpdateOffer(FragmentMain)");
        showProgressDialog();
        shopDialog = shop;
        this.position = position;
        presenter.getListOffer(getActivity(), shop.getID_SHOP_KEY());
        presenter.setUpdateOffer(getActivity(), position, shop);
    }

    @Override
    public void setListOffer(List<Offer> offers) {
        Utils.writelogFile(getActivity(), "setListOffer y DialogOffer(FragmentMain)");
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
        new DialogContact(this, shop, this);
    }

    public void onClickPhone(String number) {
        Utils.writelogFile(getActivity(), "onClickPhone() y validate oldPhones(FragmentMain)");
        if (!Utils.oldPhones()) {
            dateContact = number;
            permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
            checkForPermission(permissionCheck, PERMISSION_CALL);
        }
        Utils.writelogFile(getActivity(), "hasPermission() y CallPhone(Account)");
        if (hasPermission()) {
            callPhone(number);
        }
    }

    @Override
    public void onClickField(String url, String urlApp, int field) {
        switch (field) {
            case 0:
                onClickPhone(url);
                break;
            case 1:
                whatsappIntent();
                break;
            case 2:
                emailIntent(url);
                break;
            case 3:
                openWebURL(url);
                break;
            case 4:
                getRedSocial(url, urlApp, 4);
                break;
            case 5:
                getRedSocial(url, urlApp, 5);
                break;
            case 6:
                getRedSocial(url, urlApp, 6);
                break;
        }
    }

    private void checkForPermission(int permissionCheck, final int PERMISSION) {
        Utils.writelogFile(getActivity(), "is not oldPhones y checkForPermission(FragmentMain)");
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION);
        }
    }

    private boolean hasPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSION_CALL:
                    callPhone(dateContact);
                    break;
            }
        }
    }

    private void callPhone(String number) {
        Utils.writelogFile(getActivity(), "callPhone metodo (FragmentMain)");
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "catch: " + e.getMessage() + " (FragmentMain)");
            setError(e.getMessage());
        }
    }

    private void whatsappIntent() {
        Utils.writelogFile(getActivity(), "whatsappIntent metodo (FragmentMain)");
        try {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.whatsapp");
            startActivity(shareIntent);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "catch: " + e.getMessage() + " (FragmentMain)");
            setError(e.getMessage());
        }
    }

    public void emailIntent(String to) {
        Utils.writelogFile(getActivity(), "emailIntent metodo (FragmentMain)");
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + to));
            startActivity(intent);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "catch: " + e.getMessage() + " (FragmentMain)");
            setError(e.getMessage());
        }
    }

    public void openWebURL(String inURL) {
        Utils.writelogFile(getActivity(), "openWebURL metodo (FragmentMain)");
        try {
            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
            startActivity(browse);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "catch: " + e.getMessage() + " (FragmentMain)");
            setError(e.getMessage());
        }
    }

    private void getRedSocial(String url, String urlApp, int red) {
        Utils.writelogFile(getActivity(), "getRedSocial metodo (FragmentMain)");
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            if (packageManager.getLaunchIntentForPackage(setPackageRed(red)) != null) {
                Utils.writelogFile(getActivity(), "app found app red " + red + "(FragmentMain)");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlApp)));
            } else {
                Utils.writelogFile(getActivity(), "not app found red " + red + "(FragmentMain)");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "catch: " + e.getMessage() + " (FragmentMain)");
            setError(e.getMessage());
        }
    }

    private String setPackageRed(int red) {
        String pack = "";
        switch (red) {
            case 4: //face
                pack = "com.facebook.katana";
                break;
            case 5: //insta
                pack = "com.instagram.android";
                break;
            case 6: //twitter
                pack = "com.twitter.android";
                break;
            case 7: //snap
                pack = "com.snapchat.android";
                break;
        }
        return pack;
    }

    private void setSearch(String s) {
        if (s != null && !s.equals("")) {
            if (isMyShop) {
                showProgressDialog();
                presenter.getShopsSearchFavorites(getActivity(), s);
            } else {
                if (subCategoryExtra != null) {
                    showProgressDialog();
                    presenter.getShopsSearch(getActivity(), subCategoryExtra, s);
                }
            }
        } else {
            if (isMyShop) {
                getMyFavoritesShops();
            } else {
                if (subCategoryExtra != null) {
                    getListShops();
                }
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {

                    setSearch(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    setSearch(s);
                    return false;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregister();
    }

    @Override
    public void onDestroyView() {
        Utils.writelogFile(getActivity(), "onDestroyView(FragmentMain)");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        Utils.writelogFile(getActivity(), "onDestroy(FragmentMain)");
        hideProgressDialog();
        unregister();
        super.onDestroy();
    }
}
