package co.com.mycompany.myapplication.ui.repo;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import co.com.mycompany.myapplication.model.Contributor;
import co.com.mycompany.myapplication.model.Repo;
import co.com.mycompany.myapplication.repository.RepoRepository;
import co.com.mycompany.myapplication.repository.Resource;
import co.com.mycompany.myapplication.utils.AbsentLiveData;

public class RepoViewModel extends ViewModel {
    private MutableLiveData<RepoId> repoId;
    private final LiveData<Resource<Repo>> repo;
    private final LiveData<Resource<List<Contributor>>> contritbutors;

    @Inject
    public RepoViewModel(RepoRepository repository) {
        this.repoId = new MutableLiveData<>();
        repo = Transformations.switchMap(repoId, new Function<RepoId, LiveData<Resource<Repo>>>() {
            @Override
            public LiveData<Resource<Repo>> apply(RepoId input) {
                if (input.isEmpty()) {
                    return AbsentLiveData.create();
                } else {
                    return repository.loadRepo(input.owner, input.name);
                }
            }
        });

        contritbutors = Transformations.switchMap(repoId, input -> {
            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return repository.loadContributors(input.owner, input.name);
            }
        });
    }

    public LiveData<Resource<Repo>> getRepo() {
        return this.repo;
    }

    public LiveData<Resource<List<Contributor>>> getContritbutors() {
        return this.contritbutors;
    }

    public void retry() {
        RepoId current = repoId.getValue();
        if (current != null && !current.isEmpty()) {
            repoId.setValue(current);
        }
    }

    public void setId(String owner, String name) {
        RepoId update = new RepoId(owner, name);
        if (update.equals(repoId.getValue())) {
            return;
        }
        repoId.setValue(update);
    }

    static class RepoId {
        private final String owner;
        private final String name;

        RepoId(String owner, String name) {
            this.owner = owner != null ? owner.trim() : null;
            this.name = name != null ? name.trim() : null;
        }

        boolean isEmpty() {
            return owner == null
                    || name == null || owner.trim().length() == 0
                    || name.trim().length() == 0;
        }
    }
}
