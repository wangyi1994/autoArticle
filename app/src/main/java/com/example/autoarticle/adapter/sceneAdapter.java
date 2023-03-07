package com.example.autoarticle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;
import com.example.autoarticle.model.scenario;

import java.util.ArrayList;
import java.util.List;

public class sceneAdapter extends RecyclerView.Adapter<sceneAdapter.HolderMakeScene>{
    private Context context;
    private List<scenario> sceneList;

    private OnMakeItemEvent onMakeItemEvent;

    /**
     *  暂存每个item 的view
     */
    private List<View> viewList;
    public void setOnMakeItemEvent(OnMakeItemEvent onMakeItemEvent){
        this.onMakeItemEvent= onMakeItemEvent;
    }



    public sceneAdapter(Context context, List<scenario> sceneList) {
        this.context = context;
        this.sceneList=sceneList;
        viewList=new ArrayList<>();
    }

    @NonNull
    @Override
    public sceneAdapter.HolderMakeScene onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_make_scene, parent, false);
        sceneAdapter.HolderMakeScene holderMakeScene = new sceneAdapter.HolderMakeScene(view);
        return holderMakeScene;
    }

    @Override
    public void onBindViewHolder(@NonNull sceneAdapter.HolderMakeScene holder, @SuppressLint("RecyclerView") final int position) {
        scenario scenario= sceneList.get(position);
        holder.make_scene_name.setText(scenario.getName());
        holder.make_scene_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (View container:viewList) {
                    container.findViewById(R.id.make_scene_checked).setVisibility(View.GONE);
                }
                holder.make_scene_checked.setVisibility(View.VISIBLE);
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

        ImageView make_scene_checked;
        RelativeLayout make_scene_container;
        TextView make_scene_description;
        MotionEvent event;

        public HolderMakeScene(View itemView) {
            super(itemView);
            viewList.add(itemView);
            make_scene_name = (TextView) itemView.findViewById(R.id.make_scene_name);
            make_scene_description= (TextView) itemView.findViewById(R.id.make_scene_description);
            make_scene_container = (RelativeLayout) itemView.findViewById(R.id.make_scene_container);
            make_scene_checked= (ImageView) itemView.findViewById(R.id.make_scene_checked);
        }

    }
    public interface OnMakeItemEvent {
        void onItemClick(int position);
    }

}