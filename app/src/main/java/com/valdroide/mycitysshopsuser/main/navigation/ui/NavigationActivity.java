package com.valdroide.mycitysshopsuser.main.navigation.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

//import com.github.arturogutierrez.Badges;
//import com.github.arturogutierrez.BadgesNotSupportedException;
import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.Support;
import com.valdroide.mycitysshopsuser.main.broadcast.BroadcastUpdate;
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

import static android.app.AlarmManager.INTERVAL_HALF_DAY;

public class NavigationActivity extends AppCompatActivity
        implements NavigationActivityView {

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
    private static final int REQUEST_CODE = 1;
    private static final int TIME_INTERVAL = 1000 * 60 * 60 * 5;
    private Intent intentAlarm;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private boolean isNotification = false;
    private String message = "";

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
        Utils.writelogFile(this, "FragmentNavigationManager(Navigation)");
        mNavigationManager = FragmentNavigationManager.obtain(this);
        initHeaderNav();
        addDrawerItems();
        Utils.writelogFile(this, "getCategoriesAndSubCategories(Navigation)");
        presenter.getCategoriesAndSubCategories(this);
        setupDrawer();
        initBroadCast();
        getNotificationExtra();
//        int badgeCount = 1;
//        ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+

    }

    public void getNotificationExtra() {
        try {
            Utils.writelogFile(this, "getNotificationExtra(Navigation)");
            if (getIntent().getBooleanExtra("notification", false)) {
                message = getIntent().getStringExtra("messasge");
                int id = getIntent().getIntExtra("id_shop", 0);
                if (id != 0) {
                    Utils.writelogFile(this, "id != 0 getURLShop (Navigation)");
                    presenter.getUrlShop(this, getIntent().getIntExtra("id_shop", 0));
                } else {
                    Utils.writelogFile(this, "id == 0 error: " + getString(R.string.notification_error) + " (Navigation)");
                    setError(getString(R.string.notification_error));
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
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC, TIME_INTERVAL, TIME_INTERVAL, pendingIntent);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    public void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
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
                }
            });

            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    value = mExpandableListData.get(mExpandableListTitle.get(groupPosition));
                    positionChild = childPosition;
                    mNavigationManager.showFragmentAction(value.get(childPosition), false);
                    //       conteoClick();
                    drawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousItem = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousItem)
                        mExpandableListView.collapseGroup(previousItem);
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
            mExpandableListAdapter.setList(mExpandableListTitle, mExpandableListData);
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
        startActivity(new Intent(this, PlaceActivity.class));
    }

    @Override
    public void setUrlShowDialog(String url) {
        Utils.writelogFile(this, "setUrlShowDialog(Navigation)");
        new DialogNotification(this, url, message);
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Navigation)");
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
            getSupportActionBar().setTitle("Mis Locales Favoritos");
            mNavigationManager.showFragmentAction(null, true);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "catch error " + e.getMessage() + "(Navigation)");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.writelogFile(this, "onOptionsItemSelected(Navigation)");
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            Utils.writelogFile(this, "action_favorite click(Navigation)");
            openFragmentMyFavoriteShop();
            return true;
        } else if (id == R.id.action_change_place) {
            Utils.writelogFile(this, "action_change_place click(Navigation)");
            presenter.changePlace(this);
        } else if (id == R.id.action_support) {
            Utils.writelogFile(this, "action_support click(Navigation)");
            startActivity(new Intent(this, SupportActivity.class));
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Utils.writelogFile(this, "onOptionsItemSelected click(Navigation)");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
