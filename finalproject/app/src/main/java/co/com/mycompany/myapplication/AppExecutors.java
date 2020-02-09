package co.com.mycompany.myapplication;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutors {

    // Procesos 3
    private final Executor diskIO;
    private final Executor netWorkIO;
    private final Executor mainThread;

    public AppExecutors(Executor diskIO, Executor netWorkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.netWorkIO = netWorkIO;
        this.mainThread = mainThread;
    }

    @Inject
    public AppExecutors() {
        // Numero de hilos 3, si llega un 4 proceso, este queda en cola
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
    }

    public Executor getDiskIO() {
        return this.diskIO;
    }

    public Executor getNetWorkIO() {
        return this.netWorkIO;
    }

    public Executor getMainThread() {
        return this.mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler handler = new Handler(Looper.myLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
