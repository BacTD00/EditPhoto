package vn.tapbi.photoeditor.ui.main.sticker;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;
import vn.tapbi.photoeditor.common.models.Sticker;
import vn.tapbi.photoeditor.data.repository.PhotoRepository;
import vn.tapbi.photoeditor.ui.base.BaseViewModel;

@HiltViewModel
public class StickerViewModel extends BaseViewModel {
    public MutableLiveData<ArrayList<Sticker>> stickersLiveData = new MutableLiveData<>();
    public PhotoRepository photoRepository;

    @Inject
    public StickerViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void getSticker() {
        photoRepository.getListStickers().subscribe(new SingleObserver<List<Sticker>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull List<Sticker> stickers) {
                stickersLiveData.postValue((ArrayList<Sticker>) stickers);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                stickersLiveData.postValue(null);
                Timber.e(e);
            }
        });
    }
}