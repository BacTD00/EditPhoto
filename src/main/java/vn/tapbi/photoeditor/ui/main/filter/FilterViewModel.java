package vn.tapbi.photoeditor.ui.main.filter;

import android.content.Context;
import android.graphics.Bitmap;

import com.filter.helper.MagicFilterType;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import vn.tapbi.photoeditor.common.LiveEvent;
import vn.tapbi.photoeditor.data.repository.PhotoRepository;
import vn.tapbi.photoeditor.ui.base.BaseViewModel;
import vn.tapbi.photoeditor.utils.FilterOption;

@HiltViewModel
public class FilterViewModel extends BaseViewModel {
    public LiveEvent<FilterOption> filterOptionLiveEvent = new LiveEvent<>();
    public PhotoRepository photoRepository;

    @Inject
    public FilterViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void getFilter(Context context, Bitmap bitmap) {
        photoRepository.getAllFilters(context, bitmap).subscribe(new SingleObserver<FilterOption>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull FilterOption filterOption) {
                filterOptionLiveEvent.postValue(filterOption);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                filterOptionLiveEvent.postValue(null);
            }
        });
    }
}