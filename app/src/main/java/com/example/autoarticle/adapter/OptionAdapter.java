package com.example.autoarticle.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;
import com.example.autoarticle.listener.OnRecyclerViewItemClick;
import com.example.autoarticle.model.OptionEntity;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    List<OptionEntity> optionEntities;
    OnRecyclerViewItemClick mOnRecyclerViewItemClick;
    private Context context;

    public OptionAdapter(Context context) {
        this.context = context;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        mOnRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public void setOptionEntities(List<OptionEntity> optionEntities) {
        this.optionEntities = optionEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_option, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OptionEntity optionEntity = optionEntities.get(position);
        Drawable drawable = optionEntity.getDrawable();
        if (drawable != null) {
            holder.ivOptionIcon.setVisibility(View.VISIBLE);
            holder.ivOptionIcon.setImageDrawable(optionEntity.getDrawable());
        } else {
            holder.ivOptionIcon.setVisibility(View.GONE);
        }
        if (optionEntities != null && optionEntities.size() > 0) {
            int size = optionEntities.size();
            if (size > 1) {
                if (position == 0) {
                    holder.itemView.setBackground(addStateDrawable(context, R.drawable.shape_top_corners_bg_normal,
                            R.drawable.shape_top_corners_bg_press, R.drawable.shape_top_corners_bg_press));
                } else if (position == size - 1) {
                    holder.itemView.setBackground(addStateDrawable(context, R.drawable.shape_bottom_corners_bg_normal,
                            R.drawable.shape_bottom_corners_bg_press, R.drawable.shape_bottom_corners_bg_press));
                } else {
                    holder.itemView.setBackground(addStateDrawable(context, R.drawable.shape_bg_normal,
                            R.drawable.shape_bg_press, R.drawable.shape_bg_press));
                }
            } else {
                holder.itemView.setBackground(addStateDrawable(context, R.drawable.shape_white_small_corners_bg,
                        R.drawable.shape_gray_small_corners_bg, R.drawable.shape_gray_small_corners_bg));
            }
            if (position == size - 1) {
                holder.viewDividerLine.setVisibility(View.GONE);
            } else {
                holder.viewDividerLine.setVisibility(View.VISIBLE);
            }
        }
        holder.tvOptionName.setText(optionEntity.getOptionName());
    }

    @Override
    public int getItemCount() {
        if (optionEntities != null && optionEntities.size() > 0) {
            return optionEntities.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View itemView;
        ImageView ivOptionIcon;
        TextView tvOptionName;
        View viewDividerLine;

        public ViewHolder(View itemView) {
            super(itemView);
            ivOptionIcon = (ImageView) itemView.findViewById(R.id.imageview_option_icon);
            tvOptionName = (TextView) itemView.findViewById(R.id.textview_option_name);
            viewDividerLine = itemView.findViewById(R.id.view_divider_line);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnRecyclerViewItemClick != null) {
                mOnRecyclerViewItemClick.onItemClick(view, getAdapterPosition());
            }
        }
    }
    private StateListDrawable addStateDrawable(Context context, int idNormal, int idPressed, int idFocused) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focus = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }
}
