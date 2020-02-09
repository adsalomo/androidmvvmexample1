package co.com.mycompany.myapplication.repository;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.com.mycompany.myapplication.AppExecutors;
import co.com.mycompany.myapplication.api.ApiResponse;
import co.com.mycompany.myapplication.api.WebServiceApi;
import co.com.mycompany.myapplication.db.UserDao;
import co.com.mycompany.myapplication.model.User;

@Singleton
public class UserRepository {
    private final UserDao userDao;
    private final WebServiceApi webServiceApi;
    private final AppExecutors appExecutors;

    @Inject
    UserRepository(AppExecutors appExecutors, UserDao userDao,
                   WebServiceApi webServiceApi) {
        this.userDao = userDao;
        this.webServiceApi = webServiceApi;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<User>> loadUser(String login) {
        return new NetWorkBoundResource<User, User>(appExecutors) {
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @Override
            protected boolean shouldFetch(User data) {
                return data == null;
            }

            @Override
            protected void saveCallResult(User item) {
                userDao.inser(item);
            }

            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webServiceApi.getUser(login);
            }
        }.asLiveData();
    }
}
