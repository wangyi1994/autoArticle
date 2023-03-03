package com.example.autoarticle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.autoarticle.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class voiceAdapter extends RecyclerView.Adapter<voiceAdapter.HolderMakeScene>{
    private Context context;
    private List<String> voiceList;


    /**
     *  暂存每个item 的view
     */
    private List<View> viewList;

    public voiceAdapter(Context context, List<String> voiceList) {
        this.context = context;
        this.voiceList=voiceList;
        viewList=new ArrayList<>();
    }

    @NonNull
    @Override
    public voiceAdapter.HolderMakeScene onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_make_scene, parent, false);
        voiceAdapter.HolderMakeScene holderMakeScene = new voiceAdapter.HolderMakeScene(view);
        return holderMakeScene;
    }

    @Override
    public void onBindViewHolder(@NonNull voiceAdapter.HolderMakeScene holder, @SuppressLint("RecyclerView") final int position) {
        String name= voiceList.get(position);
        holder.make_scene_name.setText(name);
        holder.make_scene_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return voiceList.size();
    }

    class HolderMakeScene extends RecyclerView.ViewHolder  {

        TextView make_scene_name;
        RelativeLayout make_scene_container;
        TextView make_scene_description;
        MotionEvent event;

        public HolderMakeScene(View itemView) {
            super(itemView);
            viewList.add(itemView);
            make_scene_name = (TextView) itemView.findViewById(R.id.make_scene_name);
            make_scene_description= (TextView) itemView.findViewById(R.id.make_scene_description);
            make_scene_container = (RelativeLayout) itemView.findViewById(R.id.make_scene_container);
        }

    }
    public interface OnMakeItemEvent {
        void onItemClick(int position);
    }

}