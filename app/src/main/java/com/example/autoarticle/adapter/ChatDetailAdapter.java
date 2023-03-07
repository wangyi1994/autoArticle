package com.example.autoarticle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;
import com.example.autoarticle.activity.TalkActivity;
import com.example.autoarticle.command.C;
import com.example.autoarticle.model.ChatMessage;
import com.example.autoarticle.model.character;

import java.util.ArrayList;
import java.util.List;

import static com.example.autoarticle.config.config.USER;

/**
 * @CreateDate: 2018/1/26
 * @Author: lzsheng
 * @Description: 适配器，根据不同的数据类型，展示不同的UI效果
 * @Version:
 */
public class ChatDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TalkActivity activity;
    private List<ChatMessage> mChatMessages;
    private OnRecyclerViewItemEvent mOnRecyclerViewItemEvent;

    private final int TYPE_MSG_SEND = C.TYPE_MSG_SEND;
    private final int TYPE_MSG_RECEIVE = C.TYPE_MSG_RECEIVE;

    private List<View> viewList=new ArrayList<>();
    private List<Button> buttonList=new ArrayList<>();


    public ChatDetailAdapter(Context context) {
        this.activity = (TalkActivity) context;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        mChatMessages = chatMessages;
    }

    public void setOnRecyclerViewItemLongClick(OnRecyclerViewItemEvent onRecyclerViewItemEvent) {
        mOnRecyclerViewItemEvent = onRecyclerViewItemEvent;
    }

    public void resetButton(int position) {
        if (viewList == null || viewList.size() == 0 || viewList.get(position) == null) {
            return;
        }
        Log.d("wangyi", "resetButton 1" );
        Button button=buttonList.get(position);
        button.setBackground(activity.getResources().getDrawable(R.mipmap.speeched));
    }
    public void setButton(int position) {
        if (viewList == null || viewList.size() == 0 || viewList.get(position) == null) {
            return;
        }
        Log.d("wangyi", "setButton position:"+position );
        Button button=viewList.get(position).findViewById(R.id.speeched);
        button.setBackground(activity.getResources().getDrawable(R.mipmap.pause));
    }


    public void resetButton() {
        for (View view : viewList) {
            Button button=view.findViewById(R.id.speeched);
            button.setBackground(activity.getResources().getDrawable(R.mipmap.speeched));
        }
    }
    public void translate(int position,String content){
        LinearLayout translateContainer=viewList.get(position).findViewById(R.id.layout_message_translate);
        TextView textview_message_translate=viewList.get(position).findViewById(R.id.textview_message_translate);
        if(content!=null){
            textview_message_translate.setText(content);
        }
        translateContainer.setVisibility(View.VISIBLE);

    }

    /**
     * @CreateDate: 2018/2/3
     * @Author: lzsheng
     * @Description: 根据数据的类型, 返回不同的ItemViewType
     * @Params: [position]
     * @Return: int
     */
    @Override
    public int getItemViewType(int position) {
        if (mChatMessages != null && mChatMessages.size() > 0) {
            String role = mChatMessages.get(position).getRole();
            if (role.equals(USER)) {
                return TYPE_MSG_SEND;
            } else  {
                return TYPE_MSG_RECEIVE;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_MSG_SEND:
                HolderChatSend holderChatSend = new HolderChatSend(
                        LayoutInflater.from(activity).inflate(R.layout.rv_item_chat_msg_send, parent, false));
                return holderChatSend;
            case TYPE_MSG_RECEIVE:
                HolderChatReceive holderChatReceive = new HolderChatReceive(
                        LayoutInflater.from(activity).inflate(R.layout.rv_item_chat_msg_receive, parent, false));
                return holderChatReceive;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mChatMessages != null) {
            ChatMessage chatMessage = mChatMessages.get(position);
            int from = chatMessage.getRole().equals(USER)?TYPE_MSG_SEND:TYPE_MSG_RECEIVE;
            switch (from) {
                case TYPE_MSG_SEND:
                    String msgContentSend = chatMessage.getText();
                    ((HolderChatSend) holder).tvMsgContent.setText(msgContentSend);
                    if (!TextUtils.isEmpty(chatMessage.getCorrectMsg())) {
                        String correctMsg = chatMessage.getCorrectMsg();
                        ((HolderChatSend) holder).textview_message_correct.setText(correctMsg);
                        ((HolderChatSend) holder).layout_message_correct.setVisibility(View.VISIBLE);
                    } else {
                        ((HolderChatSend) holder).layout_message_correct.setVisibility(View.GONE);
                    }

                    break;
                case TYPE_MSG_RECEIVE:
                    ((HolderChatReceive) holder).tvNickName.setText(chatMessage.getRole());
                    String msgContentReceive = chatMessage.getText();
                    ((HolderChatReceive) holder).tvMsgContent.setText(msgContentReceive);
                    // 使用ClickableSpan的文本如果想真正实现点击作用，必须为TextView设置setMovementMethod方法，
                    // 否则没有点击相应，至于setHighlightColor方法则是控制点击是的背景色。
//                    ((HolderChatReceive) holder).tvPraise.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
//                    ((HolderChatReceive) holder).tvPraise.setHighlightColor(mContext.getResources().getColor(R.color.colorPrimary));


                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mChatMessages != null && mChatMessages.size() > 0) {
            return mChatMessages.size();
        }
        return 0;
    }

    class HolderChatSend extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        TextView tvMsgContent;
        TextView textview_message_correct;
        RelativeLayout layoutChat;

        LinearLayout layout_message_correct;
        Button speeched;
        MotionEvent event;

        public HolderChatSend(View itemView) {
            super(itemView);
            viewList.add(itemView);
            tvMsgContent = (TextView) itemView.findViewById(R.id.textview_message);
            layout_message_correct = (LinearLayout) itemView.findViewById(R.id.layout_message_correct);
            textview_message_correct = (TextView) itemView.findViewById(R.id.textview_message_correct);
            layoutChat = (RelativeLayout) itemView.findViewById(R.id.layout_message);
            speeched = itemView.findViewById(R.id.speeched);
            buttonList.add(speeched);
            layoutChat.setOnTouchListener(new View.OnTouchListener() {
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

            speeched.setOnClickListener(this);
            layoutChat.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnRecyclerViewItemEvent != null) {
                mOnRecyclerViewItemEvent.onItemSpeechClick(view, event, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnRecyclerViewItemEvent != null) {
                mOnRecyclerViewItemEvent.onItemLongClick(v, event, getAdapterPosition());
            }
            return false;
        }
    }

    class HolderChatReceive extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        TextView tvNickName;
        TextView tvMsgContent;
        LinearLayout layoutChat;

        Button speeched;
        MotionEvent event;

        public HolderChatReceive(View itemView) {
            super(itemView);
            viewList.add(itemView);
            tvMsgContent = (TextView) itemView.findViewById(R.id.textview_message);
            layoutChat = (LinearLayout) itemView.findViewById(R.id.layout_message);
            tvNickName = (TextView) itemView.findViewById(R.id.textview_message);
            speeched = itemView.findViewById(R.id.speeched);
            buttonList.add(speeched);
            layoutChat.setOnTouchListener(new View.OnTouchListener() {
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
            layoutChat.setOnLongClickListener(this);
            speeched.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnRecyclerViewItemEvent != null) {
                mOnRecyclerViewItemEvent.onItemSpeechClick(view, event, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnRecyclerViewItemEvent != null) {
                mOnRecyclerViewItemEvent.onItemLongClick(v, event, getAdapterPosition());
            }
            return false;
        }
    }

    /**
     * item点击接口
     */
    public interface OnRecyclerViewItemEvent {
        void onItemLongClick(View childView, MotionEvent event, int position);

        void onItemSpeechClick(View childView, MotionEvent event, int position);
    }


}
