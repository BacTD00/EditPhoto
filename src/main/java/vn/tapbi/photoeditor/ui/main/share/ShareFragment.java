package vn.tapbi.photoeditor.ui.main.share;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.databinding.ShareFragmentBinding;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;
import vn.tapbi.photoeditor.ui.main.MainViewModel;
import vn.tapbi.photoeditor.utils.PhotoUtils;

public class ShareFragment extends BaseBindingFragment<ShareFragmentBinding, MainViewModel> {
    private String image;
    private long mLastClickTime = 0;

    public static ShareFragment newInstance() {
        return new ShareFragment();
    }

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.share_fragment;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.VISIBLE);

        binding.toolbarShare.toolbarTitleEdit.setText(requireContext().getString(R.string.share));
        binding.toolbarShare.btnSave.setVisibility(View.GONE);

        initImage();
        shareImage();
        onBackEvent();

        if (savedInstanceState != null) {
            image = savedInstanceState.getString(Constant.STATE_IMAGE_SHARE_FRAGMENT);
            setImage(image);
        }
    }

    private void setImage(String image1) {
        Glide.with(requireView()).asBitmap().load(image1).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                binding.imgToShare.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void initImage() {
        mainViewModel.pathLiveData.observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                image = s;
                setImage(image);
            } else binding.imgToShare.setImageResource(0);
        });
    }

    private void shareImage() {
        binding.btnSharing.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            PhotoUtils.shareImage(image, requireContext());
        });
    }

    private void onBackEvent() {
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();

        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
//                mainViewModel.setIsClickShareButton(false);
            }
            return true;
        });

//        binding.toolbarShare.imgIconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());

        binding.toolbarShare.imgIconBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStackImmediate();
            mainViewModel.setIsClickShareButton(false);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constant.STATE_IMAGE_SHARE_FRAGMENT, image);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainViewModel.pathLiveData.postValue(null);
    }

    @Override
    protected void onPermissionGranted() {

    }
}