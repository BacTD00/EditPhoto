package vn.tapbi.photoeditor.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import vn.tapbi.photoeditor.ui.main.MainViewModel;

public abstract class BaseBindingFragment<B extends ViewDataBinding, T extends BaseViewModel>  extends BaseFragment {
    public B binding;
    public T viewModel;
    public MainViewModel mainViewModel;

    protected abstract Class<T> getViewModel();
    public abstract int getLayoutId();
    protected abstract void onCreatedView(View view, Bundle savedInstanceState);
    protected abstract void onPermissionGranted();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(getViewModel());
        mainViewModel= new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        onCreatedView(view,savedInstanceState);
    }
}
