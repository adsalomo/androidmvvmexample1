package co.com.mycompany.myapplication.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import co.com.mycompany.myapplication.api.WebServiceApi;
import co.com.mycompany.myapplication.db.GitHubDb;
import co.com.mycompany.myapplication.db.RepoDao;
import co.com.mycompany.myapplication.db.UserDao;
import co.com.mycompany.myapplication.utils.LiveDataCallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dependencias generales de la app
 */
@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    WebServiceApi provideGitHubService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(WebServiceApi.class);
    }

    @Singleton
    @Provides
    GitHubDb provideDb(Application application) {
        return Room.databaseBuilder(application, GitHubDb.class, "github.db").build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(GitHubDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    RepoDao provideRepoDao(GitHubDb db) {
        return db.repoDao();
    }
}
