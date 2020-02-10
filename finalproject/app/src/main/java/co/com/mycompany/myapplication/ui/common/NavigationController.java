package co.com.mycompany.myapplication.ui.common;

import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;

import co.com.mycompany.myapplication.MainActivity;
import co.com.mycompany.myapplication.R;

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
}
