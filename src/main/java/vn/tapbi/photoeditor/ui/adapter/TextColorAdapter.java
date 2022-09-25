package vn.tapbi.photoeditor.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.util.List;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.common.models.ColorType;
import vn.tapbi.photoeditor.databinding.ItemDrawColorBinding;

public class TextColorAdapter extends RecyclerView.Adapter<TextColorAdapter.DrawColorViewHolder> {
    private final Context context;
    int id = 0;
    private List<ColorType> colorTypeList;
    private OnClickDrawColor onClickDrawColor;
    private OnClickTextColor onClickTextColor;

    public TextColorAdapter(OnClickDrawColor onClickDrawColor, Context context) {
        this.onClickDrawColor = onClickDrawColor;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setId(int id) {
        this.id = id;
        notifyDataSetChanged();
    }

    public int getId() {
        return id;
    }

    public void setListColor(List<ColorType> colorTypes) {
        this.colorTypeList = colorTypes;
    }

    @NonNull
    @Override
    public DrawColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDrawColorBinding itemDrawColorBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_draw_color, parent, false);
        return new DrawColorViewHolder(itemDrawColorBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawColorViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ColorType colorType = colorTypeList.get(position);
        int type = colorType.getType();
        if (type == Constant.TYPE_TEXT_COLOR || type == Constant.TYPE_TEXT_BACKGROUND) {
            holder.itemDrawColorBinding.imgDrawColor.setCircleBackgroundColor(android.graphics.Color.parseColor(Constant.COLOR_START_SYMBOL + colorType.getColor()));
            holder.itemDrawColorBinding.imgDrawColor.setOnClickListener(view -> {
                if (holder.getAdapterPosition() >= 0) {
                    try {
                        onClickDrawColor.onChangeDrawColor(holder.getAdapterPosition(), colorTypeList.get(holder.getAdapterPosition()).getColor());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (id >= 0) notifyItemChanged(id);
                if (id != holder.getAdapterPosition()) {
                    id = holder.getAdapterPosition();
                    notifyItemChanged(id);
                }
            });
        }

        if (id == position) {
            holder.itemDrawColorBinding.imgDrawColor.setBorderColor(ContextCompat.getColor(context, R.color.ucrop_color_light_blue));
            holder.itemDrawColorBinding.imgDrawColor.setBorderWidth(6);
        } else {
            holder.itemDrawColorBinding.imgDrawColor.setBorderColor(0);
            holder.itemDrawColorBinding.imgDrawColor.setBorderWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        if (colorTypeList == null) {
            return 0;
        } else {
            return colorTypeList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = colorTypeList.get(position).getType();
        if (type == Constant.TYPE_TEXT_COLOR)
            return Constant.TYPE_TEXT_COLOR;
        else
            return Constant.TYPE_TEXT_BACKGROUND;
    }

    public interface OnClickDrawColor {
        void onChangeDrawColor(int position, String color) throws FileNotFoundException;
    }

    public interface OnClickTextColor {
        void onChangeTextColor(int position, String color) throws FileNotFoundException;

        void onChangeTextBackgroundColor(int position, String color) throws FileNotFoundException;
    }

    public static class DrawColorViewHolder extends RecyclerView.ViewHolder {
        ItemDrawColorBinding itemDrawColorBinding;

        public DrawColorViewHolder(@NonNull ItemDrawColorBinding itemView) {
            super(itemView.getRoot());
            itemDrawColorBinding = itemView;
        }
    }
}
