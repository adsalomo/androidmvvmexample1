package co.com.mycompany.myapplication.ui.repo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import co.com.mycompany.myapplication.R;
import co.com.mycompany.myapplication.databinding.ContributorItemBinding;
import co.com.mycompany.myapplication.model.Contributor;
import co.com.mycompany.myapplication.ui.common.DataBoundListAdapter;

public class ContributorAdapter extends DataBoundListAdapter<Contributor, ContributorItemBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ContributorClickCallBack callBack;

    public ContributorAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                              ContributorClickCallBack callBack) {
        this.dataBindingComponent = dataBindingComponent;
        this.callBack = callBack;
    }

    public interface ContributorClickCallBack {
        void onClick(Contributor contributor);
    }

    @Override
    protected ContributorItemBinding createBinding(ViewGroup viewGroup) {
        ContributorItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.contributor_item, viewGroup, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contributor contributor = binding.getContributor();
                if (contributor != null && callBack != null) {
                    callBack.onClick(contributor);
                }
            }
        });
        return binding;
    }

    @Override
    protected void bind(ContributorItemBinding binding, Contributor item) {
        binding.setContributor(item);
    }

    @Override
    protected boolean areItemsTheSame(Contributor oldItem, Contributor newItem) {
        return oldItem.getLogin().equals(newItem.getLogin());
    }

    @Override
    protected boolean areContentsTheSame(Contributor oldItem, Contributor newItem) {
        return oldItem.getAvatarUrl().equals(newItem.getAvatarUrl())
                && oldItem.getContributions() == newItem.getContributions();
    }
}
