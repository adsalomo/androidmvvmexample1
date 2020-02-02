package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDataBindingBinding;
import com.example.myapplication.util.User;

public class DataBindingActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_data_binding);
        ActivityDataBindingBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        user = new User("Adrian", "20");
        binding.setUser(user);
    }
}
