package vn.tapbi.photoeditor.ui.main.edit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.filter.base.GPUImageFilter;

import java.util.ArrayList;
import java.util.List;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.databinding.EditFragmentBinding;
import vn.tapbi.photoeditor.feature.drawable.DrawingView;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;
import vn.tapbi.photoeditor.ui.main.draw.DrawFragment;
import vn.tapbi.photoeditor.ui.main.filter.FilterFragment;
import vn.tapbi.photoeditor.ui.main.share.ShareFragment;
import vn.tapbi.photoeditor.ui.main.sticker.StickerFragment;
import vn.tapbi.photoeditor.ui.main.text.TextFragment;
import vn.tapbi.photoeditor.utils.PhotoUtils;

public class EditFragment extends BaseBindingFragment<EditFragmentBinding, EditViewModel> {
    // common
    private final List<Fragment> fragments = new ArrayList<>();
    private final int filterFragmentIndex = 0;
    private final int drawFragmentIndex = 1;
    private final int stickerFragmentIndex = 2;
    private final int textFragmentIndex = 3;
    public int countUndo1 = 0;
    // filter
    private GPUImageFilter gpuImageFilter;
    private FilterFragment filterFragment;
    private boolean checkFilterShow;
    // draw
    private DrawFragment drawFragment;
    private Boolean checkDrawShow;
    private Boolean checkDrawColorShow;
    private Boolean checkDrawSizeShow;
    // sticker
    private StickerFragment stickerFragment;
    private Boolean checkStickerShow;
    // text
    private TextFragment textFragment;
    private Boolean checkTextShow;
    // share
    private Boolean checkShareShow;
    // width and height
    private int wV, hV;
    private int wTemp = 0;
    private int hTemp = 0;

    @Override
    protected Class<EditViewModel> getViewModel() {
        return EditViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_fragment;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        setupView();

        showLoadingDialog();

        // filter
        getImageFilter();
        eventClick();
        eventViewClicked();

        // draw
        setBrushColor();
        setBrushSize();

        // undo, redo
//        drawOption();

        // save image
        onSaveClicked();

        if (savedInstanceState != null) {
            // filter
            checkFilterShow = savedInstanceState.getBoolean(Constant.SHOW_FILTER);
            mainViewModel.setIsClickItemFilter(checkFilterShow);

            // draw
            checkDrawShow = savedInstanceState.getBoolean(Constant.SHOW_DRAW);
            mainViewModel.setIsClickItemDraw(checkDrawShow);

            // sticker
            checkStickerShow = savedInstanceState.getBoolean(Constant.SHOW_STICKER);
            mainViewModel.setIsClickItemSticker(checkStickerShow);

            // text
            checkTextShow = savedInstanceState.getBoolean(Constant.SHOW_TEXT);
            mainViewModel.setIsClickItemText(checkTextShow);

            // share
            checkShareShow = savedInstanceState.getBoolean(Constant.CHECK_SHARE);
            mainViewModel.setIsClickShareButton(checkShareShow);
        }

        eventBack();

        binding.toolbarEdit.imgIconBack.setOnClickListener(view1 -> showExitDialog());
    }

    private void showLoadingDialog() {
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(com.yalantis.ucrop.R.layout.custom_loading_dialog, null);

        final Dialog loadingDialog = new Dialog(requireContext(), com.yalantis.ucrop.R.style.MaterialDialogSheet);
        loadingDialog.setContentView(v);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setGravity(Gravity.CENTER);

        loadingDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(loadingDialog::dismiss, 1200);
    }

//    private void drawOption() {
//        // undo draw
//        mainViewModel.isUndoDraw.observe(getViewLifecycleOwner(), isUndo -> {
//            if (isUndo) binding.imgToDraw.onClickUndo();
//        });
//
//        // redo draw
//        mainViewModel.isRedoDraw.observe(getViewLifecycleOwner(), isRedo -> {
//            if (isRedo) binding.imgToDraw.onClickRedo();
//        });
//
//        // delete draw
//        mainViewModel.isDeleteDraw.observe(getViewLifecycleOwner(), isDelete -> {
//            if (isDelete) binding.imgToDraw.clearAll();
//        });
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(Constant.SHOW_FILTER, checkFilterShow); // filter

        outState.putBoolean(Constant.SHOW_DRAW, checkDrawShow); // draw

        outState.putBoolean(Constant.SHOW_STICKER, checkStickerShow); // sticker

        outState.putBoolean(Constant.SHOW_TEXT, checkTextShow); // text

        outState.putBoolean(Constant.CHECK_SHARE, checkShareShow);

        super.onSaveInstanceState(outState);
    }

    private void setupView() {
        checkViewShow();

        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.VISIBLE);

        // filter
        filterFragment = FilterFragment.newInstance();
        fragments.add(filterFragment);

        // draw
        drawFragment = DrawFragment.newInstance();
        fragments.add(drawFragment);

        // sticker
        stickerFragment = StickerFragment.newInstance();
        fragments.add(stickerFragment);

        // text
        textFragment = TextFragment.newInstance();
        fragments.add(textFragment);

        addFragment();

        setImage();
    }

    private void eventBack() {
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (!checkTextShow && !checkDrawShow && !checkStickerShow && !checkFilterShow && !checkDrawColorShow && !checkDrawSizeShow && !checkShareShow) {
                    showExitDialog();
                    return true;
                }

                if (checkFilterShow) {
                    mainViewModel.setIsClickItemFilter(false);
                    checkFilterShow = false;
                    return true;
                }

                if (checkStickerShow) {
                    binding.imgEdit.setInEdit(false);
                    mainViewModel.setIsClickItemSticker(false);
                    return true;
                }

                if (checkDrawShow && !checkDrawColorShow && !checkDrawSizeShow) {
                    mainViewModel.setIsClickItemDraw(false);
                    checkDrawShow = false;
                    return true;
                }

                if (checkDrawColorShow) {
                    mainViewModel.setIsClickColorDraw(false);
                    return true;
                }
                if (checkDrawSizeShow) {
                    mainViewModel.setIsClickSizeDraw(false);
                    return true;
                }
                if (checkShareShow) {
                    mainViewModel.setIsClickShareButton(false);
                    return true;
                }
            }
            return false;
        });
    }

    private void setImage() {
        assert getArguments() != null;
        String uri = getArguments().getString(Constant.REQUEST_URI_FROM_MAIN_ACTIVITY);
        if (uri != null) {
            Glide.with(requireContext()).asBitmap().load(uri).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    setImageSize(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
    }

    private void setImageSize(Bitmap bitmap) {
        filterFragment.addFilter(bitmap);
        binding.rlDetail.post(() -> {
            wV = binding.rlDetail.getWidth();
            hV = binding.rlDetail.getHeight();
            setParam(bitmap);
            wTemp = wV;
            hTemp = hV;
            binding.rlContent.setVisibility(View.VISIBLE);
        });
    }

    private void setParam(Bitmap bitmap) {
        Bitmap bitmap1 = PhotoUtils.resizeBitmap(bitmap, getContext());
        int hB = bitmap1.getHeight();
        int wB = bitmap1.getWidth();
        int s = wV * hB - wB * hV;
        int wRl;
        int hRl;
        if (s > 0) {
            wRl = hV * wB / hB;
            hRl = hV;
        } else {
            hRl = wV * hB / wB;
            wRl = wV;
        }

        wTemp = wRl;
        hTemp = hRl;

        binding.imgToFilter.setImage(bitmap1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(wTemp, hTemp);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        binding.rlContent.setLayoutParams(layoutParams);
    }

    private void getImageFilter() {
        mainViewModel.gpuImageFilterLiveEvent.observe(this, imageFilter -> {
            if (imageFilter instanceof GPUImageFilter) {
                binding.imgToFilter.setFilter((GPUImageFilter) imageFilter);
                this.gpuImageFilter = (GPUImageFilter) imageFilter;
            }
        });
    }

    private void showLayout() {
        binding.fragmentEditContainer.setVisibility(View.VISIBLE);
        binding.toolbarEdit.toolbar.setVisibility(View.GONE);
        binding.widgetWrapper.setVisibility(View.GONE);
    }

    private void hideLayout() {
        binding.fragmentEditContainer.setVisibility(View.GONE);
        binding.toolbarEdit.toolbar.setVisibility(View.VISIBLE);
        binding.widgetWrapper.setVisibility(View.VISIBLE);
    }

    private void eventViewClicked() {
        // filter
        mainViewModel.isClickItemFilter.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showFragment(filterFragmentIndex);
                checkFilterShow = true;
                showLayout();
            } else {
                hideFragment(filterFragmentIndex);
                checkFilterShow = false;
                hideLayout();
            }
        });

        // draw
        mainViewModel.isClickItemDraw.observe(getViewLifecycleOwner(), isClickDraw -> {
            if (isClickDraw) {
                showFragment(drawFragmentIndex);
                checkDrawShow = true;
                binding.imgEdit.setLooked(true);
                binding.imgToDraw.setDraw(true);
                mainViewModel.isClickSizeDraw.setValue(false);
                mainViewModel.isClickColorDraw.setValue(false);
                showLayout();
            } else {
                hideFragment(drawFragmentIndex);
                checkDrawShow = false;
                binding.imgEdit.setLooked(false);
                binding.imgToDraw.setDraw(false);
                hideLayout();
            }
        });

        mainViewModel.isClickColorDraw.observe(this, isColorDraw -> checkDrawColorShow = isColorDraw);

        mainViewModel.isClickSizeDraw.observe(this, isColorSize -> checkDrawSizeShow = isColorSize);

        // sticker
        mainViewModel.isClickItemSticker.observe(getViewLifecycleOwner(), isClickSticker -> {
            if (isClickSticker) {
                showFragment(stickerFragmentIndex);
                checkStickerShow = true;
                binding.imgEdit.setLooked(false);
                showLayout();
            } else {
                hideFragment(stickerFragmentIndex);
                checkStickerShow = false;
                hideLayout();
            }
        });

        // text
        mainViewModel.isClickItemText.observe(getViewLifecycleOwner(), isClickText -> {
            if (isClickText) {
                showFragment(textFragmentIndex);
                checkTextShow = true;
                binding.imgEdit.setLooked(false);
                showLayout();
            } else {
                hideFragment(textFragmentIndex);
                checkTextShow = false;
                hideLayout();
            }
        });

        // share
        mainViewModel.isClickShareButton.observe(getViewLifecycleOwner(), isClickShare -> checkShareShow = isClickShare);
    }

    private void eventClick() {
        // filter
        binding.btnFilter.setOnClickListener(view -> mainViewModel.setIsClickItemFilter(true));

        // draw
        binding.btnDraw.setOnClickListener(v -> {
            new DrawingView(getContext());

            mainViewModel.setIsClickItemDraw(true);
        });

        // sticker
        binding.btnSticker.setOnClickListener(view -> mainViewModel.setIsClickItemSticker(true));

        // text
        binding.btnText.setOnClickListener(view -> mainViewModel.setIsClickItemText(true));

        binding.toolbarEdit.btnSave.setOnClickListener(view -> mainViewModel.setIsClickShareButton(true));
    }

    // draw
    private void setBrushSize() {
        mainViewModel.brushSize.observe(getViewLifecycleOwner(), integer -> {
            if (integer != 0) binding.imgToDraw.setSize(integer);
            else binding.imgToDraw.setSize(10);
        });
    }

    private void setBrushColor() {
        mainViewModel.colorBrush.observe(getViewLifecycleOwner(), colorData -> {
            if (colorData.getColor() != null && colorData.getPosition() != -1) {
                binding.imgToDraw.setColor(android.graphics.Color.parseColor(Constant.COLOR_START_SYMBOL + colorData.getColor()));
            } else binding.imgToDraw.setColor(Color.BLACK);
        });

        mainViewModel.chooseColorDraw.observe(getViewLifecycleOwner(), integer -> {
            if (integer != 0) {
                binding.imgToDraw.setColor(integer);
            } else binding.imgToDraw.setColor(Color.BLACK);
        });
    }

    private void addFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // filter
        fragmentTransaction.add(R.id.fragment_edit_container, filterFragment);

        // draw
        fragmentTransaction.add(R.id.fragment_edit_container, drawFragment);

        // sticker
        fragmentTransaction.add(R.id.fragment_edit_container, stickerFragment);

        // sticker
        fragmentTransaction.add(R.id.fragment_edit_container, textFragment);

        fragmentTransaction.commit();
    }

    private void showFragment(int fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for (int i = 0; i < fragments.size(); i++) {
            if (i == fragment) fragmentTransaction.show(fragments.get(fragment));
            else fragmentTransaction.hide(fragments.get(i));
        }

        fragmentTransaction.commit();
    }

    private void hideFragment(int fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragments.get(fragment).isAdded()) {
            fragmentTransaction.hide(fragments.get(fragment));
            fragmentTransaction.commit();
        }
    }

    private void checkViewShow() {
        // filter
        checkFilterShow = false;

        // draw
        checkDrawShow = false;
        checkDrawSizeShow = false;
        checkDrawColorShow = false;

        // sticker
        checkStickerShow = false;

        // text
        checkTextShow = false;

        // share
        checkShareShow = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (gpuImageFilter != null) {
            binding.imgToFilter.setFilter(gpuImageFilter);
        }
    }

    private void onSaveClicked() {
        binding.toolbarEdit.btnSave.setOnClickListener(view -> {
            mainViewModel.setIsClickShareButton(true);

            showLoadingDialog();

            Toast.makeText(getContext(), requireContext().getString(R.string.save_image_to_your_local_storage), Toast.LENGTH_SHORT).show();

            binding.imgEdit.setInEdit(false);
            Bitmap bitmapFilter = binding.imgToFilter.getGPUImage().captureBitmap();
            Bitmap bitmapSticker = binding.imgEdit.createBitmap();
            Bitmap bitmapDraw = binding.imgToDraw.createBitmap();
            mainViewModel.saveBitmapToLocal(bitmapFilter, bitmapSticker, bitmapDraw, requireContext());

            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.container_edit, new ShareFragment());
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        });
    }

    private void showExitDialog() {
        View v = getLayoutInflater().inflate(R.layout.custom_exit_dialog, null);

        Dialog exitDialog = new Dialog(requireContext(), R.style.MaterialDialogSheet);
        exitDialog.setContentView(v);
        exitDialog.setCancelable(false);
        exitDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        exitDialog.getWindow().setGravity(Gravity.CENTER);

        exitDialog.show();

        v.findViewById(R.id.btn_exit_confirm).setOnClickListener(view12 -> {
            Navigation.findNavController(binding.getRoot()).navigateUp();
            exitDialog.dismiss();
        });

        v.findViewById(R.id.btn_exit_cancel).setOnClickListener(view1 -> exitDialog.dismiss());
    }

    @Override
    protected void onPermissionGranted() {
    }
}