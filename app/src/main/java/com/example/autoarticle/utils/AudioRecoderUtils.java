package com.example.autoarticle.utils;

/**
 * @author wy
 * @date 2023/2/14
 * description:
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.example.autoarticle.NetWork.RetrofitManager;
import com.example.autoarticle.config.config;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class AudioRecoderUtils {

    private static AudioRecoderUtils mInstance;

    public static AudioRecoderUtils getInstance() {
        if (mInstance == null) {
            synchronized (AudioRecoderUtils.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecoderUtils();
                }
            }
        }
        return mInstance;
    }

    private Context mContext;

    //文件路径
    private String filePath;
    //文件夹路径
    private String FolderPath;

    private MediaRecorder mMediaRecorder;

    private MediaPlayer player;
    private final String TAG = "fan";
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    /**
     * 文件存储默认sdcard/record
     */
    public void init(Context context) {
        this.mContext = context;
        //默认保存路径为/sdcard/record/下
        String filePath = config.ROOT_PATH;
        File path = new File(config.ROOT_PATH);
        if (!path.exists())
            path.mkdirs();

        this.FolderPath = config.ROOT_PATH;

    }


    private long startTime;
    private long endTime;


    /**
     * 开始录音 使用wav格式
     *      录音文件
     * @return
     */
    public String startRecord() {
        // 开始录音

        int audioSource = MediaRecorder.AudioSource.MIC;

        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            filePath = mContext.getCacheDir() + "/" + +System.currentTimeMillis() + ".wav";
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.i("fan", "startTime" + startTime);
            return filePath;
        } catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } finally {
            return filePath;
        }
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();

        //有一些网友反应在5.0以上在调用stop的时候会报错，翻阅了一下谷歌文档发现上面确实写的有可能会报错的情况，捕获异常清理一下就行了，感谢大家反馈！
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            // audioStatusUpdateListener.onStop(filePath);
            filePath = "";

        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = "";

        }
        return endTime - startTime;
    }

    public boolean isPlaying(){
        if(player==null){
            return false;
        }
        return player.isPlaying();
    }


    /**
     * 播放录音
     * @param filepath
     */
    public MediaPlayer playRecord(String filepath, MediaPlayer.OnCompletionListener listener) {
        player = getMediaPlayer(mContext);
        if (player != null) {
            player.reset();
            try {
                //设置语言的来源
                player.setDataSource(filepath);
                //初始化
                player.prepare();
                //开始播放
                player.start();
                player.setOnCompletionListener(listener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return player;
    }

    public void pausePlay() {
        if (player != null) {
            try {
                //开始播放
                player.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPlay() {
        if (player != null) {
            try {
                //开始播放
                player.reset();
                player.stop();
                player=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void reset() {
        if (player != null) {
            try {
                //开始播放
                player.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {

        try {

            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        File file = new File(filePath);
        if (file.exists())
            file.delete();

        filePath = "";

    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {

        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         * @param db 当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db, long time);

        /**
         * 停止录音
         * @param filePath 保存路径
         */
        public void onStop(String filePath);

    }


    /**
     *  </br> This code is trying to do the following from the hidden API
     * </br> SubtitleController sc = new SubtitleController(context, null, null);
     * </br> sc.mHandler = new Handler();
     * </br> mediaplayer.setSubtitleAnchor(sc, null)</p>
     */
    private MediaPlayer getMediaPlayer(Context context) {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(
                    new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor",
                    cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            Log.d(TAG, "getMediaPlayer crash ,exception = " + e);
        }
        return mediaplayer;
    }


    // 录音状态
    private boolean isRecording = true;

    private AudioRecord audioRecord;

    public String startRecording() {
       final String path=mContext.getCacheDir() + "/" + +System.currentTimeMillis() + ".wav";
        // 耗时操作要开线程
        new Thread() {
            @Override
            public void run() {
                // 音源
                int audioSource = MediaRecorder.AudioSource.MIC;
                // 采样率
                int sampleRate = 44100;
                // 声道数
                int channelConfig = AudioFormat.CHANNEL_IN_STEREO;//双声道
                // 采样位数
                int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
                // 获取最小缓存区大小
                int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                // 创建录音对象
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                audioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, minBufferSize);
                try {
                    // 创建随机读写流
                    RandomAccessFile raf = new RandomAccessFile(path, "rw");
                    // 留出文件头的位置
                    raf.seek(44);
                    byte[] buffer = new byte[minBufferSize];

                    // 录音中
                    audioRecord.startRecording();
                    isRecording = true;
                    while (isRecording) {
                        int readSize = audioRecord.read(buffer, 0, minBufferSize);
                        raf.write(buffer,0,readSize);
                    }

                    // 录音停止
                    audioRecord.stop();
                    audioRecord.release();

                    // 写文件头
                    WriteWaveFileHeader(raf, raf.length(),sampleRate,2,sampleRate*16*2/8);
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }.start();
        return path;
    }

    /**
     * 为 wav 文件添加文件头，前提是在头部预留了 44字节空间
     *
     * @param raf
     *              随机读写流
     * @param fileLength
     *              文件总长
     * @param sampleRate
     *              采样率
     * @param channels
     *              声道数量
     * @param byteRate
     *              码率 = 采样率 * 采样位数 * 声道数 / 8
     * @throws IOException
     */
    private void WriteWaveFileHeader(RandomAccessFile raf, long fileLength, long sampleRate, int channels, long byteRate) throws IOException {
        long totalDataLen = fileLength + 36;
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (fileLength & 0xff);
        header[41] = (byte) ((fileLength >> 8) & 0xff);
        header[42] = (byte) ((fileLength >> 16) & 0xff);
        header[43] = (byte) ((fileLength >> 24) & 0xff);
        raf.seek(0);
        raf.write(header, 0, 44);
    }
    public void stopRecording() {
        // 停止录音
        isRecording = false;
    }


}