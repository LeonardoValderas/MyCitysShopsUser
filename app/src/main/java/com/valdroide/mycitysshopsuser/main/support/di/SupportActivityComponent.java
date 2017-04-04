package com.valdroide.mycitysshopsuser.main.support.di;
import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.support.ui.SupportActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SupportActivityModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface SupportActivityComponent {
      void inject(SupportActivity activity);
}
