package vn.tapbi.photoeditor.ui.main.draw;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.common.models.ColorData;
import vn.tapbi.photoeditor.databinding.FragmentDrawColorBinding;
import vn.tapbi.photoeditor.ui.adapter.ColorAdapter;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;
import vn.tapbi.photoeditor.ui.dialog.ColorPickerDialog;

public class DrawColorFragment extends BaseBindingFragment<FragmentDrawColorBinding, DrawViewModel> implements ColorAdapter.OnClickColorImage, ColorPickerDialog.OnColorChangedListener {
    private ColorAdapter colorAdapter;
    private int stateIdColor;
    private String stateColor;

    private int colorCustom;

    public static DrawColorFragment newInstance() {
        return new DrawColorFragment();
    }

    @Override
    protected Class<DrawViewModel> getViewModel() {
        return DrawViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_draw_color;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.tools.tvWidgetName.setText(requireContext().getString(R.string.brush_color));
        binding.tools.icUndo.setVisibility(View.GONE);
        binding.tools.icRedo.setVisibility(View.GONE);

        addListColor();
        drawColorEvent();

        chooseListColor();

        mainViewModel.backColorBrush.observe(this, aBoolean -> {
            if (aBoolean) colorAdapter.setId(1);
        });

        if (savedInstanceState != null) {
            stateColor = savedInstanceState.getString(Constant.STATE_COLOR_DRAW);
            stateIdColor = savedInstanceState.getInt(Constant.STATE_ID_COLOR_DRAW);

            colorCustom = savedInstanceState.getInt(Constant.STATE_CHOOSE_COLOR_DRAW);

//            colorAdapter.setId(stateIdColor);
//            mainViewModel.setColorBrush(new ColorData(stateColor, stateIdColor));

            if (colorAdapter != null) {
                if (colorCustom == 0) {
                    colorAdapter.setId(stateIdColor);
                    mainViewModel.setColorBrush(new ColorData(stateColor, stateIdColor));
                } else {
                    colorAdapter.setId(-1);
                    mainViewModel.setChooseColorDraw(colorCustom);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constant.STATE_ID_COLOR_DRAW, stateIdColor);
        outState.putString(Constant.STATE_COLOR_DRAW, stateColor);

        outState.putInt(Constant.STATE_CHOOSE_COLOR_DRAW, colorCustom);

        super.onSaveInstanceState(outState);
    }

    private void chooseListColor() {
        binding.ivChooseColor.setOnClickListener(v ->
                new ColorPickerDialog(requireContext(), DrawColorFragment.this::colorChanged, ContextCompat.getColor(requireContext(), R.color.white)).show());
    }

    private void addListColor() {
        viewModel.getListColorDraw(Constant.TYPE_COLOR);
        viewModel.listColor.observe(this, colorTypes -> {
            if (colorTypes != null) {
                colorAdapter = new ColorAdapter(this, getContext());
                colorAdapter.setListColor(colorTypes);
                colorAdapter.setId(1);

                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);

                binding.rcvDrawColor.setLayoutManager(layoutManager);
                binding.rcvDrawColor.setAdapter(colorAdapter);
            }
        });
    }

    private void drawColorEvent() {
        binding.tools.icRemoveAll.setOnClickListener(v -> {
            mainViewModel.setColorBrush(new ColorData(null, -1));
            colorAdapter.setId(1);
            mainViewModel.setIsClickColorDraw(false);
        });

        binding.tools.icDone.setOnClickListener(v -> mainViewModel.setIsClickColorDraw(false));
    }

    @Override
    protected void onPermissionGranted() {
    }

    @Override
    public void onClickColor(int position, String color) {
        mainViewModel.setColorBrush(new ColorData(color, position));
        stateColor = color;
        stateIdColor = position;
        colorCustom = 0;
    }

    @Override
    public void colorChanged(int color) {
        mainViewModel.setChooseColorDraw(color);
        colorAdapter.setId(-1);
        colorCustom = color;
        stateIdColor = -1;
        stateColor = null;
    }
}