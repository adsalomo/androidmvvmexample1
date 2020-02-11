package co.com.mycompany.myapplication.ui.search;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import co.com.mycompany.myapplication.R;
import co.com.mycompany.myapplication.binding.FragmentDataBindingComponent;
import co.com.mycompany.myapplication.databinding.FragmentSearchBinding;
import co.com.mycompany.myapplication.di.Injectable;
import co.com.mycompany.myapplication.model.Repo;
import co.com.mycompany.myapplication.repository.Resource;
import co.com.mycompany.myapplication.ui.common.NavigationController;
import co.com.mycompany.myapplication.ui.common.RepoListAdapter;
import co.com.mycompany.myapplication.ui.common.RetryCall;
import co.com.mycompany.myapplication.utils.AutoClearedValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelProvider;
    @Inject
    NavigationController navigationController;
    // Se genera en tiempo de compilacion y contiene todos los getters para
    // todos binding adapters de las instancias utilizadas (los imports dentro del xml)
    androidx.databinding.DataBindingComponent dataBindingComponent =
            new FragmentDataBindingComponent(this);
    // Limpia las referencias una vez destruida
    AutoClearedValue<FragmentSearchBinding> binding;
    AutoClearedValue<RepoListAdapter> adapter;
    private SearchViewModel searchViewModel;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_search, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = ViewModelProviders.of(this, viewModelProvider).get(SearchViewModel.class);

        initRecyclerView();

        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent,
                true, new RepoListAdapter.RepoClickCallback() {
            @Override
            public void onClick(Repo repo) {
                navigationController.navigateToRepo(repo.owner.login, repo.name);
            }
        });
        binding.get().repoList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        initSearchInputListener();
        binding.get().setCallback(new RetryCall() {
            @Override
            public void retry() {
                searchViewModel.refresh();
            }
        });
    }

    private void initSearchInputListener() {
        binding.get().input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchFragment.this.doSearch(v);
                    return true;
                }
                return false;
            }
        });
        binding.get().input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    doSearch(v);
                    return true;
                }
                return false;
            }
        });
    }

    private void doSearch(View v) {
        String query = binding.get().input.getText().toString();
        dissmissKeyBoard(v.getWindowToken());
        binding.get().setQuery(query);
    }

    private void initRecyclerView() {
        binding.get().repoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lasPosition = layoutManager.findLastVisibleItemPosition();
                if (lasPosition == adapter.get().getItemCount() - 1) {
                    searchViewModel.loadNextPage();
                }
            }
        });

        searchViewModel.getResults().observe(this, new Observer<Resource<List<Repo>>>() {
            @Override
            public void onChanged(Resource<List<Repo>> listResource) {
                binding.get().setSearchResource(listResource);
                binding.get().setResultCount(listResource == null
                        || listResource.data == null ? 0 : listResource.data.size());
                adapter.get().replace(listResource == null ? null : listResource.data);
                binding.get().executePendingBindings();
            }
        });

        searchViewModel.getLoadMoreStatus().observe(this, new Observer<SearchViewModel.LoadMoreState>() {
            @Override
            public void onChanged(SearchViewModel.LoadMoreState loadMoreState) {
                if (loadMoreState == null) {
                    binding.get().setLoadingMore(false);
                } else {
                    binding.get().setLoadingMore(loadMoreState.isRunning());
                    String error = loadMoreState.getErrorMessageIfNoHandle();
                    if (error != null) {
                        Log.d("TAG1", "Error on LoadMore");
                    }
                }
                binding.get().executePendingBindings();
            }
        });
    }

    private void dissmissKeyBoard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }
}
