package com.valdroide.mycitysshopsuser.main.offers.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.offers.ui.OfferFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OfferFragmentModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface OfferFragmentComponent {
    void inject(OfferFragment fragment);
}
