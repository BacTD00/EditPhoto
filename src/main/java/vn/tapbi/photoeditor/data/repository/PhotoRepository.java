package vn.tapbi.photoeditor.data.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.models.ColorType;
import vn.tapbi.photoeditor.common.models.Font;
import vn.tapbi.photoeditor.common.models.Sticker;
import vn.tapbi.photoeditor.utils.FileUtils;
import vn.tapbi.photoeditor.utils.FilterOption;
import vn.tapbi.photoeditor.utils.PhotoUtils;

public class PhotoRepository {

    @Inject
    public PhotoRepository() {
    }

    /**
     * get filters
     *
     * @param context
     * @param bitmap
     * @return
     */
    public @NonNull Single<FilterOption> getAllFilters(Context context, Bitmap bitmap) {
        return Single.fromCallable(() -> PhotoUtils.getListFilter(context, bitmap))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * get stickers
     *
     * @return
     */
    private List<Sticker> getAllStickers() {
        List<Sticker> stickers = new ArrayList<>();
        stickers.add(new Sticker(R.drawable.sticker_1));
        stickers.add(new Sticker(R.drawable.sticker_2));
        stickers.add(new Sticker(R.drawable.sticker_3));
        stickers.add(new Sticker(R.drawable.sticker_4));
        stickers.add(new Sticker(R.drawable.sticker_5));
        stickers.add(new Sticker(R.drawable.sticker_6));

        return stickers;
    }

    public Single<List<Sticker>> getListStickers() {
        return Single.fromCallable(this::getAllStickers).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // draw color
    public Single<List<ColorType>> getColorList(int type) {
        return Single.fromCallable(() -> PhotoUtils.getListColor(type)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // font
    public Single<List<Font>> getListFont(String folder, Context context) {
        return Single.fromCallable(() -> PhotoUtils.getListFont(context, folder)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * save image
     *
     * @param bitmap
     * @param bmSticker
     * @param bmDraw
     * @param context
     * @return
     */
    public Single<String> saveImageToStorage(Bitmap bitmap, Bitmap bmSticker, Bitmap bmDraw, Context context) {
        return Single.fromCallable(() -> saveImage(bitmap, bmSticker, bmDraw, context)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public String saveImage(Bitmap bitmap, Bitmap bmSticker, Bitmap bmDraw, Context context) {
        int maxH = PhotoUtils.getScrHeight(context);
        Bitmap bmSave = PhotoUtils.changeBitmapSize(bitmap, maxH);
        Bitmap bmGroup = Bitmap.createBitmap(bmSave.getWidth(), bmSave.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmGroup);
        canvas.drawBitmap(bmSave, 0, 0, null);
        Bitmap bmDraw1 = PhotoUtils.bitmapResizer(bmDraw, bmGroup.getWidth(), bmGroup.getHeight());
        canvas.drawBitmap(bmDraw1, 0, 0, null);
        Bitmap bmSticker1 = PhotoUtils.bitmapResizer(bmSticker, bmGroup.getWidth(), bmGroup.getHeight());
        canvas.drawBitmap(bmSticker1, 0, 0, null);

        return FileUtils.saveBitmapToLocal(bmGroup, 100, context);
    }
}
