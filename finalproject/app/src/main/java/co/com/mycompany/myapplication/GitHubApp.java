package co.com.mycompany.myapplication;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import co.com.mycompany.myapplication.di.AppInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Solamente puede existir una, y esta se declara en AndroidManifest.xml
 */
public class GitHubApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
