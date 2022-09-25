package vn.tapbi.photoeditor.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.common.models.ColorType;
import vn.tapbi.photoeditor.databinding.ItemDrawColorBinding;

public class ColorAdapter extends RecyclerView.Adapter {
    private final OnClickColorImage onClickColorImage;
    private final Context context;
    private List<ColorType> colorTypes;
    private int id = -1;

    public ColorAdapter(OnClickColorImage onClickColorImage, Context context) {
        this.onClickColorImage = onClickColorImage;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setId(int id) {
        this.id = id;
        notifyDataSetChanged();
    }

    public void setListColor(List<ColorType> colorTypes) {
        this.colorTypes = colorTypes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_COLOR) {
            ItemDrawColorBinding itemDrawColorBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_draw_color, parent, false);
            return new ViewHolderColor(itemDrawColorBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ColorType color = colorTypes.get(position);
        int type = color.getType();
        if (type == Constant.TYPE_COLOR) {
            ((ViewHolderColor) holder).itemDrawColorBinding.imgDrawColor.setCircleBackgroundColor(android.graphics.Color.parseColor(Constant.COLOR_START_SYMBOL + color.getColor()));
            ((ViewHolderColor) holder).itemDrawColorBinding.imgDrawColor.setOnClickListener(v -> {
                if (holder.getAdapterPosition() >= 0)
                    onClickColorImage.onClickColor(holder.getAdapterPosition(), colorTypes.get(holder.getAdapterPosition()).getColor());
                if (id >= 0) notifyItemChanged(id);
                if (id != holder.getAdapterPosition()) {
                    id = holder.getAdapterPosition();
                    notifyItemChanged(id);
                }
            });

            if (id == position) {
                ((ViewHolderColor) holder).itemDrawColorBinding.imgDrawColor.setBorderColor(ContextCompat.getColor(context, R.color.ucrop_color_light_blue));
                ((ViewHolderColor) holder).itemDrawColorBinding.imgDrawColor.setBorderWidth(6);
            } else {
                ((ViewHolderColor) holder).itemDrawColorBinding.imgDrawColor.setBorderColor(0);
                ((ViewHolderColor) holder).itemDrawColorBinding.imgDrawColor.setBorderWidth(0);
            }
        }
    }

    @Override
    public int getItemCount() {
        return colorTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Constant.TYPE_COLOR;
    }

    public interface OnClickColorImage {
        void onClickColor(int position, String color);
    }

    public static class ViewHolderColor extends RecyclerView.ViewHolder {
        ItemDrawColorBinding itemDrawColorBinding;

        public ViewHolderColor(@NonNull ItemDrawColorBinding itemView) {
            super(itemView.getRoot());
            itemDrawColorBinding = itemView;
        }
    }
}
