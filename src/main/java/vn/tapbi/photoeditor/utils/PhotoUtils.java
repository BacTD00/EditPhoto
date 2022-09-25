package vn.tapbi.photoeditor.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.view.Display;
import android.view.WindowManager;

import com.filter.base.GPUImage;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.common.models.ColorType;
import vn.tapbi.photoeditor.common.models.Font;

public class PhotoUtils {
    public static Bitmap resizeBitmap(Bitmap mBitmap, Context context) {
        Bitmap bitmap = null;
        int wB = mBitmap.getWidth();
        int hB = mBitmap.getHeight();
        float tl = (float) wB / hB;
        float hs = 1;
        int maxHeight = getScrHeight(context);
        if (tl > 1) {

            if (wB > getScrWidth(context)) {
                hs = (float) wB / maxHeight;
                wB = maxHeight;
                hB = (int) (hB / hs);
                bitmap = Bitmap.createScaledBitmap(mBitmap, wB, hB, false);
            } else {
                bitmap = mBitmap;
            }
        } else {
            if (hB > maxHeight) {
                hs = (float) hB / maxHeight;
                hB = maxHeight;
                wB = (int) (wB / hs);
                bitmap = Bitmap.createScaledBitmap(mBitmap, wB, hB, false);
            } else {
                bitmap = mBitmap;
            }
        }
        return bitmap;
    }

    public static Bitmap bitmapResizer(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, paint);

        return scaledBitmap;
    }

    public static void shareImage(String image, Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(image);
        sharingIntent.setType(Constant.IMAGE_JPEG);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        context.startActivity(Intent.createChooser(sharingIntent, Constant.SHARE_IMAGE));
    }

    public static String[] listColor() {
        return new String[]{"FFFFFF", "000000", "E5E5E5", "CFCFCF", "A7A7A7", "00078c", "010bc5", "000be1", "000bf0", "0a16ff", "202bff", "333dff", "454fff",
                "5962ff", "747bff", "888eff", "a2a6ff", "00b4c3", "03d0e3", "00e6fd", "1decfe", "42efff", "5feefd", "8af5ff", "b4f9ff", "d2fbff", "7d01a1", "9101b9", "a800d9",
                "be03f4", "cb10ff", "ce23ff", "d23bfd", "d43eff", "db64fd", "dc68fd", "eeb1ff", "f4ccff", "00790f", "029f14", "01ba17", "03da1b", "00ff1f", "17fe32", "3aff50",
                "57ff6a", "72fe81", "83ff91", "a2ffae", "beffc7", "a3008f", "ba01a2", "c001a7", "e904cc", "ee02d0", "f200d3", "f93ae0", "fc65e8", "fe8aef", "b7005a", "cd0065",
                "e30171", "ff017e", "ff148a", "ff3196", "ff4da5", "ff6ab4", "ff88c2", "ffa4d0", "ffc4e2", "ffdaec", "7aab01", "9cdc01", "a9ef01", "b4ff04", "c3ff2e", "caff4f",
                "d4ff6d", "dcff97", "e5ffa4", "c23b01", "d94402", "ee4a02", "ff550a", "ff631e", "ff7a41", "ff8853", "ff9c72", "ffab86", "ffba9d", "ffcab2", "7f0800", "950b01",
                "b20b02", "cb0f04", "e20c01", "ff180a", "ff281b", "ff4338", "ff574e", "fb6e85", "ff8c85", "f9aca6", "4b00a7", "5800c8", "6400df", "7100fd", "7f17ff", "8f34fe",
                "9f52ff", "ab69ff", "be8cff", "d0aaff", "e2c9ff", "014fbb", "0257cc", "0061e4", "046bf8", "197aff", "368bff", "5ba0ff", "82b6ff", "a2cafe", "04eca3", "00ffaf",
                "1cffb7", "3dfdc1", "5effcd", "8bffda", "acffe5", "c4ffed", "c4ffed", "fff001", "fcf000", "fff335", "fdf352", "fef455", "fef99e", "fffbc1"};
    }

    public static List<ColorType> getListColor(int type) {
        List<ColorType> colors = new ArrayList<>();
        for (int i = 0; i < listColor().length; i++) {
            ColorType color = new ColorType(listColor()[i], type);
            colors.add(color);
        }
        return colors;
    }

    // font
    public static List<Font> getListFont(Context context, String folder) {
        List<Font> fonts = new ArrayList<>();
        List<String> list = getListFromAssets(context, folder);
        for (int i = 0; i < list.size(); i++) {
            String[] strFont = list.get(i).split(Constant.REGEX);
            String tempStrFont = strFont[5];
            String[] fontName = tempStrFont.split(Constant.DOT);
            Font font = new Font(tempStrFont);
            font.setName(fontName[0]);
            fonts.add(font);
        }
        return fonts;
    }

    public static List<String> getListFromAssets(Context context, String folder) {
        ArrayList<String> list = new ArrayList<>();
        try {
            String[] images = context.getAssets().list(folder);
            for (String image : images) {
                list.add("file:///android_asset/" + folder + File.separator + image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static FilterOption getListFilter(Context context, Bitmap bitmap) { // get list filter
        FilterManager.init(context);

        Bitmap bmScale = Bitmap.createScaledBitmap(bitmap, Constant.SIZE_FILTER, Constant.SIZE_FILTER, false);

        List<MagicFilterType> list = Arrays.asList(FilterManager.getInstance().types);
        List<Bitmap> listBMFilter = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            GPUImage gpuImage = new GPUImage(context);
            gpuImage.setImage(bmScale);
            gpuImage.setFilter(FilterManager.getInstance().getFilter(list.get(i)));
            Bitmap bitmapFilter = gpuImage.getBitmapWithFilterApplied();
            listBMFilter.add(bitmapFilter);
        }

        return new FilterOption(list, bmScale, listBMFilter);
    }


    public static int getScrWidth(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScrHeight(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    public static Bitmap changeBitmapSize(Bitmap bitmap, int size) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int wB = 0;
        int hB = 0;
        Bitmap bitmapResult = null;
        float s = (float) width / height;
        float tl = 0;
        if (s > 1) {
            tl = (float) width / size;
            wB = size;
            hB = (int) (height / tl);
        } else {
            tl = (float) height / size;
            hB = size;
            wB = (int) (width / tl);
        }
        bitmapResult = Bitmap.createScaledBitmap(bitmap, wB, hB, false);

        return bitmapResult;
    }
}
