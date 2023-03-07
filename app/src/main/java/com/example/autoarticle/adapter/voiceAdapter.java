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
import com.example.autoarticle.model.character;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class voiceAdapter extends RecyclerView.Adapter<voiceAdapter.HolderMakevoice>{
    private Context context;
    private List<character> voiceList;


    /**
     *  暂存每个item 的view
     */
    private List<View> viewList;

    public voiceAdapter(Context context, List<character> voiceList) {
        this.context = context;
        this.voiceList=voiceList;
        viewList=new ArrayList<>();
    }

    @NonNull
    @Override
    public voiceAdapter.HolderMakevoice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_make_voice, parent, false);
        voiceAdapter.HolderMakevoice holderMakevoice = new voiceAdapter.HolderMakevoice(view);
        return holderMakevoice;
    }

    @Override
    public void onBindViewHolder(@NonNull voiceAdapter.HolderMakevoice holder, @SuppressLint("RecyclerView") final int position) {
        character character= voiceList.get(position);
        holder.make_voice_name.setText(character.getName());
        holder.make_voice_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return voiceList.size();
    }

    class HolderMakevoice extends RecyclerView.ViewHolder  {

        TextView make_voice_name;
        RelativeLayout make_voice_container;
        TextView make_voice_description;
        MotionEvent event;

        public HolderMakevoice(View itemView) {
            super(itemView);
            viewList.add(itemView);
            make_voice_name = (TextView) itemView.findViewById(R.id.make_voice_name);
            make_voice_description= (TextView) itemView.findViewById(R.id.make_voice_description);
            make_voice_container = (RelativeLayout) itemView.findViewById(R.id.make_voice_container);
        }

    }
    public interface OnMakeItemEvent {
        void onItemClick(int position);
    }

}