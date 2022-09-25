package vn.tapbi.photoeditor.data.local;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SharedPreferenceHelper {
    private static final int DEFAULT_NUM = 0;
    private static final String DEFAULT_STRING = "";
    public static SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferenceHelper(SharedPreferences sharedPreferences) {
        SharedPreferenceHelper.sharedPreferences = sharedPreferences;
    }

    public static void storeString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, DEFAULT_STRING);
    }
    public static String getStringWithDefault(String key,String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void storeInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return sharedPreferences.getInt(key, DEFAULT_NUM);
    }

    public static int getIntWithDefault(String key,int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    public static void storeLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public static long getLong(String key) {
        return sharedPreferences.getLong(key, DEFAULT_NUM);
    }


    public static void storeBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void storeFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0f);
    }

    public static void setStringArrayPref(String key, List<String> values) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, DEFAULT_STRING);
        }
        editor.apply();
    }

    public static List<String> getStringArrayPref(String key) {

        String json = sharedPreferences.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static void setIntArray(String key, List<Integer> values) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, "");
        }
        editor.apply();
    }

    public static List<Integer> getIntArray(String key) {
        String json = sharedPreferences.getString(key, null);
        ArrayList<Integer> urls = new ArrayList<Integer>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(Integer.valueOf(url));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static void removeKey(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public static void resetAll() {
        sharedPreferences.edit().clear().apply();
    }

    public static boolean containKey(String key) {
        return sharedPreferences.contains(key);
    }
}

