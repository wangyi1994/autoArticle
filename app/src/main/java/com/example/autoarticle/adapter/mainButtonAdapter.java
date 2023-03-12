package com.example.autoarticle.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;
import com.example.autoarticle.activity.MainActivity;
import com.example.autoarticle.model.character;

import java.util.ArrayList;
import java.util.List;

public class mainButtonAdapter extends RecyclerView.Adapter<mainButtonAdapter.Holder> {
    private MainActivity mainActivity;
    private List<String> buttons;


    /**
     * 暂存每个item 的view
     */
    private List<View> viewList;

    public mainButtonAdapter(MainActivity mainActivity, List<String> buttonList) {
        this.mainActivity = mainActivity;
        this.buttons = buttonList;
        viewList = new ArrayList<>();
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_main_button, parent, false);
        Holder holder = new mainButtonAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") final int position) {
        String button = buttons.get(position);
        holder.main_button_name.setText(button);
        if(button.equals("聊天")){
            holder.main_button_img.setBackground(mainActivity.getResources().getDrawable(R.drawable.message_focus));
            holder.main_button_name.setTextColor(mainActivity.getResources().getColor(R.color.button_focus));
        }
        holder.main_button_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageChange(position);
                mainActivity.PageSelectd(position);
                holder.main_button_name.setTextColor(mainActivity.getResources().getColor(R.color.button_focus));

            }
        });

    }

    public void PageChange(int position){
        String button=buttons.get(position);
         for (int i=0;i<viewList.size();i++){
             View view1=viewList.get(i);
            if(i==0){
                view1.findViewById(R.id.main_button_img).setBackground(mainActivity.getResources().getDrawable(R.drawable.message));
            }
            else{
                view1.findViewById(R.id.main_button_img).setBackground(mainActivity.getResources().getDrawable(R.drawable.mine));
            }
            TextView name= view1.findViewById(R.id.main_button_name);
            name.setTextColor(mainActivity.getResources().getColor(R.color.button_unfocus));
        }
        if(button.equals("聊天")){
            viewList.get(position).findViewById(R.id.main_button_img).setBackground(mainActivity.getResources().getDrawable(R.drawable.message_focus));
        }
        else{
            viewList.get(position).findViewById(R.id.main_button_img).setBackground(mainActivity.getResources().getDrawable(R.drawable.mine_focus));
        }
    }
    @Override
    public int getItemCount() {
        return buttons.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        LinearLayout main_button_container;
        TextView main_button_name;

        ImageView main_button_img;
        MotionEvent event;

        public Holder(View itemView) {
            super(itemView);
            viewList.add(itemView);
            main_button_container = (LinearLayout) itemView.findViewById(R.id.main_button_container);
            main_button_name = (TextView) itemView.findViewById(R.id.main_button_name);
            main_button_img = (ImageView) itemView.findViewById(R.id.main_button_img);
        }

    }

    public interface OnMakeItemEvent {
        void onItemClick(int position);
    }

}