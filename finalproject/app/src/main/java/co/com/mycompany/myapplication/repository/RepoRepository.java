package co.com.mycompany.myapplication.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.com.mycompany.myapplication.AppExecutors;
import co.com.mycompany.myapplication.api.ApiResponse;
import co.com.mycompany.myapplication.api.WebServiceApi;
import co.com.mycompany.myapplication.db.GitHubDb;
import co.com.mycompany.myapplication.db.RepoDao;
import co.com.mycompany.myapplication.model.Repo;
import co.com.mycompany.myapplication.utils.RateLimiter;

@Singleton
public class RepoRepository {
    private final GitHubDb db;
    private final RepoDao repoDao;
    private final WebServiceApi webServiceApi;
    protected final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public RepoRepository(AppExecutors appExecutors, GitHubDb db,
                          RepoDao repoDao, WebServiceApi webServiceApi) {
        this.db = db;
        this.appExecutors = appExecutors;
        this.repoDao = repoDao;
        this.webServiceApi = webServiceApi;
    }

    public LiveData<Resource<List<Repo>>> loadRepos(String owner) {
        return new NetWorkBoundResource<List<Repo>, List<Repo>>(appExecutors) {

            @Override
            protected LiveData<List<Repo>> loadFromDb() {
                return repoDao.loadRepositories(owner);
            }

            @Override
            protected boolean shouldFetch(List<Repo> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(owner);
            }

            @Override
            protected void saveCallResult(List<Repo> item) {
                repoDao.insertRepos(item);
            }

            @Override
            protected LiveData<ApiResponse<List<Repo>>> createCall() {
                return webServiceApi.getRepos(owner);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(owner);
            }
        }.asLiveData();
    }
}
