package vn.tapbi.photoeditor.ui.main.draw;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import vn.tapbi.photoeditor.common.models.ColorType;
import vn.tapbi.photoeditor.data.repository.PhotoRepository;
import vn.tapbi.photoeditor.ui.base.BaseViewModel;

@HiltViewModel
public class DrawViewModel extends BaseViewModel {
    public MutableLiveData<List<ColorType>> listColor = new MutableLiveData<>();

    public PhotoRepository photoRepository;

    @Inject
    public DrawViewModel(PhotoRepository photoRepository) {
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
}