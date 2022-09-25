package vn.tapbi.photoeditor.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;

import com.yalantis.ucrop.UCropFragmentCallback;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.databinding.ActivityMainBinding;
import vn.tapbi.photoeditor.ui.base.BaseBindingActivity;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding, MainViewModel> implements UCropFragmentCallback {
    NavHostFragment navHostFragment;
    NavController navCo;
    private String resultUri;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public void setupView(Bundle savedInstanceState) {
        setTheme(R.style.Theme_PhotoEditor);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public String getUri() {
        return resultUri;
    }

    @Override
    public void setupData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (navHostFragment != null) {
            navCo = navHostFragment.getNavController();
        }
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCrop(String uriPath) {
        resultUri = uriPath;
        if (resultUri != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.REQUEST_URI_FROM_MAIN_ACTIVITY, resultUri);
            navCo.navigate(R.id.editFragment, bundle);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}