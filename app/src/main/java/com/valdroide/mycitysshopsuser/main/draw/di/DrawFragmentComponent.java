package com.valdroide.mycitysshopsuser.main.draw.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.draw.ui.DrawFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DrawFragmentModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface DrawFragmentComponent {
    void inject(DrawFragment fragment);
}
