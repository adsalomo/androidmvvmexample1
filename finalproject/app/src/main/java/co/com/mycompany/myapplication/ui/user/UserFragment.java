package co.com.mycompany.myapplication.ui.user;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import co.com.mycompany.myapplication.R;
import co.com.mycompany.myapplication.binding.FragmentDataBindingComponent;
import co.com.mycompany.myapplication.databinding.FragmentUserBinding;
import co.com.mycompany.myapplication.di.Injectable;
import co.com.mycompany.myapplication.model.Repo;
import co.com.mycompany.myapplication.model.User;
import co.com.mycompany.myapplication.repository.Resource;
import co.com.mycompany.myapplication.ui.common.NavigationController;
import co.com.mycompany.myapplication.ui.common.RepoListAdapter;
import co.com.mycompany.myapplication.ui.common.RetryCall;
import co.com.mycompany.myapplication.utils.AutoClearedValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements Injectable {

    private static final String LOGIN_KEY = "login";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserViewModel userViewModel;

    AutoClearedValue<FragmentUserBinding> binding;
    AutoClearedValue<RepoListAdapter> adapter;

    public static UserFragment create(String login) {
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_KEY, login);
        userFragment.setArguments(bundle);
        return userFragment;
    }

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentUserBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_user, container, false, dataBindingComponent);
        dataBinding.setRetryCallback(new RetryCall() {
            @Override
            public void retry() {
                userViewModel.retry();
            }
        });
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
        //return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.setLogin(getArguments().getString(LOGIN_KEY));
        userViewModel.getUser().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                binding.get().setUser(userResource == null ? null : userResource.data);
                binding.get().setUserResource(userResource);
                binding.get().executePendingBindings();
            }
        });
       /* RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent, false, new RepoListAdapter.RepoClickCallback() {
            @Override
            public void onClick(Repo repo) {
                navigationController.navigateToRepo(repo.owner.login, repo.name);
            }
        });
        binding.get().repoList.setAdapter(rvAdapter);
        this.adapter = new AutoClearedValue<>(this, rvAdapter);
        initRepoList();*/
    }

    private void initRepoList() {
        /*userViewModel.getRespositories().observe(this, new Observer<Resource<List<Repo>>>() {
            @Override
            public void onChanged(Resource<List<Repo>> repos) {
                if (repos == null) {
                    adapter.get().replace(null);
                } else {
                    adapter.get().replace(repos.data);
                }
            }
        });*/
    }
}
