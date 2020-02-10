package co.com.mycompany.myapplication.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import co.com.mycompany.myapplication.ui.repo.RepoViewModel;
import co.com.mycompany.myapplication.ui.search.SearchViewModel;
import co.com.mycompany.myapplication.ui.user.UserViewModel;
import co.com.mycompany.myapplication.viewmodel.GitHubViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Instancias que necesitamos: relacionada con GitHubViewModelFactory
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RepoViewModel.class)
    abstract ViewModel bindRepoViewModel(RepoViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(GitHubViewModelFactory factory);
}
