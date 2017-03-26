package com.valdroide.mycitysshopsuser.main.navigation.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityPresenter;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.CustomExpandableListAdapter;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.ExpandableListDataSource;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.FragmentNavigationManager;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.NavigationManager;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity
        implements NavigationActivityView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    //@Bind(R.id.content_navigation)
    //RelativeLayout content;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    //  @Bind(R.id.nav_view)
    // NavigationView navigationView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        setupInjection();
        presenter.onCreate();
        initToolBar();
        mNavigationManager = FragmentNavigationManager.obtain(this);
        initHeaderNav();
        addDrawerItems();
        presenter.getCategoriesAndSubCategories();
        setupDrawer();
    }

    public void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home); for image
    }

    public void initHeaderNav() {
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header_navigation, null, false);
        mExpandableListView.addHeaderView(listHeaderView);
    }

    private void setupInjection() {
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getNavigationActivityComponent(this, this, this).inject(this);
    }

    @Override
    public void onBackPressed() {
        //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupDrawer() {
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
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void addDrawerItems() {
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
                mNavigationManager.showFragmentAction(value.get(childPosition));
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
    }

    @Override
    public void setListCategoriesAndSubCategories(List<Category> categories, List<SubCategory> subCategories) {
        mExpandableListData = ExpandableListDataSource.getData(categories, subCategories);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());
        mExpandableListAdapter.setList(mExpandableListTitle, mExpandableListData);
    }

    @Override
    public void setError(String msg) {
        Utils.showSnackBar(drawer, msg);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.navigation, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
