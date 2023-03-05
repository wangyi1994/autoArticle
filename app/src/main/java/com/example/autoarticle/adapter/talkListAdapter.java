package com.example.autoarticle.adapter;

import static com.example.autoarticle.config.config.TALK_ITEM;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;
import com.example.autoarticle.activity.TalkActivity;
import com.example.autoarticle.model.conversation;

import java.util.List;

public class talkListAdapter extends RecyclerView.Adapter<talkListAdapter.HolderMainTalkItem>{
    private Context context;
    private List<conversation> conversations;

    public talkListAdapter(Context context, List<conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public HolderMainTalkItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_main_talk_list, parent, false);
        HolderMainTalkItem holderMainTalkItem = new HolderMainTalkItem(view);
        return holderMainTalkItem;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMainTalkItem holder, int position) {
        conversation bean= conversations.get(position);
        String name=bean.getScenario().getName();
        holder.main_talk_name.setText(name);
        holder.main_talk_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TalkActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(TALK_ITEM,bean);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class HolderMainTalkItem extends RecyclerView.ViewHolder  {

        ImageView main_talk_portrait;
        TextView main_talk_name;
        FrameLayout main_talk_container;
        MotionEvent event;

        public HolderMainTalkItem(View itemView) {
            super(itemView);
            main_talk_portrait = (ImageView) itemView.findViewById(R.id.main_talk_portrait);
            main_talk_name = (TextView) itemView.findViewById(R.id.main_talk_name);
            main_talk_container = (FrameLayout) itemView.findViewById(R.id.main_talk_container);
            main_talk_container.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent e) {
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            event = e;
                            break;
                        default:
                            break;
                    }
                    // 如果onTouch返回false,首先是onTouch事件的down事件发生，此时，如果长按，触发onLongClick事件；
                    // 然后是onTouch事件的up事件发生，up完毕，最后触发onClick事件。
                    return false;
                }
            });

        }

    }
    public interface OnTalkItemEvent {
        void onItemClick(int position);
    }

}
