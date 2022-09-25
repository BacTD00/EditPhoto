package vn.tapbi.photoeditor.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import timber.log.Timber;
import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.models.Sticker;
import vn.tapbi.photoeditor.databinding.ItemStickerBinding;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {
    private final ArrayList<Sticker> stickers;
    private final Context context;
    private final OnClickSticker onClickSticker;
    int id = 0;

    public StickerAdapter(ArrayList<Sticker> stickers, Context context, OnClickSticker onClickSticker) {
        this.stickers = stickers;
        this.context = context;
        this.onClickSticker = onClickSticker;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setId(int id) {
        this.id = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStickerBinding itemStickerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_sticker, parent, false);
        return new StickerViewHolder(itemStickerBinding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (stickers != null) {
            try {
                holder.itemStickerBinding.imgSticker.setImageResource(stickers.get(position).getStickerImg());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.itemStickerBinding.stickerWrapper.setOnClickListener(view -> {
            if (stickers != null) {
                onClickSticker.onChangeSticker(stickers.get(position).getStickerImg(), position);
            }
//            Toast.makeText(view.getContext(), "click on: " + position, Toast.LENGTH_SHORT).show();
            Timber.e("click on: %s", position);
            if (id >= 0) notifyItemChanged(id);
            if (id != position) {
                id = position;
                notifyItemChanged(id);
            }
        });

        if (id == position) {
            holder.itemStickerBinding.viewSticker.setBackground(context.getResources().getDrawable(R.drawable.custom_blue_bound));
        } else {
            holder.itemStickerBinding.viewSticker.setBackground(context.getResources().getDrawable(R.drawable.custom_black_bound));
        }
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public interface OnClickSticker {
        void onChangeSticker(int image, int id);
    }

    public static class StickerViewHolder extends RecyclerView.ViewHolder {
        ItemStickerBinding itemStickerBinding;

        public StickerViewHolder(ItemStickerBinding itemView) {
            super(itemView.getRoot());
            itemStickerBinding = itemView;
        }
    }
}

