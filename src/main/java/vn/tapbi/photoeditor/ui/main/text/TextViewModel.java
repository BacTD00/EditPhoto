package vn.tapbi.photoeditor.ui.main.text;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

import vn.tapbi.photoeditor.common.models.ColorType;
import vn.tapbi.photoeditor.common.models.Font;
import vn.tapbi.photoeditor.common.models.TextColor;
import vn.tapbi.photoeditor.data.repository.PhotoRepository;
import vn.tapbi.photoeditor.ui.base.BaseViewModel;

@HiltViewModel
public class TextViewModel extends BaseViewModel {
    public MutableLiveData<List<ColorType>> listColor = new MutableLiveData<>();
    public MutableLiveData<List<Font>> listFonts = new MutableLiveData<>();

    public PhotoRepository photoRepository;

    @Inject
    public TextViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void getListColorDraw(int type) {
        photoRepository.getColorList(type).subscribe(new SingleObserver<List<ColorType>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull List<ColorType> colors) {
                listColor.postValue(colors);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public void getListFont(String folder, Context context) {
        photoRepository.getListFont(folder, context).subscribe(new SingleObserver<List<Font>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull List<Font> fonts) {
                listFonts.postValue(fonts);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
}