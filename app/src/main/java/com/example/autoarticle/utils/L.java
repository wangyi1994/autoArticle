package com.example.autoarticle.utils;

import android.util.Log;

/**
 * 日志打印工具类，代码中统一使用该工具打印日志，编译调试
 */
public class L {
    //是否需要打印日志
    private static boolean isDebug = true;
    private final static String TAG = "GameStore";
    /**
     * 最低级输出类型
     */
    private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息

    public static void v(String tag, String msg) {
        log(tag, msg != null ? msg.toString() : "null", 'v');
    }

    public static void v(String msg) {
        log(TAG, msg != null ? msg.toString() : "null", 'v');
    }

    public static void d(String tag, String msg) {
        log(tag, msg != null ? msg.toString() : "null", 'd');
    }

    public static void d(String msg) {
        log(TAG, msg != null ? msg.toString() : "null", 'd');
    }

    public static void i(String tag, String msg) {
        log(tag, msg != null ? msg.toString() : "null", 'i');
    }

    public static void i(String msg) {
        log(TAG, msg != null ? msg.toString() : "null", 'i');
    }

    public static void w(String tag, String msg) {
        log(tag, msg != null ? msg.toString() : "null", 'w');
    }

    public static void w(String msg) {
        log(TAG, msg != null ? msg.toString() : "null", 'w');
    }

    public static void e(String tag, String msg) {
        log(tag, msg != null ? msg.toString() : "null", 'e');
    }

    public static void e(String msg) {
        log(TAG, msg != null ? msg.toString() : "null", 'e');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag   TAG
     * @param msg   内容
     * @param level 级别
     */
    private static void log(String tag, String msg, char level) {
        if (isDebug) {
            if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE || 'd' == MYLOG_TYPE || 'i' == MYLOG_TYPE)) {
                Log.w(tag, msg);
            } else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.d(tag, msg);
            } else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE || 'i' == MYLOG_TYPE)) {
                Log.i(tag, msg);
            } else if ('v' == level && ('v' == MYLOG_TYPE)) {
                Log.v(tag, msg);
            } else {
                Log.e(tag, msg);
            }
//            if (isWrite) {
//                writeLogtoFile(String.valueOf(level), tag, msg);
//            }
        }
    }

//    /**
//     * 打开日志文件并写入日志
//     *
//     * @param mylogtype 日志级别
//     * @param tag       tag
//     * @param text      内容
//     */
//    private static void writeLogtoFile(final String mylogtype, final String tag, final String text) {
//       TaskManager.getInstance().executeSeries(new RunnablePriority() {
//            @Override
//            public void run() {
//                StringBuilder builder = new StringBuilder(mylogtype);
//                builder.append("    " + tag);
//                builder.append("    " + text + "\n");
//                File file = new File(Config.FILENAME_log);
//                FileManager.writeStringToFile(builder.toString(),file);
//            }
//        });
//    }

}
