package com.valdroide.mycitysshopsuser.main.navigation;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.navigation.events.NavigationActivityEvent;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivityView;

import org.greenrobot.eventbus.Subscribe;

public class NavigationActivityPresenterImpl implements NavigationActivityPresenter {


    private NavigationActivityView view;
    private EventBus eventBus;
    private NavigationActivityInteractor interactor;

    public NavigationActivityPresenterImpl(NavigationActivityView view, EventBus eventBus, NavigationActivityInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void getCategoriesAndSubCategories(Context context) {
        interactor.getCategoriesAndSubCategories(context);
    }

    @Override
    public void changePlace(Context context) {
        interactor.changePlace(context);
    }

    @Override
    public void setUpdateCategory(Context context, String category) {
        interactor.setUpdateCategory(context, category);
    }

    @Override
    public void setUpdateSubCategory(Context context, SubCategory subCategory) {
        interactor.setUpdateSubCategory(context, subCategory);
    }

    @Override
    @Subscribe
    public void onEventMainThread(NavigationActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case NavigationActivityEvent.GETCATEGORIESANDSUBCATEGORIES:
                    view.setListCategoriesAndSubCategories(event.getCategories(), event.getSubCategories());
                    view.hideProgressDialog();
                    break;
                case NavigationActivityEvent.CHANGEPLACE:
                    view.goToPlace();
                    break;
                case NavigationActivityEvent.ERROR:
                    view.hideProgressDialog();
                    view.setError(event.getError());
                    break;
                case NavigationActivityEvent.CATEGORORYSUBCATEGORYUPDATE:
                    view.updateAdapter();
                    view.hideProgressDialog();
                    break;
            }
        }
    }
}
