package com.valdroide.mycitysshopsuser.main.navigation;


public class NavigationActivityInteractorImpl implements NavigationActivityInteractor {
    private NavigationActivityRepository repository;

    public NavigationActivityInteractorImpl(NavigationActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getCategoriesAndSubCategories() {
        repository.getCategoriesAndSubCategories();
    }

}
