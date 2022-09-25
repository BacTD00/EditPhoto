package vn.tapbi.photoeditor.ui.main.filter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.View;

import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;
import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.databinding.FilterFragmentBinding;
import vn.tapbi.photoeditor.ui.adapter.FilterAdapter;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;
import vn.tapbi.photoeditor.ui.main.MainActivity;
import vn.tapbi.photoeditor.utils.FilterOption;

public class FilterFragment extends BaseBindingFragment<FilterFragmentBinding, FilterViewModel> implements FilterAdapter.OnClickFilter {
    private static SharedPreferences sharedPreferences;
    private final List<MagicFilterType> magicFilterTypes = new ArrayList<>();
    protected int position = 0;
    private FilterAdapter filterAdapter;
    private String uri;

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Override
    protected Class<FilterViewModel> getViewModel() {
        return FilterViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.filter_fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Constant.STATE_POSITION_ID_FILTER_ADAPTER, position);
        outState.putString(Constant.STATE_URI_FILTER_ADAPTER, uri);

        sharedPreferences.edit().putString(Constant.SAVE_URI_FILTER, uri).apply();
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {

        setupViews();

        confirmFilterEvent();

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(Constant.STATE_POSITION_ID_FILTER_ADAPTER);
            uri = savedInstanceState.getString(Constant.STATE_URI_FILTER_ADAPTER);
            if (filterAdapter != null) {
                filterAdapter.setId(position);
            }
        }
    }

    private void confirmFilterEvent() {
        // Reset filter
        binding.tools.icRemoveAll.setOnClickListener(view -> {
            mainViewModel.setIsClickItemFilter(false);
            mainViewModel.gpuImageFilterLiveEvent.postValue(new GPUImageFilter());
            if (filterAdapter != null) {
                filterAdapter.setId(0);
            }
        });

        // confirm filter
        binding.tools.icDone.setOnClickListener(view1 -> {
            mainViewModel.setIsClickItemFilter(false);
        });
    }

    private void setupViews() {
        sharedPreferences = requireActivity().getSharedPreferences(Constant.MY_PREF, Context.MODE_PRIVATE);

        binding.tools.tvWidgetName.setText(R.string.filter);
        binding.tools.icRedo.setVisibility(View.GONE);
        binding.tools.icUndo.setVisibility(View.GONE);

        mainViewModel.backFilter.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) filterAdapter.setId(0);
        });

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        uri = mainActivity.getUri();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addFilter(Bitmap bitmap) {
        viewModel.getFilter(requireContext(), bitmap);

        viewModel.filterOptionLiveEvent.observe(this, filterOption -> {
            if (filterOption != null) {
                if (filterOption instanceof FilterOption) {
                    magicFilterTypes.clear();
                    magicFilterTypes.addAll(((FilterOption) filterOption).getListFilter());

                    filterAdapter.setMagicFilterTypeList(magicFilterTypes); // list
                    filterAdapter.setListBitmapFilter(((FilterOption) filterOption).getListBmFilter()); // image

                    Timber.d("addFilter: %s", magicFilterTypes.size());
                }
            }
        });
        filterAdapter = new FilterAdapter(this, requireContext());

        binding.rcvItem.post(() -> {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rcvItem.setLayoutManager(linearLayoutManager);
            binding.rcvItem.setAdapter(filterAdapter);
            filterAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onPermissionGranted() {

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.edit().clear();
    }

    @Override
    public void onChangeFilter(GPUImageFilter gpuImageFilter, int position) {
        mainViewModel.gpuImageFilterLiveEvent.postValue(gpuImageFilter);
        this.position = position;
    }
}