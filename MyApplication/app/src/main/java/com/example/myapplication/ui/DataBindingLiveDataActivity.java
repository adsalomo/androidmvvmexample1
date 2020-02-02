package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDataBindingLiveDataBinding;
import com.example.myapplication.util.User;
import com.example.myapplication.viewmodel.DataBindingLiveDataViewModel;

public class DataBindingLiveDataActivity extends AppCompatActivity {

    private DataBindingLiveDataViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_data_binding_live_data);
        ActivityDataBindingLiveDataBinding
                binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_live_data);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(DataBindingLiveDataViewModel.class);
        binding.setViewModel(viewModel);

        User user = new User("Millito", "35");
        viewModel.setUserMutableLiveData(user);
    }
}
