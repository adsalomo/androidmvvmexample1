package co.com.mycompany.myapplication.di;

import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

@Documented
@Target({ElementType.METHOD}) // Que se aplica en metodos
@Retention(RetentionPolicy.RUNTIME) // En tiempo de ejecucion
@MapKey
public @interface ViewModelKey {

    Class<? extends ViewModel> value();

}
