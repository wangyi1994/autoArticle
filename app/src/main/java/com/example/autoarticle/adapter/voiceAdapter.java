package com.example.autoarticle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;

import java.util.ArrayList;
import java.util.List;

public class voiceAdapter extends RecyclerView.Adapter<voiceAdapter.HolderMakeScene>{
    private Context context;
    private List<String> sceneList;

    private OnMakeItemEvent onMakeItemEvent;

    /**
     *  暂存每个item 的view
     */
    private List<View> viewList;
    public void setOnMakeItemEvent(OnMakeItemEvent onMakeItemEvent){
        this.onMakeItemEvent= onMakeItemEvent;
    }

    public void  makeSceneClick(int position) {
        for(int i =0;i<viewList.size();i++){
            View itemView=viewList.get(i);
            TextView  make_scene_name = (TextView) itemView.findViewById(R.id.make_scene_name);
            RelativeLayout  make_scene_container = (RelativeLayout) itemView.findViewById(R.id.make_scene_container);
            if(i==position){
                make_scene_name.setTextColor(context.getResources().getColor(R.color.white));
                make_scene_container.setBackgroundColor(context.getResources().getColor(R.color.black));
            }
            else{
                make_scene_name.setTextColor(context.getResources().getColor(R.color.black));
                make_scene_container.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
    }


    public voiceAdapter(Context context, List<String> sceneList) {
        this.context = context;
        this.sceneList=sceneList;
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
        String name= sceneList.get(position);
        holder.make_scene_name.setText(name);
        holder.make_scene_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMakeItemEvent.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sceneList.size();
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