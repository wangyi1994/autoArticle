package com.example.autoarticle.config;

import android.os.Environment;

import java.io.File;

public class config {
    public static String speechSubscriptionKey = "9e2d5f6836834144b17130c815ae2373";
    // Replace below with your own service region (e.g., "westus").
    public static String serviceRegion = "japanwest";

    public static String IP = "https://www.oralpractice.site:5678";

    public static String TALK_ITEM = "talk_item";
    /**
     * 本地文件根目录名称
     */
    private static final String ROOT_DIRNAME = "amt.wangyi.auto";
    /**
     * 内存卡根目录
     */
    public final static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
//    public final static String SDPATH = "data"+File.separator +"data";
    /**
     * 本地文件根目录
     */
    public final static String ROOT_PATH = SDPATH + File.separator + ROOT_DIRNAME;
}
