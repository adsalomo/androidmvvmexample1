package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.util.Sumar;
import com.example.myapplication.viewmodel.SumarViewModel;

public class ViewModelActivity extends AppCompatActivity {

    private TextView tvSumar;
    private TextView tvSumarViewModel;
    private Button btSumar;
    private int resultado;
    private SumarViewModel sumarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);
        Log.d("TAG1", "onCreate");
        setUpView();
    }

    private void setUpView() {
        sumarViewModel = ViewModelProviders.of(this).get(SumarViewModel.class);

        tvSumar = findViewById(R.id.tvSumar);
        tvSumarViewModel = findViewById(R.id.tvSumarViewModel);
        btSumar = findViewById(R.id.btSumar);
        btSumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultado = Sumar.sumar(resultado);
                tvSumar.setText("" + resultado);

                sumarViewModel.setResultado(Sumar.sumar(sumarViewModel.getResultado()));
                tvSumarViewModel.setText(("" + sumarViewModel.getResultado()));
            }
        });
    }

    // Notifica observer
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG1", "onStart");
    }

    // Notifica observer
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG1", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG1", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG1", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG1", "onDestroy");
    }
}
