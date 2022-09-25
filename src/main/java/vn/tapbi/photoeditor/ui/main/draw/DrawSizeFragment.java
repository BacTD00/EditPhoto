package vn.tapbi.photoeditor.ui.main.draw;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatImageView;

import android.view.View;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.databinding.FragmentDrawSizeBinding;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;
import vn.tapbi.photoeditor.ui.main.MainViewModel;

public class DrawSizeFragment extends BaseBindingFragment<FragmentDrawSizeBinding, MainViewModel> {
    public static DrawSizeFragment newInstance() {
        return new DrawSizeFragment();
    }

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_draw_size;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.tools.tvWidgetName.setText(requireContext().getString(R.string.brush_size));
        binding.tools.icRedo.setVisibility(View.GONE);
        binding.tools.icUndo.setVisibility(View.GONE);

        mainViewModel.backSizeBrush.observe(this, aBoolean -> {
            resetImage();
            binding.ivBrushSize1.setImageResource(R.drawable.draw_size_8_selected);
        });

        selectBrushSize();
        brushSizeEvent();

//        onBackPressEvent();
    }

    private void onBackPressEvent() {
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainViewModel.setIsClickSizeDraw(false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void selectSize(int size, View view, int image) {
        view.setOnClickListener(v -> {
            resetImage();
            if (view instanceof AppCompatImageView)
                ((AppCompatImageView) view).setImageResource(image);
            mainViewModel.setBrushSize(size);
        });
    }

    private void selectBrushSize() {
        selectSize(8, binding.ivBrushSize1, R.drawable.draw_size_8_selected);
        selectSize(15, binding.ivBrushSize2, R.drawable.draw_size_15_selected);
        selectSize(21, binding.ivBrushSize3, R.drawable.draw_size_21_selected);
        selectSize(25, binding.ivBrushSize4, R.drawable.draw_size_25_selected);
        selectSize(33, binding.ivBrushSize5, R.drawable.draw_size_33_selected);
        selectSize(40, binding.ivBrushSize6, R.drawable.draw_size_40_selected);
        selectSize(46, binding.ivBrushSize7, R.drawable.draw_size_46_selected);
    }

    private void resetImage() {
        binding.ivBrushSize1.setImageResource(R.drawable.draw_size_8);
        binding.ivBrushSize2.setImageResource(R.drawable.draw_size_15);
        binding.ivBrushSize3.setImageResource(R.drawable.draw_size_21);
        binding.ivBrushSize4.setImageResource(R.drawable.draw_size_25);
        binding.ivBrushSize5.setImageResource(R.drawable.draw_size_33);
        binding.ivBrushSize6.setImageResource(R.drawable.draw_size_40);
        binding.ivBrushSize7.setImageResource(R.drawable.draw_size_46);
    }

    private void brushSizeEvent() {
        binding.tools.icRemoveAll.setOnClickListener(v -> {
            mainViewModel.setIsClickSizeDraw(false);
            mainViewModel.setBrushSize(8);
            resetImage();
            binding.ivBrushSize1.setImageResource(R.drawable.draw_size_8_selected);
        });

        binding.tools.icDone.setOnClickListener(v -> mainViewModel.setIsClickSizeDraw(false));
    }

    @Override
    protected void onPermissionGranted() {
    }
}