package co.com.mycompany.myapplication.repository;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import co.com.mycompany.myapplication.AppExecutors;
import co.com.mycompany.myapplication.api.ApiResponse;

/**
 * Puede obtener datos del repositorio o del servicio
 */
public abstract class NetWorkBoundResource<ResultType, RequestType> {
    private final AppExecutors appExecutors;

    // Permite tener varios objetos live data, evita observar muchos live data
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    NetWorkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType data) {
                // Pare de observar
                result.removeSource(dbSource);
                if (NetWorkBoundResource.this.shouldFetch(data)) {
                    NetWorkBoundResource.this.fetchFromNetWork(dbSource);
                } else {
                    result.addSource(dbSource, (ResultType newData) -> {
                        NetWorkBoundResource.this.setValue(Resource.success(newData));
                    });
                }
            }
        });
    }

    private void fetchFromNetWork(final LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        // Base de datos
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType resultType) {
                NetWorkBoundResource.this.setValue(Resource.loading(resultType));
            }
        });

        // Api
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            if (response.isSuccessFull()) {
                appExecutors.getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        NetWorkBoundResource
                                .this.saveCallResult(NetWorkBoundResource
                                .this.processResponse(response));
                        appExecutors.getMainThread().execute(() -> {
                            result.addSource(NetWorkBoundResource.this.loadFromDb(), newData -> {
                                NetWorkBoundResource.this.setValue(Resource.success(newData));
                            });
                        });
                    }
                });
            } else {
                onFetchFailed();;
                result.addSource(dbSource, newData -> {
                    setValue(Resource.error(response.errorMessage, newData));
                });
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!result.getValue().equals(newValue)) {
            result.setValue(newValue);
        }
    }

    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @MainThread
    protected abstract boolean shouldFetch(ResultType data);

    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    // Segundo plano
    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(RequestType item);

    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

}
