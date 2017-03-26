package com.valdroide.mycitysshopsuser;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyCitysShopsUserAppModule {
    Application application;

    public MyCitysShopsUserAppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
}
