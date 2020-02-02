package com.example.myapplication.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.util.User;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private List<User> userList;

    public UserViewModel() {
        this.userList = new ArrayList<>();
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    public List<User> getUserList() {
        return this.userList;
    }


}
