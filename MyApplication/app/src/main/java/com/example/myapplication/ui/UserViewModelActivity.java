package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.util.User;
import com.example.myapplication.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserViewModelActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etEdad;
    private Button btSalvar;
    private Button btVer;
    private TextView tvUser;
    private TextView tvUserViewModel;
    private List<User> userList;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_model);
        setUpView();
    }

    private void setUpView() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userList = new ArrayList<>();

        tvUser = findViewById(R.id.tvUser);
        tvUserViewModel = findViewById(R.id.tvUserViewModel);

        etNombre = findViewById(R.id.etNombre);
        etEdad = findViewById(R.id.etEdad);

        btSalvar = findViewById(R.id.btSalvar);
        btSalvar .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setEdad(etEdad.getText().toString());
                user.setNombre(etNombre.getText().toString());
                userList.add(user);
                // View Model
                userViewModel.addUser(user);
            }
        });

        btVer = findViewById(R.id.btVer);
        btVer .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = "";
                for (int i = 0; i < userList.size(); i++) {
                    texto += userList.get(i).getNombre() + " " + userList.get(i).getEdad() + "\n";
                }
                tvUser.setText(texto);

                // View Model
                texto = "";
                for (int i = 0; i < userViewModel.getUserList().size(); i++) {
                    texto +=  userViewModel.getUserList().get(i).getNombre()
                            + " " +  userViewModel.getUserList().get(i).getEdad() + "\n";
                }
                tvUserViewModel.setText(texto);
            }
        });
    }
}
