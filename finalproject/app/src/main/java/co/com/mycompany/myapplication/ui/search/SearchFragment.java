package co.com.mycompany.myapplication.ui.search;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.com.mycompany.myapplication.R;
import co.com.mycompany.myapplication.di.Injectable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements Injectable {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

}
