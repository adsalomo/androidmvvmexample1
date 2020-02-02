package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.ui.DataBindingActivity;
import com.example.myapplication.ui.DataBindingLiveDataActivity;
import com.example.myapplication.ui.LiveDataActivity;
import com.example.myapplication.ui.UserViewModelActivity;
import com.example.myapplication.ui.ViewModelActivity;

public class MainActivity extends AppCompatActivity {

    private Button btViewModel;
    private Button btUserViewModel;
    private Button btLiveData;
    private Button btDataBinding;
    private Button btDataBindingLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();
    }

    private void setUpView() {
        btViewModel = findViewById(R.id.btViewModel);
        btViewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewModelActivity.class));
            }
        });

        btUserViewModel = findViewById(R.id.btUserViewModel);
        btUserViewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserViewModelActivity.class));
            }
        });

        btLiveData = findViewById(R.id.btLiveData);
        btLiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LiveDataActivity.class));
            }
        });

        btDataBinding = findViewById(R.id.btDataBinding);
        btDataBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DataBindingActivity.class));
            }
        });

        btDataBindingLiveData = findViewById(R.id.btDataBindingLiveData);
        btDataBindingLiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DataBindingLiveDataActivity.class));
            }
        });
    }
}
