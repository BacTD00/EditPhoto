package vn.tapbi.photoeditor.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;

import java.util.List;

import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.databinding.ItemFilterBinding;
import vn.tapbi.photoeditor.utils.PhotoUtils;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private final Context context;
    private final OnClickFilter onClickFilter;
    int id = 0;
    private List<MagicFilterType> magicFilterTypes;
    private List<Bitmap> bitmaps;

    public FilterAdapter(OnClickFilter onClickFilter, Context context) {
        this.onClickFilter = onClickFilter;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setId(int id) {
        this.id = id;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMagicFilterTypeList(List<MagicFilterType> magicFilterTypeList) {
        this.magicFilterTypes = magicFilterTypeList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListBitmapFilter(List<Bitmap> listBitmapFilter) {
        this.bitmaps = listBitmapFilter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterBinding itemFilterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_filter, parent, false);
        return new ViewHolder(itemFilterBinding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (magicFilterTypes != null) {
            try {
//                if (bitmaps.get(position).getWidth() >= bitmaps.get(position).getHeight()) {
//                    holder.itemFilterBinding.imgFilter.setImageBitmap(Bitmap.createBitmap(bitmaps.get(position), bitmaps.get(position).getWidth() / 2 - bitmaps.get(position).getHeight() / 2, 0, bitmaps.get(position).getHeight(), bitmaps.get(position).getHeight()));
//                } else {
//                    holder.itemFilterBinding.imgFilter.setImageBitmap(Bitmap.createBitmap(bitmaps.get(position), 0, bitmaps.get(position).getHeight() / 2 - bitmaps.get(position).getWidth() / 2, bitmaps.get(position).getWidth(), bitmaps.get(position).getWidth()));
//                }
                holder.itemFilterBinding.imgFilter.setImageBitmap(bitmaps.get(position));
                holder.itemFilterBinding.imgFilter.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (magicFilterTypes != null) {
            holder.itemFilterBinding.tvFilterType.setText(FilterManager.getInstance().getFilterName(magicFilterTypes.get(position)));
        }

        holder.itemFilterBinding.filterWrapper.setOnClickListener(view -> {
            onClickFilter.onChangeFilter(FilterManager.getInstance().getFilter(magicFilterTypes.get(position)), position);
            if (id >= 0) notifyItemChanged(id);
            if (id != position) {
                id = position;
                notifyItemChanged(id);
            }
        });
        if (id == position) {
            holder.itemFilterBinding.tvFilterType.setTextColor(context.getResources().getColor(R.color.ucrop_color_light_blue));
            holder.itemFilterBinding.viewFilter.setBackground(context.getResources().getDrawable(R.drawable.custom_blue_bound));
        } else {
            holder.itemFilterBinding.tvFilterType.setTextColor(context.getResources().getColor(R.color.white));
            holder.itemFilterBinding.viewFilter.setBackground(context.getResources().getDrawable(R.drawable.custom_black_bound));
        }
    }

    @Override
    public int getItemCount() {
        if (magicFilterTypes == null) {
            return 0;
        } else {
            return magicFilterTypes.size();
        }
    }

    public interface OnClickFilter {
        void onChangeFilter(GPUImageFilter gpuImageFilter, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemFilterBinding itemFilterBinding;

        public ViewHolder(ItemFilterBinding itemView) {
            super(itemView.getRoot());
            itemFilterBinding = itemView;
        }
    }
}