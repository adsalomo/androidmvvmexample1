package co.com.mycompany.myapplication.di;

import co.com.mycompany.myapplication.ui.repo.RepoFragment;
import co.com.mycompany.myapplication.ui.search.SearchFragment;
import co.com.mycompany.myapplication.ui.user.UserFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    /**
     * Van a poder ser injectados con cualquier dependencia
     * @return
     */
    @ContributesAndroidInjector
    abstract RepoFragment contributeRepoFragment();

    @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
