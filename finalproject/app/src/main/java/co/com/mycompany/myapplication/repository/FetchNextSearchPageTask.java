package co.com.mycompany.myapplication.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import co.com.mycompany.myapplication.api.ApiResponse;
import co.com.mycompany.myapplication.api.WebServiceApi;
import co.com.mycompany.myapplication.db.GitHubDb;
import co.com.mycompany.myapplication.model.RepoSearchResponse;
import co.com.mycompany.myapplication.model.RepoSearchResult;
import retrofit2.Response;

public class FetchNextSearchPageTask implements Runnable {
    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String query;
    private final WebServiceApi webServiceApi;
    private final GitHubDb db;

    public FetchNextSearchPageTask(String query, WebServiceApi webServiceApi, GitHubDb db) {
        this.query = query;
        this.webServiceApi = webServiceApi;
        this.db = db;
    }

    @Override
    public void run() {
        RepoSearchResult current = db.repoDao().findSearchResult(query);
        if (current == null) {
            liveData.postValue(null);
            return;
        }

        final Integer nextPage = current.next;
        if (nextPage == null) {
            liveData.postValue(Resource.success(false));
            return;
        }

        try {
            Response<RepoSearchResponse> response = webServiceApi.searchRepos(query, nextPage).execute();

            ApiResponse<RepoSearchResponse> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessFull()) {
                List<Integer> ids = new ArrayList<>();
                ids.addAll(current.repoIds);
                ids.addAll((apiResponse.body.getRepoIds()));
                RepoSearchResult merged =
                        new RepoSearchResult(query, ids, apiResponse.body.total, apiResponse.getNextPage());
                try {
                    db.beginTransaction();
                    db.repoDao().insert(merged);
                    db.repoDao().insertRepos(apiResponse.body.getItems());
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                liveData.postValue(Resource.success(apiResponse.getNextPage() != null));
            } else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, true));
            }
        } catch (Exception ex) {
            liveData.postValue(Resource.error(ex.getLocalizedMessage(), true));
        }
    }

    LiveData<Resource<Boolean>> getLiveData() {
        return this.liveData;
    }
}
