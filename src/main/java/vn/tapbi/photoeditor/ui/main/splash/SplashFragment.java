package vn.tapbi.photoeditor.ui.main.splash;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import timber.log.Timber;
import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.databinding.SplashFragmentBinding;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;

public class SplashFragment extends BaseBindingFragment<SplashFragmentBinding, SplashViewModel> {
    private final static String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Dialog mBottomSheetDialog;
    private Button btnAllow;
    private View dialog;
    private long mLastClickTime = 0;

    @Override
    protected Class<SplashViewModel> getViewModel() {
        return SplashViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.splash_fragment;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        dialog = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        btnAllow = dialog.findViewById(R.id.btn_allow);

        mBottomSheetDialog = new Dialog(requireContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(dialog);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        checkPermission();

        binding.btnChoosePhoto.setOnClickListener(view1 -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.REQUEST_PICK_IMAGE);
        });
    }

    private void checkPermission() {
        if (!isAllPermissionGranted()) {
            mBottomSheetDialog.show();
            btnAllow.setOnClickListener(view -> Dexter.withContext(requireContext())
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                mBottomSheetDialog.dismiss();
                            }

                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }

                            Timber.e("%s", multiplePermissionsReport.isAnyPermissionPermanentlyDenied());
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(dexterError -> Toast.makeText(requireContext(), requireContext().getString(R.string.some_error), Toast.LENGTH_SHORT).show())
                    .onSameThread().check());
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.settings_dialog_title);
        builder.setMessage(R.string.settings_dialog_message);
        builder.setPositiveButton(R.string.settings_dialog_positive_button, (dialogInterface, i) -> {
            dialogInterface.cancel();
            openSettings();
        });

        builder.setNegativeButton(R.string.settings_dialog_negative_button, (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }


    private final ActivityResultLauncher<Intent> activityResultLauncher
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (isAllPermissionGranted()) {
            mBottomSheetDialog.dismiss();
        } else {
            mBottomSheetDialog.show();
        }

    });

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constant.REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    startCrop(selectedImage);
                }
            }
        }
    }

    private void startCrop(Uri selectedImage) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.REQUEST_URI_FROM_SPLASH, selectedImage.toString());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.uCropFragment, bundle);
    }

    @Override
    protected void onPermissionGranted() {

    }

    protected boolean isAllPermissionGranted() {
        boolean isNotGranted = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                isNotGranted = true;
            }
        }
        Timber.e("isAllPermissionsGranted %b", isNotGranted);
        return !isNotGranted;
    }
}