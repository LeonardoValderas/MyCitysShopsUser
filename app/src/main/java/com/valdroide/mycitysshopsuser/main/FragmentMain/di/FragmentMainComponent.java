package com.valdroide.mycitysshopsuser.main.FragmentMain.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.FragmentMain;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {FragmentMainModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface FragmentMainComponent {
      void inject(FragmentMain fragment);
}
