package vn.tapbi.photoeditor.ui.main;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.filter.base.GPUImageFilter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import vn.tapbi.photoeditor.common.LiveEvent;
import vn.tapbi.photoeditor.common.models.ColorData;
import vn.tapbi.photoeditor.common.models.MessageEvent;
import vn.tapbi.photoeditor.data.repository.PhotoRepository;
import vn.tapbi.photoeditor.ui.base.BaseViewModel;

@HiltViewModel
public class MainViewModel extends BaseViewModel {
    // filter
    public MutableLiveData<Boolean> isClickItemFilter = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backFilter = new MutableLiveData<>(false);
    public LiveEvent<GPUImageFilter> gpuImageFilterLiveEvent = new LiveEvent<>();

    // draw
    public MutableLiveData<Boolean> isClickItemDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickColorDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickSizeDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isDeleteDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isUndoDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isRedoDraw = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backColorBrush = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> backSizeBrush = new MutableLiveData<>(false);
    public MutableLiveData<Integer> brushSize = new MutableLiveData<>();
    public MutableLiveData<ColorData> colorBrush = new MutableLiveData<>();
    public MutableLiveData<Integer> chooseColorDraw = new MutableLiveData<>();

    // sticker
    public MutableLiveData<Boolean> isClickItemSticker = new MutableLiveData<>(false);

    // text
    public MutableLiveData<Boolean> isClickItemText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickFontText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickColorText = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isClickBgColorText = new MutableLiveData<>(false);

    // share
    public MutableLiveData<Boolean> isClickShareButton = new MutableLiveData<>(false);

    // image path
    public MutableLiveData<String> pathLiveData = new MutableLiveData<>();

    public LiveEvent<MessageEvent> messageEventLiveEvent = new LiveEvent<>();

    public PhotoRepository photoRepository;

    @Inject
    public MainViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public String setContentEditText(String text) {
        return text;
    }

    // filter
    public void setIsClickItemFilter(Boolean isClickItemFilter) {
        this.isClickItemFilter.setValue(isClickItemFilter);
    }

    // draw
    public void setIsDeleteDraw(Boolean isDeleteDraw) {
        this.isDeleteDraw.setValue(isDeleteDraw);
    }

    public void setColorBrush(ColorData colorDataBrush) {
        this.colorBrush.setValue(colorDataBrush);
    }

    public void setBrushSize(int brushSize) {
        this.brushSize.setValue(brushSize);
    }

    public void setIsClickItemDraw(Boolean isClickItemDraw) {
        this.isClickItemDraw.setValue(isClickItemDraw);
    }

    public void setIsClickColorDraw(Boolean isClickColorDraw) {
        this.isClickColorDraw.setValue(isClickColorDraw);
    }

    public void setIsUndoDraw(Boolean isUndoDraw) {
        this.isUndoDraw.setValue(isUndoDraw);
    }

    public void setIsRedoDraw(Boolean isRedoDraw) {
        this.isRedoDraw.setValue(isRedoDraw);
    }

    public void setIsClickSizeDraw(Boolean isClickSizeDraw) {
        this.isClickSizeDraw.setValue(isClickSizeDraw);
    }

    public void setChooseColorDraw(int chooseColorDraw) {
        this.chooseColorDraw.setValue(chooseColorDraw);
    }

    // sticker
    public void setIsClickItemSticker(Boolean isClickItemSticker) {
        this.isClickItemSticker.setValue(isClickItemSticker);
    }

    // text
    public void setIsClickItemText(Boolean isClickItemText) {
        this.isClickItemText.setValue(isClickItemText);
    }

    public void setIsClickFontText(Boolean isClickFontText) {
        this.isClickFontText.setValue(isClickFontText);
    }

    public void setIsClickColorText(Boolean isClickColorText) {
        this.isClickColorText.setValue(isClickColorText);
    }

    public void setIsClickBgColorText(Boolean isClickBgColorText) {
        this.isClickBgColorText.setValue(isClickBgColorText);
    }

    // share
    public void setIsClickShareButton(Boolean isClickShareButton) {
        this.isClickShareButton.setValue(isClickShareButton);
    }

    public void saveBitmapToLocal(Bitmap bitmap, Bitmap bmSticker, Bitmap bmDraw, Context context) {
        photoRepository.saveImageToStorage(bitmap, bmSticker, bmDraw, context).subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull String s) {
                pathLiveData.postValue(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }
}
