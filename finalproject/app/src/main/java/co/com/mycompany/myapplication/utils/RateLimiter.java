package co.com.mycompany.myapplication.utils;

import android.os.SystemClock;

import androidx.collection.ArrayMap;

import java.util.concurrent.TimeUnit;

public class RateLimiter<KEY> {
    private ArrayMap<KEY, Long> timeStamps = new ArrayMap<>();
    private final long timeout;

    public RateLimiter(int timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(timeout);
    }

    public synchronized boolean shouldFetch(KEY key) {
        Long lasFetched = timeStamps.get(key);
        long now = this.now();
        if (lasFetched == null) {
            timeStamps.put(key, now);
            return true;
        }

        if (now - lasFetched > timeout) {
            timeStamps.put(key, now);
        }

        return false;
    }

    private long now() {
        return SystemClock.uptimeMillis();
    }

    public synchronized void reset(KEY key) {
        timeStamps.remove(key);
    }
}
