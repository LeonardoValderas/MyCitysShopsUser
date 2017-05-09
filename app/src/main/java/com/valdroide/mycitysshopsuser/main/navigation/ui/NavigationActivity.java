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
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.main.broadcast.BroadcastUpdate;
import com.valdroide.mycitysshopsuser.main.legal.ui.LegalActivity;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityPresenter;
import com.valdroide.mycitysshopsuser.main.navigation.dialogs.DialogNotification;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.CustomExpandableListAdapter;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.ExpandableListDataSource;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.FragmentNavigationManager;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.NavigationManager;
import com.valdroide.mycitysshopsuser.main.place.ui.PlaceActivity;
import com.valdroide.mycitysshopsuser.main.support.ui.SupportActivity;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

public class NavigationActivity extends AppCompatActivity
        implements NavigationActivityView, RewardedVideoAdListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.navList)
    ExpandableListView mExpandableListView;
    @Inject
    CustomExpandableListAdapter mExpandableListAdapter;
    @Inject
    List<String> mExpandableListTitle;
    @Inject
    Map<String, List<SubCategory>> mExpandableListData;
    @Inject
    NavigationActivityPresenter presenter;
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
   // private boolean mIsRewardedVideoLoading;
    private final Object mLock = new Object();
    private int clickMenu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Utils.writelogFile(this, "Se inicia ButterKnife(Navigation)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Navigation)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Navigation)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(Navigation)");
        initToolBar();
        Utils.writelogFile(this, "Se inicia dialog Oncreate(Navigation)");
        initDialog();
        Utils.writelogFile(this, "initAdsVideo(Navigation)");
        initAdsVideo();
        Utils.writelogFile(this, "FragmentNavigationManager(Navigation)");
        mNavigationManager = FragmentNavigationManager.obtain(this);
        initHeaderNav();
        addDrawerItems();
        Utils.writelogFile(this, "getCategoriesAndSubCategories(Navigation)");
        pDialog.show();
        presenter.getCategoriesAndSubCategories(this);
        setupDrawer();
        initBroadCast();
        getNotificationExtra();
        openFragmentMyFavoriteShop();
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
    }

    public void initAdsVideo() {
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
//        synchronized (mLock) {
//            if (!mIsRewardedVideoLoading) {
//                mIsRewardedVideoLoading = true;
//                Bundle extras = new Bundle();
//                extras.putBoolean("_noRefresh", true);
        AdRequest adRequest = new AdRequest.Builder()
                //   .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B52960D9E6A2A5833E82FEA8ACD4B80C")
                .build();
        mAd.loadAd(getString(R.string.video_navigation), adRequest);
//            }
//        }
    }

    public void getNotificationExtra() {
        try {
            Utils.writelogFile(this, "getNotificationExtra(Navigation)");
            if (getIntent().getBooleanExtra("notification", false)) {
                String message = getIntent().getStringExtra("messasge");
                String url_shop = getIntent().getStringExtra("url_shop");
                if (message != null && url_shop != null) {
                    if (!message.isEmpty() && !url_shop.isEmpty()) {
                        Utils.writelogFile(this, "!message.isEmpty() && !url_shop.isEmpty() y show dialog(Navigation)");
                        new DialogNotification(this, url_shop, message);
                    } else {
                        Utils.writelogFile(this, "message.isEmpty() || url_shop.isEmpty(): " + getString(R.string.error_notification) + " (Navigation)");
                        setError(getString(R.string.error_notification));
                    }
                } else {
                    Utils.writelogFile(this, "message == null && url_shop == null " + getString(R.string.error_notification) + " (Navigation)");
                    setError(getString(R.string.error_notification));
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "getNotificationExtra error: " + e.getMessage() + "(Navigation)");
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name_title);
        Utils.applyFontForToolbarTitle(this, toolbar);
    }

    public void initHeaderNav() {
        Utils.writelogFile(this, "initHeaderNav(Navigation)");
        try {
            LayoutInflater inflater = getLayoutInflater();
            View listHeaderView = inflater.inflate(R.layout.nav_header_navigation, null, false);
            mExpandableListView.addHeaderView(listHeaderView);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    private void setupInjection() {
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getNavigationActivityComponent(this, this, this).inject(this);
    }

    @Override
    public void onBackPressed() {
        Utils.writelogFile(this, "onBackPressed(Navigation)");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
                    if (value != null)
                        if (value.get(positionChild) != null)
                            getSupportActionBar().setTitle(value.get(positionChild).getSUBCATEGORY());
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
                    ImageView image = (ImageView) v.findViewById(R.id.imageViewIcon);
                    image.setVisibility(View.GONE);
                    value = mExpandableListData.get(mExpandableListTitle.get(groupPosition));
                    positionChild = childPosition;
                    presenter.setUpdateSubCategory(NavigationActivity.this, value.get(childPosition));
                    mNavigationManager.showFragmentAction(value.get(childPosition), false);
                    setClickMenu();
                    drawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousItem = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousItem) {
                        presenter.setUpdateCategory(NavigationActivity.this, mExpandableListAdapter.getGroup(groupPosition).toString());
                        mExpandableListView.collapseGroup(previousItem);
                    }
                    previousItem = groupPosition;
                }
            });

        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation, Repository)");
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
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void setError(String msg) {
        Utils.writelogFile(this, "setError " + msg + "(Navigation)");
        if (pDialog.isShowing())
            pDialog.dismiss();
        Utils.showSnackBar(drawer, msg);
    }

    @Override
    public void goToPlace() {
        Utils.writelogFile(this, "Metodo goToPlace Intente Place(Navigation)");
        startActivity(new Intent(this, PlaceActivity.class));
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
        presenter.getCategoriesAndSubCategories(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAd.pause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAd.resume(this);
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Navigation)");
        pDialog.dismiss();
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utils.writelogFile(this, "onCreateOptionsMenu(Navigation)");
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    public void openFragmentMyFavoriteShop() {
        Utils.writelogFile(this, "openFragmentMyFavoriteShop(Navigation)");
        try {
            getSupportActionBar().setTitle(getString(R.string.my_favorites_shops_title));

            mNavigationManager.showFragmentAction(null, true);
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
        if (id == R.id.action_favorite) {
            Utils.writelogFile(this, "action_favorite click(Navigation)");
            pDialog.show();
            openFragmentMyFavoriteShop();
            if (pDialog.isShowing())
                pDialog.dismiss();
            return true;
        } else if (id == R.id.action_change_place) {
            Utils.writelogFile(this, "action_change_place click(Navigation)");
            presenter.changePlace(this);
        } else if (id == R.id.action_support) {
            Utils.writelogFile(this, "action_support click(Navigation)");
            startActivity(new Intent(this, SupportActivity.class));
        } else if (id == R.id.action_legal) {
            Utils.writelogFile(this, "action_legal click(Navigation)");
            startActivity(new Intent(this, LegalActivity.class));
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
}
