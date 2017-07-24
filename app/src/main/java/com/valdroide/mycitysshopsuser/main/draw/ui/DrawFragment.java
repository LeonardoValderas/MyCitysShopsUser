package com.valdroide.mycitysshopsuser.main.draw.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.main.draw.DrawFragmentPresenter;
import com.valdroide.mycitysshopsuser.main.draw.dialogs.DialogDraw;
import com.valdroide.mycitysshopsuser.main.draw.ui.adapters.DrawFragmentAdapter;
import com.valdroide.mycitysshopsuser.main.draw.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawFragment extends Fragment implements DrawFragmentView, OnItemClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.conteiner)
    FrameLayout conteiner;

    @Inject
    DrawFragmentAdapter adapter;
    @Inject
    DrawFragmentPresenter presenter;

    private ProgressDialog pDialog;
    private int position;
    private DialogDraw dialogDraw;
    private boolean isRegister = false;
    static DrawFragment drawFragment;

    public DrawFragment() {
    }

    public static DrawFragment newInstance() {
        if (drawFragment == null)
            createFragment();
        else{
            drawFragment = null;
            createFragment();
        }
        return drawFragment;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Utils.writelogFile(getActivity(), "Se inicia Injection(FragmentMain)");
        setupInjection();
        register();
        initDialog();
        initRecyclerView();
        initSwipeRefreshLayout(getActivity());
        getDrawList();
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
        Utils.writelogFile(getActivity(), "setupInjection(DrawFragment)");
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getActivity().getApplication();
        app.getDrawFragmentComponent(this, this, this).inject(this);
    }

    private void initRecyclerView() {
        Utils.writelogFile(getActivity(), "initRecyclerView(DrawFragment)");
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(getActivity(), "initRecyclerView catch error " + e.getMessage() + "(DrawFragment)");
        }
    }

    private void initDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.process));
        pDialog.setCancelable(false);
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

    @Override
    public void onClickDraw(int position, Draw draw) {
        if (draw.getIS_WINNER() == 1) {
            Utils.showSnackBar(conteiner, getString(R.string.you_are_winner_click));
        } else if (draw.getPARTICIPATION() == 1) {
            Utils.showSnackBar(conteiner, getString(R.string.is_participate_msg));
        } else {
            this.position = position;
            dialogDraw = new DialogDraw(this, draw);
        }
    }

    public void participateDraw(Draw draw, String dni, String name) {
        showProgressDialog();
        presenter.participate(getActivity(), draw, dni, name);
    }

    public static void createFragment() {
        drawFragment = new DrawFragment();
    }

    @Override
    public void setDraws(List<Draw> draws) {
        adapter.setDraw(draws);
    }

    @Override
    public void setError(String error) {
        if (dialogDraw != null)
            dialogDraw.alertDialog.dismiss();

        Utils.showSnackBar(conteiner, error);
    }

    @Override
    public void participationSuccess() {
        adapter.setUpdateDraw(position);
        dialogDraw.alertDialog.dismiss();
        Utils.showSnackBar(conteiner, getString(R.string.draw_participate_ok));
    }

    @Override
    public void withoutChange() {
        verifySwipeRefresh();
    }

    @Override
    public void setDrawsRefresh() {
        verifySwipeRefresh();
        showProgressDialog();
        presenter.getDraws(getActivity());
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

    private void register() {
        if (!isRegister) {
            presenter.onCreate();
            isRegister = true;
        }
    }

    private void unregister() {
        if (isRegister) {
            presenter.onDestroy();
            isRegister = false;
        }
    }

    private void getDrawList() {
        showProgressDialog();
        presenter.getDraws(getActivity());
    }

    private void setSearch(String s) {
        if (s != null && !s.equals("")) {
            showProgressDialog();
            presenter.getDrawSearch(getActivity(), s);
        } else {
            getDrawList();
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
    public void onDestroy() {
        if (presenter != null)
            presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(getActivity());
    }
}
