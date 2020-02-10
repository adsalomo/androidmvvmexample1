package co.com.mycompany.myapplication.di;

import android.app.Application;

import javax.inject.Singleton;

import co.com.mycompany.myapplication.GitHubApp;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Son el puente entre los modulos y la parte del codigo que va hacer la injeccion de dependencia
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class,
        AppModule.class, MainActivityModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(GitHubApp gitHubApp);
}
