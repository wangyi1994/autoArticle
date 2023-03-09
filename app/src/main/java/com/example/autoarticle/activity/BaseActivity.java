package com.example.autoarticle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.example.autoarticle.R;
import com.example.autoarticle.utils.L;
import com.example.autoarticle.utils.LoadingView;

public class BaseActivity extends Activity {
    private String TAG=this.getClass().getSimpleName();

    /*********************************************dialog****************************************/
    /**
     * 关闭加载对话框
     */
    public void disMissLoadingDialog() {
        L.i(TAG, "disMissLoadingDialog");
        if (loadingDialog != null && loadingDialog.isShowing()) {
            if (loadingView != null) {
                loadingView.stopAnimator();
            }
            loadingDialog.dismiss();
        }
    }
    /**
     * 显示默认加载消息对话框，（如果已经显示了就显示已经显示的对话框）
     */
    public void showLoadingDialog() {
        L.i(TAG, "showLoadingDialog");
        showLoadingDialog(null, null);
    }


    /**
     * 显示默认加载消息对话框，（如果已经显示了就显示已经显示的对话框）
     *
     * @param onCancelListener 对话框关闭监听器
     * @param onKeyListener    对话框按键监听器
     */
    public void showLoadingDialog(DialogInterface.OnCancelListener onCancelListener,
                                  DialogInterface.OnKeyListener onKeyListener) {
        L.i(TAG, "showLoading");
        if (loadingDialog != null && loadingDialog.isShowing()) {
            if (onCancelListener != null) {
                loadingDialog.setOnCancelListener(onCancelListener);
            }
            if (onKeyListener != null) {
                loadingDialog.setOnKeyListener(onKeyListener);
            }
            return;
        }
        try {
            LayoutInflater inflater = null;
            if (loadingDialog == null) {
                inflater = LayoutInflater.from(this);
                loadingDialog = new AlertDialog.Builder(this).create();
                loadingDialog.getContext().setTheme(R.style.dialogWithoutBG);
                loadingDialog.show();
                loadingDialog.setCancelable(false);
                View view = inflater.inflate(R.layout.dialog_loading, null);
                loadingView = (LoadingView) view.findViewById(R.id.view_loading);
                loadingView.startAnimator();
                loadingDialog.setContentView(view);
                loadingDialog.setCancelable(true);
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (loadingView != null) {
                            loadingView.stopAnimator();
                            loadingView.clearAnimation();
                        }
                    }
                });
            } else {
                loadingDialog.show();
                if (loadingView != null) {
                    loadingView.startAnimator();
                }
            }

            if (onCancelListener != null) {
                loadingDialog.setOnCancelListener(onCancelListener);
            }
            if (onKeyListener != null) {
                loadingDialog.setOnKeyListener(onKeyListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭加载对话框
     */
    protected Dialog loadingDialog;

    protected LoadingView loadingView;
}
