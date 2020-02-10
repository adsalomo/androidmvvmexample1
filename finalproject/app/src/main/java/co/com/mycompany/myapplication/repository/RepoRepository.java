package co.com.mycompany.myapplication.repository;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Transaction;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.com.mycompany.myapplication.AppExecutors;
import co.com.mycompany.myapplication.api.ApiResponse;
import co.com.mycompany.myapplication.api.WebServiceApi;
import co.com.mycompany.myapplication.db.GitHubDb;
import co.com.mycompany.myapplication.db.RepoDao;
import co.com.mycompany.myapplication.model.Contributor;
import co.com.mycompany.myapplication.model.Repo;
import co.com.mycompany.myapplication.model.RepoSearchResponse;
import co.com.mycompany.myapplication.model.RepoSearchResult;
import co.com.mycompany.myapplication.utils.AbsentLiveData;
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

    public LiveData<Resource<Repo>> loadRepo(String owner, String name) {
        return new NetWorkBoundResource<Repo, Repo>(appExecutors) {
            @Override
            protected LiveData<Repo> loadFromDb() {
                return repoDao.load(owner, name);
            }

            @Override
            protected boolean shouldFetch(Repo data) {
                return data != null;
            }

            @Override
            protected void saveCallResult(Repo item) {
                repoDao.insert(item);
            }

            @Override
            protected LiveData<ApiResponse<Repo>> createCall() {
                return webServiceApi.getRepo(owner, name);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Contributor>>> loadContributors(String owner, String name) {
        return new NetWorkBoundResource<List<Contributor>, List<Contributor>>(appExecutors) {

            @Override
            protected LiveData<List<Contributor>> loadFromDb() {
                return repoDao.loadContributors(name, owner);
            }

            @Override
            protected boolean shouldFetch(List<Contributor> data) {
                return data == null || data.isEmpty();
            }

            @Override
            @Transaction
            protected void saveCallResult(List<Contributor> item) {
                for (Contributor contributor : item) {
                    contributor.setRepoName(name);
                    contributor.setRepoOwner(owner);
                }

                repoDao.createRepoIfNotExists(
                        new Repo(Repo.UNKNOWN_ID, name,
                                owner.concat("/").concat(name),
                                "", 0, new Repo.Owner(owner, null)));
            }

            @Override
            protected LiveData<ApiResponse<List<Contributor>>> createCall() {
                return webServiceApi.getContributors(owner, name);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> searchNextPage(String query) {
        FetchNextSearchPageTask fetchNextSearchPageTask =
                new FetchNextSearchPageTask(query, webServiceApi, db);
        appExecutors.getNetWorkIO().execute(fetchNextSearchPageTask);
        return fetchNextSearchPageTask.getLiveData();
    }

    public LiveData<Resource<List<Repo>>> search(String query) {
        return new NetWorkBoundResource<List<Repo>, RepoSearchResponse>(appExecutors) {

            @Override
            protected LiveData<List<Repo>> loadFromDb() {
                return Transformations.switchMap(repoDao.search(query),
                        new Function<RepoSearchResult, LiveData<List<Repo>>>() {
                            @Override
                            public LiveData<List<Repo>> apply(RepoSearchResult input) {
                                if (input == null) {
                                    return AbsentLiveData.create();
                                } else {
                                    return repoDao.loadOrdered(input.repoIds);
                                }
                            }
                        });
            }

            @Override
            protected boolean shouldFetch(List<Repo> data) {
                return data == null || data.isEmpty();
            }

            @Override
            @Transaction
            protected void saveCallResult(RepoSearchResponse item) {
                List<Integer> repoIds = item.getRepoIds();
                RepoSearchResult repoSearchResult = new RepoSearchResult(
                        query, repoIds, item.getTotal(), item.getNextPage()
                );
                repoDao.insertRepos(item.getItems());
                repoDao.insert(repoSearchResult);
            }

            @Override
            protected LiveData<ApiResponse<RepoSearchResponse>> createCall() {
                return webServiceApi.searchRepos(query);
            }

            @Override
            protected RepoSearchResponse processResponse(ApiResponse<RepoSearchResponse> response) {
                RepoSearchResponse body = response.body;
                if (body != null) {
                    body.setNextPage(response.getNextPage());
                }
                return body;
            }
        }.asLiveData();
    }
}
