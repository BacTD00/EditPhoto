package vn.tapbi.photoeditor.ui.main.sticker;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.models.Sticker;
import vn.tapbi.photoeditor.databinding.StickerFragmentBinding;
import vn.tapbi.photoeditor.ui.adapter.StickerAdapter;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;
import vn.tapbi.photoeditor.ui.main.edit.EditFragment;

public class StickerFragment extends BaseBindingFragment<StickerFragmentBinding, StickerViewModel> implements StickerAdapter.OnClickSticker {
    EditFragment editFragment;
    private StickerAdapter mStickerAdapter;

    private boolean isTest = true;

    private int countUndoSticker = 0, countRedoSticker = 0;

    public static StickerFragment newInstance() {
        return new StickerFragment();
    }

    @Override
    protected Class<StickerViewModel> getViewModel() {
        return StickerViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.sticker_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        setupViews();
    }

    private void setupViews() {
        binding.tools.tvWidgetName.setText(R.string.sticker);

        addSticker();

        editFragment = (EditFragment) StickerFragment.this.getParentFragment();

        binding.tools.icRemoveAll.setOnClickListener(view2 -> {
            Toast.makeText(requireContext(), requireContext().getString(R.string.remove_all_sticker), Toast.LENGTH_SHORT).show();
            mainViewModel.setIsClickItemSticker(false);

            countUndoSticker -= 1;

            assert editFragment != null;
            editFragment.binding.imgEdit.deleteSticker();
            mStickerAdapter.setId(-1);
        });

        binding.tools.icDone.setOnClickListener(view -> {
            mainViewModel.setIsClickItemSticker(false);
            editFragment.binding.imgEdit.setInEdit(false);
            mStickerAdapter.setId(-1);
        });

        binding.tools.icUndo.setOnClickListener(view -> {
            countUndoSticker -= 1;
            countRedoSticker += 1;

            setUndoIcon();
            setRedoIcon();

            editFragment.binding.imgEdit.setInEdit(false);
            editFragment.binding.imgEdit.undoSticker();
        });

        binding.tools.icRedo.setOnClickListener(view -> {
            countRedoSticker -= 1;
            countUndoSticker += 1;

            setUndoIcon();
            setRedoIcon();

            editFragment.binding.imgEdit.redoSticker();
        });
    }

    private void setUndoIcon() {
        if (countUndoSticker > 0) {
            binding.tools.icUndo.setImageResource(R.drawable.ic_undo_selected);
        } else {
            binding.tools.icUndo.setImageResource(R.drawable.ic_undo);
        }
    }

    private void setRedoIcon() {
        if (countRedoSticker > 0) {
            binding.tools.icRedo.setImageResource(R.drawable.ic_redo_selected);
        } else {
            binding.tools.icRedo.setImageResource(R.drawable.ic_redo);
        }
    }

    private void addSticker() {
        viewModel.getSticker();
        viewModel.stickersLiveData.observe(this, stickers -> {
            showListStickers(stickers, binding.rcvItem);
        });
    }

    public void showListStickers(ArrayList<Sticker> stickerList, RecyclerView recyclerView) {
        mStickerAdapter = new StickerAdapter(stickerList, requireContext(), this);
        recyclerView.setHasFixedSize(true);
        mStickerAdapter.setId(-1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mStickerAdapter);
    }

    @Override
    protected void onPermissionGranted() {

    }

    @Override
    public void onChangeSticker(int image, int id) {
        countUndoSticker += 1;

        setUndoIcon();
        setRedoIcon();

        assert editFragment != null;
        Drawable drawable = ContextCompat.getDrawable(requireContext(), image);
//        editFragment.binding.imgEdit.deleteSticker();
        editFragment.binding.imgEdit.addSticker(drawable);
    }
}