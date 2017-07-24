package com.valdroide.mycitysshopsuser.main.navigation.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.main.broadcast.BroadcastUpdate;
import com.valdroide.mycitysshopsuser.main.legal.ui.LegalActivity;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityPresenter;
import com.valdroide.mycitysshopsuser.main.navigation.dialogs.DialogNotification;
import com.valdroide.mycitysshopsuser.main.navigation.dialogs.DialogWelcome;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.CustomExpandableListAdapter;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.ExpandableListDataSource;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.FragmentNavigationManager;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.NavigationManager;
import com.valdroide.mycitysshopsuser.main.splash.ui.SplashActivity;
import com.valdroide.mycitysshopsuser.main.support.ui.SupportActivity;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity
        implements NavigationActivityView, RewardedVideoAdListener, BottomNavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.navList)
    ExpandableListView mExpandableListView;
    @Bind(R.id.bottom_menu)
    BottomNavigationViewEx bottomNavigationView;
    @Bind(R.id.adView)
    AdView mAdView;

    @Inject
    CustomExpandableListAdapter mExpandableListAdapter;
    @Inject
    List<String> mExpandableListTitle;
    @Inject
    Map<String, List<SubCategory>> mExpandableListData;
    @Inject
    NavigationActivityPresenter presenter;
    @Bind(R.id.linearAdView)
    LinearLayout linearAdView;


    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationManager mNavigationManager;
    List<SubCategory> value;
    private int positionChild = 0;
    private static final int REQUEST_CODE = 0;
    //7*24*60*60*1000 1 week
    private static final int TIME_INTERVAL = 3 * 60 * 60 * 1000;
    //private static final int TIME_INTERVAL = 60000;
    private Intent intentAlarm;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private ProgressDialog pDialog;
    private LayoutInflater mLayoutInflater;
    private RewardedVideoAd mAd;
    private int clickMenu = 0;
    private AdRequest adRequest;
    private String textTool = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Utils.writelogFile(this, "Se inicia ButterKnife(Navigation)");
        ButterKnife.bind(this);
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Navigation)");
        presenter.onCreate();
        initToolBar();
        setOnClickBottomMenu();
        initDialog();
        initAdsVideo();
        BannerAd();
        initNavigationManager();
        initHeaderNav();
        addDrawerItems();
        getCategoryAndSub();
        setupDrawer();
        initBroadCast();
        initAnimationBottomNavigation();
        if (Utils.getIsFirst(this)) {
            showDialogWelcome();
            Utils.setIsFirst(this, false);
        }
        getNotificationOrDrawExtra();
    }

    private void showDialogWelcome() {
        Utils.writelogFile(this, "showDialogWelcome(Navigation)");
        new DialogWelcome(this);
    }

    private void initAnimationBottomNavigation() {
        Utils.writelogFile(this, "initAnimationBottomNavigation(Navigation)");
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(false);
    }

    private void initNavigationManager() {
        Utils.writelogFile(this, "FragmentNavigationManager(Navigation)");
        if (mNavigationManager == null)
            mNavigationManager = FragmentNavigationManager.obtain(this);
    }

    private void initDialog() {
        Utils.writelogFile(this, "Se inicia dialog Oncreate(Navigation)");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
    }

    private void getCategoryAndSub() {
        Utils.writelogFile(this, "getCategoriesAndSubCategories(Navigation)");
        showProgressDialog();
        presenter.getCategoriesAndSubCategories(this);
    }

    public void BannerAd() {
        adRequest = new AdRequest.Builder()
                //    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //  .addTestDevice("B52960D9E6A2A5833E82FEA8ACD4B80C")
                .build();
        mAdView.loadAd(adRequest);

    }

    public void initAdsVideo() {
        Utils.writelogFile(this, "initAdsVideo(Navigation)");
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    public void loadVideo() {
        if (mAd.isLoaded()) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAd.show();
                }
            }, 1000);
        }
    }

    private void loadRewardedVideoAd() {

        AdRequest adRequest = new AdRequest.Builder()
                //              .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //              .addTestDevice("B52960D9E6A2A5833E82FEA8ACD4B80C")
                .build();
        mAd.loadAd(getString(R.string.video_navigation), adRequest);
    }

    public void getNotificationOrDrawExtra() {
        try {
            Utils.writelogFile(this, "getNotificationOrDrawExtra(Navigation)");
            if (getIntent().getBooleanExtra("notification", false)) {
                String name = getIntent().getStringExtra("title");
                String message = getIntent().getStringExtra("messasge");
                String url_shop = getIntent().getStringExtra("url_shop");
                if (name != null && !name.isEmpty() && message != null && !message.isEmpty() && url_shop != null && !url_shop.isEmpty()) {
                    Utils.writelogFile(this, "data notification != null and !empty y show dialog(Navigation)");
                    new DialogNotification(this, name, url_shop, message);
                } else {
                    Utils.writelogFile(this, "data notification null or empty " + getString(R.string.error_notification) + " (Navigation)");
                    setError(getString(R.string.error_notification));
                }
            } else if (getIntent().getBooleanExtra("draw", false)) {
                openFragmentDraw();
                setVisibilityAds(true);
            } else {
                openFragmentMyFavoriteShop();
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "getNotificationOrDrawExtra error: " + e.getMessage() + "(Navigation)");
            setError(e.getMessage());
        }
    }

    public void initBroadCast() {
        Utils.writelogFile(this, "initBroadCast(Navigation)");
        try {
            intentAlarm = new Intent(this, BroadcastUpdate.class);
            boolean isWorking = (PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intentAlarm, PendingIntent.FLAG_NO_CREATE) != null);
            if (!isWorking) {
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TIME_INTERVAL, pendingIntent);
            }
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    public void initToolBar() {
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(Navigation)");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name_title);
        Utils.applyFontForToolbarTitle(this, toolbar);
    }

    public void initHeaderNav() {
        Utils.writelogFile(this, "initHeaderNav(Navigation)");
        try {
            if (mExpandableListView != null) {
                LayoutInflater inflater = getLayoutInflater();
                View listHeaderView = inflater.inflate(R.layout.nav_header_navigation, null, false);
                mExpandableListView.addHeaderView(listHeaderView);
            }
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    private void setupInjection() {
        Utils.writelogFile(this, "Se inicia Injection(Navigation)");
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getNavigationActivityComponent(this, this, this).inject(this);
    }

    @Override
    public void onBackPressed() {
        Utils.writelogFile(this, "onBackPressed(Navigation)");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void setupDrawer() {
        Utils.writelogFile(this, "setupDrawer(Navigation)");
        try {
            mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    getSupportActionBar().setTitle(getString(R.string.app_name));
                    invalidateOptionsMenu();
                }

                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    getSupportActionBar().setTitle(textTool);
                    invalidateOptionsMenu();
                }
            };

            mDrawerToggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(mDrawerToggle);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Utils.writelogFile(this, "onPostCreate(Navigation)");
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.writelogFile(this, "onConfigurationChanged(Navigation)");
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void addDrawerItems() {
        Utils.writelogFile(this, "addDrawerItems(Navigation)");
        try {
            mExpandableListView.setAdapter(mExpandableListAdapter);
            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {

                }
            });

            mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    setClickMenu();
                }
            });

            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    if (v == null) {
                        v = mLayoutInflater.inflate(R.layout.list_item, null);
                    }
                    imageViewItemList(v);
                    value = mExpandableListData.get(mExpandableListTitle.get(groupPosition));
                    textTool = value.get(childPosition).getSUBCATEGORY();
                    positionChild = childPosition;
                    presenter.setUpdateSubCategory(NavigationActivity.this, value.get(childPosition));
                    mNavigationManager.showFragmentAction(value.get(childPosition), false);
                    setVisibilityAds(false);
                    setClickMenu();
                    closeDrawer();

                    return false;
                }
            });
            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousItem = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousItem) {
                        presenter.setUpdateCategory(NavigationActivity.this, mExpandableListAdapter.getGroup(groupPosition).toString());

                        if (mExpandableListView == null) {
                            mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
                        }
                        mExpandableListView.collapseGroup(previousItem); //null
                    }
                    previousItem = groupPosition;
                }
            });

        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation, Repository)");
        }
    }

    private void imageViewItemList(View v) {
        ImageView image = (ImageView) v.findViewById(R.id.imageViewIcon);
        image.setVisibility(View.GONE);
    }

    private void closeDrawer() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void setListCategoriesAndSubCategories(List<Category> categories, List<SubCategory> subCategories) {
        Utils.writelogFile(this, "setListCategoriesAndSubCategories(Navigation)");
        try {
            mExpandableListData = ExpandableListDataSource.getData(categories, subCategories);
            mExpandableListTitle = new ArrayList(mExpandableListData.keySet());
            mExpandableListAdapter.setList(categories, mExpandableListTitle, mExpandableListData);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    @Override
    public void setError(String msg) {
        Utils.writelogFile(this, "setError " + msg + "(Navigation)");
        Utils.showSnackBar(drawer, msg);
    }

    @Override
    public void goToPlace() {
        Utils.writelogFile(this, "Metodo goToPlace Intente Place(Navigation)");
        startActivity(new Intent(this, SplashActivity.class));
    }

    private void setClickMenu() {
        if (clickMenu != 8) {
            clickMenu++;
        } else {
            clickMenu = 0;
            loadVideo();
        }
    }

    @Override
    public void updateAdapter() {
        Utils.writelogFile(this, "Metodo updateAdapter(Navigation)");
        getCategoryAndSub();
    }

    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mAd.pause(this);
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAd.resume(this);
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Navigation)");
        hideProgressDialog();
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utils.writelogFile(this, "onCreateOptionsMenu(Navigation)");
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    private void openFragmentMyFavoriteShop() {
        Utils.writelogFile(this, "openFragmentMyFavoriteShop(Navigation)");
        try {
            textTool = getString(R.string.my_favorites_shops_title);
            getSupportActionBar().setTitle(textTool);
            mNavigationManager.showFragmentAction(null, true);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    private void openFragmentDraw() {
        Utils.writelogFile(this, "openFragmentDraw(Navigation)");
        try {
            textTool = getString(R.string.draw_title_tool);
            getSupportActionBar().setTitle(textTool);
            mNavigationManager.showDrawFragment();
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    private void openFragmentOffer() {
        Utils.writelogFile(this, "openFragmentOffer(Navigation)");
        try {
            textTool = getString(R.string.offer_tool_title);
            getSupportActionBar().setTitle(textTool);
            mNavigationManager.showOfferFragment();
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.writelogFile(this, "onOptionsItemSelected(Navigation)");
        int id = item.getItemId();
        if (id == R.id.action_change_place) {
            Utils.writelogFile(this, "action_change_place click(Navigation)");
            presenter.changePlace(this);
            deleteFragmentId();
        } else if (id == R.id.action_support) {
            Utils.writelogFile(this, "action_support click(Navigation)");
            startActivity(new Intent(this, SupportActivity.class));
            deleteFragmentId();
        } else if (id == R.id.action_legal) {
            Utils.writelogFile(this, "action_legal click(Navigation)");
            startActivity(new Intent(this, LegalActivity.class));
            deleteFragmentId();
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Utils.writelogFile(this, "onOptionsItemSelected click(Navigation)");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    private void setOnClickBottomMenu() {
        Utils.writelogFile(this, "setOnClickBottomMenu(Navigation)");
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setVisibilityAds(boolean isVisible) {
        Utils.writelogFile(this, "setVisibilityAds(Navigation)");
        if (linearAdView == null)
            linearAdView = (LinearLayout) findViewById(R.id.linearAdView);
        if (isVisible) {
            if (linearAdView.getVisibility() == View.GONE)
                linearAdView.setVisibility(View.VISIBLE);
        } else {
            if (linearAdView.getVisibility() == View.VISIBLE)
                linearAdView.setVisibility(View.GONE);
        }
    }

    private void deleteFragmentId() {
        Utils.writelogFile(this, "deleteFragmentId(Navigation)");
        try {

            if (getSupportFragmentManager()
                    .findFragmentById(R.id.container) != null) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();

                fragmentTransaction.remove(getSupportFragmentManager()
                        .findFragmentById(R.id.container));
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Navigation)");
            Utils.showSnackBar(drawer, e.getMessage());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorities:
                openFragmentMyFavoriteShop();
                setVisibilityAds(false);
                break;
            case R.id.action_offers:
                openFragmentOffer();
                setVisibilityAds(true);
                break;
            case R.id.action_draws:
                openFragmentDraw();
                setVisibilityAds(true);
                break;
        }
        closeDrawer();
        return true;
    }
}
