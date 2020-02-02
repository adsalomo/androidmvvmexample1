package com.example.myapplication.viewmodel;

import androidx.lifecycle.ViewModel;

public class SumarViewModel extends ViewModel {
    private int resultado;

    public int getResultado() {
        return this.resultado;
    }

    public void  setResultado(int resultado) {
        this.resultado = resultado;
    }
}
