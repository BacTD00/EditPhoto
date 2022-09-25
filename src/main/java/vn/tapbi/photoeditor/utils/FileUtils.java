package vn.tapbi.photoeditor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class FileUtils {

//    public static File getNewFile(Context context, String folderName) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
//
//        String timeStamp = simpleDateFormat.format(new Date());
//
//        String path;
//
//        path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpeg";
//
//        if (TextUtils.isEmpty(path)) {
//            return null;
//        }
//
//        return new File(path);
//    }

    public static String saveBitmapToLocal(Bitmap bm, int quality, Context context) {
        String path;
        try {
            File rootFile = new File(context.getCacheDir() + File.separator + "image");
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File file = new File(rootFile, Calendar.getInstance().getTimeInMillis() + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
            //Insert files into the system Gallery
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), path, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }
}