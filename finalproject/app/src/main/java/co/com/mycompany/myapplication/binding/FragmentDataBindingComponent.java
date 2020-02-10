package co.com.mycompany.myapplication.binding;


import androidx.fragment.app.Fragment;

public class FragmentDataBindingComponent implements androidx.databinding.DataBindingComponent {
    private  final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    public FragmentBindingAdapters getAdapter() {
        return this.adapter;
    }
}
