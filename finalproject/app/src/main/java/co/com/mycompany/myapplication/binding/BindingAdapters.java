package co.com.mycompany.myapplication.binding;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showOrHide(View view, boolean show) {
        view.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}