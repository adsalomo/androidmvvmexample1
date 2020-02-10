package co.com.mycompany.myapplication.di;

import co.com.mycompany.myapplication.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Hace que FragmentBuildersModuleModule sea un sub componente de main activity
 */
@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

}
