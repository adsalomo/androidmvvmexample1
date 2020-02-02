package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.util.User;

public class DataBindingLiveDataViewModel extends ViewModel {
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Boolean> visible;
    private MutableLiveData<Float> size;

    public DataBindingLiveDataViewModel() {
        this.userMutableLiveData = new MutableLiveData<>();
        this.visible = new MutableLiveData<>();
        this.visible.setValue(true);
        this.size = new MutableLiveData<>();
        this.size.setValue(14f);
    }


    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User user) {
        this.userMutableLiveData.setValue(user);
    }

    public void updateUser() {
        User user = new User("Adrian", "30");
        this.userMutableLiveData.setValue(user);
    }

    public LiveData<Boolean> getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible.setValue(visible);
    }

    public void changeVisibility() {
        if (this.visible.getValue().booleanValue()) {
            this.visible.setValue(false);
        } else {
            this.visible.setValue(true);
        }
        size.setValue(size.getValue().floatValue() + 5l);
    }

    public LiveData<Float> getSize() {
        return this.size;
    }
}
