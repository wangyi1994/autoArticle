package com.example.autoarticle.activity;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

import static com.example.autoarticle.command.C.TYPE_MSG_RECEIVE;
import static com.example.autoarticle.command.C.TYPE_MSG_SEND;
import static com.example.autoarticle.config.config.TALK_ITEM;
import static com.example.autoarticle.config.config.USER;
import static com.example.autoarticle.config.config.serviceRegion;
import static com.example.autoarticle.config.config.speechSubscriptionKey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.NetWork.RetrofitManager;
import com.example.autoarticle.R;
import com.example.autoarticle.adapter.ChatDetailAdapter;
import com.example.autoarticle.NetWork.requests;
import com.example.autoarticle.adapter.OptionAdapter;
import com.example.autoarticle.listener.OnRecyclerViewItemClick;
import com.example.autoarticle.model.ChatMessage;
import com.example.autoarticle.model.DataCenter;
import com.example.autoarticle.model.OptionEntity;
import com.example.autoarticle.model.OralChatBean;
import com.example.autoarticle.model.character;
import com.example.autoarticle.model.resultBean;
import com.example.autoarticle.model.conversation;
import com.example.autoarticle.utils.AudioRecoderUtils;
import com.example.autoarticle.utils.DensityUtil;
import com.example.autoarticle.utils.L;
import com.example.autoarticle.utils.LoadingView;
import com.google.gson.Gson;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * @CreateDate: 2022/02/10
 * @Author: wy
 * @Description:
 * @Version:
 */
public class TalkActivity extends BaseActivity implements View.OnClickListener {


    private conversation conversation;
    private RecyclerView mRecyclerView;
    private TextView teacher_name;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatDetailAdapter mChatDetailAdapter;
    private List<ChatMessage> mChatMessages;
    private ImageView back;
    private float mRawX;
    private float mRawY;

    private Button speech;

    private Button inspire;

    private int requestCode = 5;

    private Retrofit retrofit;

    private String TAG = TalkActivity.class.getSimpleName();


    /**
     * ?????????????????????????????????????????????position
     */
    public int playPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        Intent intent = getIntent();
        if (intent != null) {
            conversation = (conversation) intent.getSerializableExtra(TALK_ITEM);
            if (conversation != null) {
                mChatMessages = conversation.getMessages();
            }
        }
        // Initialize SpeechSDK and request required permissions.
        try {
            // a unique number within the application to allow
            // correlating permission request responses with the request.
            int permissionRequestId = 5;

            // Request permissions needed for speech recognition
            ActivityCompat.requestPermissions(TalkActivity.this, new String[]{RECORD_AUDIO, READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE, INTERNET}, permissionRequestId);
        } catch (Exception ex) {
            Log.e("SpeechSDK", "could not init sdk, " + ex.toString());
        }
        init();

        initViews();
        setViewsData();
        initRetrofit();
        initVedio();

    }

    SpeechConfig config;
    SpeechRecognizer reco;
    AudioConfig audioConfig;

    private void init() {
        audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        String key="";
        String regin="";
        if(DataCenter.getInstance().getInitBean()!=null&&DataCenter.getInstance().getInitBean().getSpeech_api()!=null){
             key=DataCenter.getInstance().getInitBean().getSpeech_api().getSpeech_key();
             regin=DataCenter.getInstance().getInitBean().getSpeech_api().getService_region();
        }

        config = SpeechConfig.fromSubscription(TextUtils.isEmpty(key)?speechSubscriptionKey:key,
                TextUtils.isEmpty(regin)?serviceRegion:regin);
        // reco = new SpeechRecognizer(config);
    }

    private void initRetrofit() {
        retrofit = RetrofitManager.getInstance().getRetrofit();
    }

    private void onInspireButtonClicked() {
        getResult("true",true);
    }

    public void onSpeechButtonClicked(boolean isRecording) {
        if (isRecording) {
            speech.setText(TalkActivity.this.getString(R.string.record));
            AudioRecoderUtils.getInstance().stopRecording();
            speech.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        AudioConfig audioConfig = AudioConfig.fromWavFileInput(recordingPath);
                        reco = new SpeechRecognizer(config, audioConfig);
                        Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();

                        SpeechRecognitionResult result = task.get();

                        if (result.getReason() == ResultReason.RecognizedSpeech) {
                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setRole(USER);
                            chatMessage.setText(result.getText());
                            chatMessage.setFilePath(recordingPath);
                            mChatMessages.add(chatMessage);
                            TalkActivity.this.runOnUiThread(() -> runOnUiThread(() -> {
                                speech.setText(TalkActivity.this.getString(R.string.record));
                                mChatDetailAdapter.notifyItemInserted(mChatMessages.size()-1);
                                mRecyclerView.scrollToPosition(mChatMessages.size() - 1);
                            }));
                            getResult("false",true);
                        } else {
                            TalkActivity.this.runOnUiThread(() -> runOnUiThread(() -> {
                                speech.setText(TalkActivity.this.getString(R.string.record));
                                Toast.makeText(TalkActivity.this, "Error recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString()
                                        , Toast.LENGTH_SHORT).show();
                            }));
                        }
                    } catch (Exception ex) {
                        Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
                    }
                }
            }, 300);
        } else {
            AudioRecoderUtils.getInstance().stopPlay();
            speech.setText(TalkActivity.this.getString(R.string.recording));
            recordingPath = AudioRecoderUtils.getInstance().startRecording();
        }

        // Note: this will block the UI thread, so eventually, you want to
        //       register for the event (see full samples)


    }

    @Override
    protected void onDestroy() {

        // in the end, recognizer need to close
        if (reco != null)
            reco.close();
        AudioRecoderUtils.getInstance().stopPlay();
        super.onDestroy();

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String getResult(String isInspire,boolean showLoading) {
        if(showLoading){
            showLoadingDialog();
        }

        List<ChatMessage> talkBeans = new ArrayList<>();
        for (int i = mChatMessages.size() - 1; i >= 0; i--) {
            if (talkBeans.size() == 5) {
                break;
            }
            talkBeans.add(mChatMessages.get(i));
        }
        Collections.reverse(talkBeans);
        Gson gson = new Gson();
        OralChatBean bean = new OralChatBean();
        bean.setUser(DataCenter.getInstance().getUser());
        bean.setMessages(talkBeans);
        bean.setConversation_id(conversation.getConversation_id());
        bean.setScenario(conversation.getScenario());
        character character1 = conversation.getCharacter();
        bean.setCharacter(character1);
        bean.setInspire_me(isInspire);
        String oralChat = gson.toJson(bean);

        RequestBody requestBody = MultipartBody.create(JSON, oralChat);
        requests request = retrofit.create(requests.class);
        Call<ResponseBody> responseBodyCall = request.getResult(requestBody);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    disMissLoadingDialog();
                    if (response == null || response.body() == null) {
                        return;
                    }
                    String RESULT = response.body().string().trim();
                    Log.i("test api", "response.body:" + RESULT);
                    resultBean result = new Gson().fromJson(RESULT, resultBean.class);
                    ChatMessage chatMessage = new ChatMessage();
                    if (isInspire.equals("true")) {
                        chatMessage.setRole(USER);
                        chatMessage.setText(result.getUser_text());
                        mChatMessages.add(chatMessage);
                        mChatDetailAdapter.notifyItemInserted(mChatMessages.size());
                        mRecyclerView.scrollToPosition(mChatMessages.size() - 1);
                        getResult("false",false);
                    } else {
                        chatMessage.setRole(conversation.getCharacter().getName());
                        chatMessage.setText(result.getAi_text());
                        mChatMessages.get(mChatMessages.size() - 1).setCorrectMsg(result.getCorrected_user_text());
                        mChatDetailAdapter.notifyItemChanged(mChatMessages.size() - 1);
                        try {
                            mChatMessages.add(chatMessage);
                            mChatDetailAdapter.notifyItemInserted(mChatMessages.size());
                            mRecyclerView.scrollToPosition(mChatMessages.size() - 1);
                        } catch (Exception exp) {

                        }
                        speek(chatMessage);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    disMissLoadingDialog();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("test api", "t.body:" + t.getMessage());
                disMissLoadingDialog();
            }
        });
        return "";
    }


    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_messages);
        teacher_name = findViewById(R.id.teacher_name);
        back = findViewById(R.id.talk_back);
        if (conversation != null && conversation.getCharacter() != null) {
            teacher_name.setText(conversation.getCharacter().getName());
        }
        mLinearLayoutManager = new LinearLayoutManager(TalkActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        speech = findViewById(R.id.speech);
        inspire = findViewById(R.id.inspire);

        back.setOnClickListener(this);
        speech.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == ACTION_DOWN) {
                    onSpeechButtonClicked(false);
                } else if (motionEvent.getAction() == ACTION_UP) {
                    onSpeechButtonClicked(true);
                }
                return false;
            }
        });
        inspire.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inspire:
                onInspireButtonClicked();
                break;
            case R.id.talk_back:
                TalkActivity.this.finish();
                break;

        }
    }


    private String recordingPath = "";

    //???????????? ?????????
    private void initVedio() {
        AudioRecoderUtils.getInstance().init(this);
    }

    private void speek(ChatMessage chatMessage) {
        if (TextUtils.isEmpty(chatMessage.getFilePath())) {
            try {
                String path = getCacheDir() + "/" + +System.currentTimeMillis() + ".wav";
                AudioConfig audioConfig = AudioConfig.fromWavFileOutput(path);
                SpeechSynthesizer synthesizer = new SpeechSynthesizer(config, audioConfig);
                synthesizer.SpeakText(chatMessage.getText());
                chatMessage.setFilePath(path);
            } catch (Exception exp) {
                Log.i(TAG, "speek azure error :" + exp.getMessage());
            }
        }
        AudioRecoderUtils.getInstance().playRecord(chatMessage.getFilePath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mChatDetailAdapter.resetButton();
            }
        });
    }

    private void setViewsData() {
        mChatDetailAdapter = new ChatDetailAdapter(TalkActivity.this);
        mChatDetailAdapter.setChatMessages(mChatMessages);
        mRecyclerView.setAdapter(mChatDetailAdapter);
//        mRecyclerView.smoothScrollToPosition(mChatDetailAdapter.getItemCount());
//        mLinearLayoutManager.scrollToPositionWithOffset(mChatDetailAdapter.getItemCount() + 1, 0);
        mRecyclerView.scrollToPosition(mChatDetailAdapter.getItemCount() - 1);
        mChatDetailAdapter.setOnRecyclerViewItemLongClick(new ChatDetailAdapter.OnRecyclerViewItemEvent() {
            @Override
            public void onItemSpeechClick(View childView, MotionEvent event, int position) {
                mChatDetailAdapter.resetButton();

                if (AudioRecoderUtils.getInstance().isPlaying()) {
                    if (playPosition == position) {
                        AudioRecoderUtils.getInstance().pausePlay();
                        return;
                    } else {
                        AudioRecoderUtils.getInstance().reset();
                    }
                }
                Log.d(TAG, "wangyi  click" );
                playPosition = position;
                mChatDetailAdapter.setButton(childView);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChatMessage chatMessage = mChatMessages.get(playPosition);
                        speek(chatMessage);
                        Log.d(TAG, "onItemSpeechClick chatMessage getMsgContent =" + chatMessage.getText());
                    }
                }, 500);


            }

            @Override
            public void onItemLongClick(View childView, MotionEvent event, int position) {
                ChatMessage chatMessage = mChatMessages.get(position);
                if (!chatMessage.getRole().equals(USER)) {
                    mRawX = event.getRawX();
                    mRawY = event.getRawY();
                    mPressedPos = position;
                    Log.d(TAG, "e.getRawX()?????????=" + mRawX + ", e.getRawY()?????????=" + mRawY);
                    Log.d(TAG, "position=" + position);
                    initPopWindow(childView, position);
                }
            }
        });
    }

    private int mPressedPos; // ?????????????????????????????????


    /**
     * ????????????view
     */
    private View mPopContentView;
    /**
     * ??????
     */
    private PopupWindow mPopupWindow;

    private void initPopWindow(final View selectedView, final int position) {

        List<OptionEntity> optionEntities = new ArrayList<>();
        optionEntities.add(new OptionEntity(0, null, "??????"));
        if (mPopContentView == null) {
            mPopContentView = View.inflate(this, R.layout.item_list_option_pop, null);
        }
        RecyclerView rvOptions = (RecyclerView) mPopContentView.findViewById(R.id.recyclerview_options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOptions.setLayoutManager(linearLayoutManager);
        OptionAdapter optionAdapter = new OptionAdapter(this);
        optionAdapter.setOptionEntities(optionEntities);
        optionAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int p) {
                if (p == 0) {
                    ChatMessage chatMessage = mChatMessages.get(position);
                    String msg = chatMessage.getText();
                    mChatDetailAdapter.translate(position, msg);
                    mPopupWindow.dismiss();
                }
            }
        });
        rvOptions.setAdapter(optionAdapter);
//        LinearLayout layoutDelete = (LinearLayout) mPopContentView.findViewById(R.id.layout_delete);
        // ???popupWindow???????????????????????????????????????????????????????????????px?????????
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mPopContentView.measure(w, h);
        int viewWidth = mPopContentView.getMeasuredWidth();//??????????????????px
        int viewHeight = mPopContentView.getMeasuredHeight();//??????????????????px
        final int screenWidth = DensityUtil.getScreenWidth(this.getWindow().getDecorView().getContext());
        final int screenHeight = DensityUtil.getScreenHeight(this.getWindow().getDecorView().getContext());
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mPopContentView, viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        int offX = 20; // ????????????????????????
        int offY = 20; // ????????????????????????
        float rawX = mRawX;
        float rawY = mRawY;
        if (mRawX <= screenWidth / 2) {
            rawX = mRawX + offX;
            if (mRawY < screenHeight / 3) {
                rawY = mRawY;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_left_top); //????????????
            } else {
                rawY = mRawY - viewHeight - offY;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_left_bottom); //????????????
            }
        } else {
            rawX = mRawX - viewWidth - offX;
            if (mRawY < screenHeight / 3) {
                rawY = mRawY;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_right_top); //????????????
            } else {
                rawY = mRawY - viewHeight;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_right_bottom); //????????????
            }
        }
        mPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.NO_GRAVITY, (int) rawX, (int) rawY);
        /*layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                if (mChatMessages.size() <= 0) {
                    return;
                } else {
                    mChatMessages.remove(position);
                    mChatDetailAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                selectedView.setSelected(false);
            }
        });
    }

}
