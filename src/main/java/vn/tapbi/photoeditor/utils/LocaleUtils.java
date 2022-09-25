package vn.tapbi.photoeditor.utils;

import static vn.tapbi.photoeditor.utils.Utils.isAtLeastSdkVersion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import java.util.Locale;

import timber.log.Timber;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.data.local.SharedPreferenceHelper;
import vn.tapbi.photoeditor.ui.main.MainActivity;

public class LocaleUtils {

    public static void applyLocale(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String localeString =preferences.getString(Constant.PREF_SETTING_LANGUAGE, Constant.LANGUAGE_EN);
        if(TextUtils.isEmpty(localeString)){
            localeString = Constant.LANGUAGE_EN;
        }
        Timber.e("applyLocale "+ localeString);
        Locale newLocale= new Locale(localeString);
        updateResource(context, newLocale);
        if(context!=context.getApplicationContext()){
            updateResource(context.getApplicationContext(),newLocale);
        }

    }

    public static void updateResource(Context context, Locale locale) {
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Locale current = getLocaleCompat(resources);
        if(current==locale){
            return;
        }
        Configuration configuration = new Configuration(resources.getConfiguration());
        if( isAtLeastSdkVersion(Build.VERSION_CODES.N)){
            configuration.setLocale(locale);
        }else if(isAtLeastSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)){
            configuration.setLocale(locale);
        }else{
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }

    public  static Locale getLocaleCompat(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        return isAtLeastSdkVersion(Build.VERSION_CODES.N) ? configuration.getLocales().get(0) : configuration.locale;
    }

    public static void applyLocaleAndRestart(Activity activity, String localeString){
        Timber.e("applyLocaleAndRestart "+ localeString);
        SharedPreferenceHelper.storeString(Constant.PREF_SETTING_LANGUAGE,localeString);
        LocaleUtils.applyLocale(activity);
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        ActivityCompat.finishAffinity(activity);
    }

}
