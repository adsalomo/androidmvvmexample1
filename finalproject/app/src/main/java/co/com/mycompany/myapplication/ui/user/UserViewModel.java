package co.com.mycompany.myapplication.ui.user;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import co.com.mycompany.myapplication.model.Repo;
import co.com.mycompany.myapplication.model.User;
import co.com.mycompany.myapplication.repository.RepoRepository;
import co.com.mycompany.myapplication.repository.Resource;
import co.com.mycompany.myapplication.repository.UserRepository;
import co.com.mycompany.myapplication.utils.AbsentLiveData;

public class UserViewModel extends ViewModel {
    final MutableLiveData<String> login = new MutableLiveData<>();
    private final LiveData<Resource<List<Repo>>> respositories;
    private final LiveData<Resource<User>> user;

    @Inject
    public UserViewModel(UserRepository userRepository,
                         RepoRepository repoRepository) {
        user = Transformations.switchMap(login, new Function<String, LiveData<Resource<User>>>() {
            @Override
            public LiveData<Resource<User>> apply(String login) {
                if (login == null) {
                    return AbsentLiveData.create();
                } else  {
                    return userRepository.loadUser(login);
                }
            }
        });
        respositories = Transformations.switchMap(login, login -> {
            if (login == null) {
                return AbsentLiveData.create();
            } else {
                return repoRepository.loadRepos(login);
            }
        });
    }

    public void setLogin(String login) {
        if (login.equals(this.login)) {
            return;
        }
        this.login.setValue(login);
    }

    public LiveData<Resource<User>> getUser() {
        return this.user;
    }

    public LiveData<Resource<List<Repo>>> getRespositories() {
        return this.respositories;
    }
}
