package com.valdroide.mycitysshopsuser.main.offers.ui;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.main.offers.OfferFragmentPresenter;
import com.valdroide.mycitysshopsuser.main.offers.ui.adapters.OfferFragmentAdapter;
import com.valdroide.mycitysshopsuser.main.offers.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferFragment extends Fragment implements OfferFragmentView, OnItemClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.conteiner)
    FrameLayout conteiner;
    private boolean isRegister = false;
    private ProgressDialog pDialog;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;

    @Inject
    OfferFragmentAdapter adapter;
    @Inject
    OfferFragmentPresenter presenter;

    static OfferFragment activitiesFragment;

    public static OfferFragment newInstance() {
        if (activitiesFragment == null)
            createFragment();
        else {
            activitiesFragment = null;
            createFragment();
        }
        return activitiesFragment;
    }

    public OfferFragment() {
    }

    public static void createFragment() {
        activitiesFragment = new OfferFragment();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        setupInjection();
        register();
        initDialog();
        initRecyclerView();
        initSwipeRefreshLayout(getActivity());
        getOffers();
    }

    private void getOffers() {
        showProgressDialog();
        presenter.getOffers(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void setupInjection() {
        Utils.writelogFile(getActivity(), "setupInjection(OfferFragment)");
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getActivity().getApplication();
        app.getOfferFragmentComponent(this, this, this).inject(this);
    }

    private void initDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.process));
        pDialog.setCancelable(false);
    }

    private void initRecyclerView() {
        Utils.writelogFile(getActivity(), "initRecyclerView(DrawFragment)");
        try {
            gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gaggeredGridLayoutManager);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "initRecyclerView catch error " + e.getMessage() + "(DrawFragment)");
        }
    }

    private void initSwipeRefreshLayout(final Context context) {
        Utils.writelogFile(getActivity(), "initSwipeRefreshLayout(DrawFragment)");
        try {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Utils.writelogFile(context, "onRefresh(DrawFragment)");
                    presenter.refreshLayout(context);
                }
            });
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "initSwipeRefreshLayout catch error " + e.getMessage() + "(DrawFragment)");
        }
    }

    private void verifySwipeRefresh() {
        Utils.writelogFile(getActivity(), "verifySwipeRefresh(DrawFragment)");
        try {
            if (mSwipeRefreshLayout != null) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "verifySwipeRefresh catch error " + e.getMessage() + "(DrawFragment)");
        }
    }

    @Override
    public void setOffers(List<Offer> offers) {
        adapter.setDraw(offers);
    }

    @Override
    public void setError(String error) {
        hideProgressDialog();
        Utils.showSnackBar(conteiner, error);
    }

    @Override
    public void withoutChange() {
        verifySwipeRefresh();
    }

    @Override
    public void setOffersRefresh() {
        verifySwipeRefresh();
        showProgressDialog();
        presenter.getOffers(getActivity());
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

    public void register() {
        if (!isRegister) {
            presenter.onCreate();
            isRegister = true;
        }
    }

    public void unregister() {
        if (isRegister) {
            presenter.onDestroy();
            isRegister = false;
        }
    }

    private void getOfferSearch(String s) {
        if (s != null && !s.equals("")) {
            showProgressDialog();
            presenter.getOfferSearch(getActivity(), s);
        } else {
            getOffers();
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

                    getOfferSearch(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    getOfferSearch(s);
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
    public void onDestroy() {
        Utils.writelogFile(getActivity(), "onDestroy(FragmentMain)");
        hideProgressDialog();
        unregister();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClickOffer(int position, Offer offer) {

    }
}
