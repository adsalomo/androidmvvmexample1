package co.com.mycompany.myapplication.ui.common;

import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;

import co.com.mycompany.myapplication.MainActivity;
import co.com.mycompany.myapplication.R;
import co.com.mycompany.myapplication.ui.repo.RepoFragment;
import co.com.mycompany.myapplication.ui.search.SearchFragment;
import co.com.mycompany.myapplication.ui.user.UserFragment;

/**
 * Permite manejar la navegacion del main activity
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentActivity;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentActivity = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentActivity.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToRepo(String owner, String name) {
        RepoFragment fragment = RepoFragment.create(owner, name);
        String tag = "repo" + "/" + "/" + name;
        fragmentActivity.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToUser(String login) {
        String tag = "user" + "/" + login;
        UserFragment fragment = UserFragment.create(login);
        fragmentActivity.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

}
