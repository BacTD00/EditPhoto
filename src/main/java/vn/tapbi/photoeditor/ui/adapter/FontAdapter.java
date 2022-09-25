package vn.tapbi.photoeditor.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.util.List;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.common.models.Font;
import vn.tapbi.photoeditor.databinding.ItemFontBinding;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {
    private final OnClickFont onClickFont;
    private final Context context;
    private List<Font> fonts;
    private int id = -1;

    public FontAdapter(OnClickFont onClickFont, Context context) {
        this.onClickFont = onClickFont;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setId(int id) {
        this.id = id;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFonts(List<Font> fonts) {
        this.fonts = fonts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFontBinding itemFontBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_font, parent, false);
        return new FontViewHolder(itemFontBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if (id == holder.getAdapterPosition()) {
            holder.itemFontBinding.tvFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.ucrop_color_light_blue));
            holder.itemFontBinding.tvItemAgFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.ucrop_color_light_blue));
        } else {
            holder.itemFontBinding.tvFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.white));
            holder.itemFontBinding.tvItemAgFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.white));
        }

        holder.itemFontBinding.tvFont.setText(fonts.get(position).getName());
        holder.itemFontBinding.fontWrapper.setOnClickListener(view -> {
            if (holder.getAdapterPosition() >= 0) {
                String fontType = Constant.FONT_START + fonts.get(holder.getAdapterPosition()).getFont();
                try {
                    onClickFont.onChangeFont(holder.getAdapterPosition(), fontType);
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

    @Override
    public int getItemCount() {
        if (fonts == null) {
            return 0;
        } else {
            return fonts.size();
        }
    }

    public interface OnClickFont {
        void onChangeFont(int position, String font) throws FileNotFoundException;
    }

    public static class FontViewHolder extends RecyclerView.ViewHolder {
        ItemFontBinding itemFontBinding;

        public FontViewHolder(@NonNull ItemFontBinding itemView) {
            super(itemView.getRoot());
            itemFontBinding = itemView;
        }
    }
}
