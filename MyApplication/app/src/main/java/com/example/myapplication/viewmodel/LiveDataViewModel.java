package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.util.User;

import java.util.ArrayList;
import java.util.List;

// Clase observable dataholder -> dataholder 0 comportamiento, pero mucha data
public class LiveDataViewModel extends ViewModel {
    // data holder
    // Lectura y escritura
    private MutableLiveData<List<User>> userListLiveData;
    private List<User> userList;

    public LiveData<List<User>> getUserList() {
        if (userListLiveData == null) {
            userListLiveData = new MutableLiveData<>();
            userList = new ArrayList<>();
        }
        return userListLiveData;
    }

    public void addUser(User user) {
        userList.add(user);
        userListLiveData.setValue(userList); // Se llama en el hilo principal de la app
        //userListLiveData.postValue(userList); // Si esta en segundo plano
    }
}
