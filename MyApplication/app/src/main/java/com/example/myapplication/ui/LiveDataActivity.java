package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.util.User;
import com.example.myapplication.viewmodel.LiveDataViewModel;

import java.util.List;

public class LiveDataActivity extends AppCompatActivity {

    private TextView tvLiveData;
    private Button btSave;
    private int numero = 0;
    private LiveDataViewModel liveDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);
        setUpView();
    }

    private void setUpView() {
        liveDataViewModel = ViewModelProviders.of(this).get(LiveDataViewModel.class);
        tvLiveData = findViewById(R.id.tvLiveData);
        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numero == 0) {
                    User user = new User();
                    user.setNombre("Adrian");
                    user.setEdad("30");
                    Log.d("TAG1", "numero0");
                    liveDataViewModel.addUser(user);
                }
                if (numero == 1) {
                    User user = new User();
                    user.setNombre("Maria");
                    user.setEdad("32");
                    Log.d("TAG1", "numero1");
                    liveDataViewModel.addUser(user);
                }
                if (numero == 2) {
                    User user = new User();
                    user.setNombre("Manuel");
                    user.setEdad("35");
                    Log.d("TAG1", "numero2");
                    liveDataViewModel.addUser(user);
                }
                numero++;
            }
        });

        final Observer<List<User>> listObserver = new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                String texto = "";
                for (int i = 0; i < users.size(); i++) {
                    texto += users.get(i).getNombre() + " " + users.get(i).getEdad() + "\n";
                }
                tvLiveData.setText(texto);
            }
        };

        liveDataViewModel.getUserList().observe(this, listObserver);
    }
}
