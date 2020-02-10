package co.com.mycompany.myapplication.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import co.com.mycompany.myapplication.R;
import co.com.mycompany.myapplication.databinding.RepoItemBinding;
import co.com.mycompany.myapplication.model.Repo;

public class RepoListAdapter extends DataBoundListAdapter<Repo, RepoItemBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final RepoClickCallback repoClickCallback;
    private final boolean showFullName;

    public RepoListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                           boolean showFullName, RepoClickCallback repoClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.repoClickCallback = repoClickCallback;
        this.showFullName = showFullName;
    }

    @Override
    protected RepoItemBinding createBinding(ViewGroup parent) {
        RepoItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.repo_item, parent, false, dataBindingComponent);
        binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repo repo = binding.getRepo();
                if (repo != null && repoClickCallback != null) {
                    repoClickCallback.onClick(repo);
                }
            }
        });
        return binding;
    }

    @Override
    protected void bind(RepoItemBinding binding, Repo item) {
        binding.setRepo(item);
    }

    @Override
    protected boolean areItemsTheSame(Repo oldItem, Repo newItem) {
        return oldItem.owner.equals(newItem.owner)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(Repo oldItem, Repo newItem) {
        return oldItem.description.equals(newItem.description)
                && oldItem.starts == newItem.starts;
    }

    public interface RepoClickCallback {
        void onClick(Repo repo);
    }
}
