package com.valdroide.mycitysshopsuser.main.offer.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.offer.ui.OfferActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OfferActivityModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface OfferActivityComponent {
      void inject(OfferActivity activity);
}
