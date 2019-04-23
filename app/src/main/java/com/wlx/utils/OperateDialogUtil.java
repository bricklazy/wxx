package com.wlx.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.wlx.application.R;

public class OperateDialogUtil {

    private static OperateDialogUtil operateDialogUtil;

    public enum Type{
        DEL
    }

    public static OperateDialogUtil getIns(){
        if (operateDialogUtil==null){
            synchronized (OperateDialogUtil.class){
                if(operateDialogUtil==null){
                    operateDialogUtil = new OperateDialogUtil();
                }
            }
        }
        return operateDialogUtil;
    }

    private OperateDialogUtil(){}

    public void showDelDialog(Context mContext, String msg, DialogCallBack dialogCallBack){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_del_info, null);
        alertDialog.setView(dialogView);
        final AlertDialog dialog = alertDialog.create();
        TextView dialog_msg = dialogView.findViewById(R.id.dialog_msg);
        dialog_msg.setText(msg);
        TextView dialog_ok = dialogView.findViewById(R.id.dialog_ok);
        TextView dialog_cancel = dialogView.findViewById(R.id.dialog_cancel);
        dialog.setCancelable(false);
        dialog_ok.setOnClickListener(v1 -> {
            dialog.dismiss();
            dialogCallBack.ok();
        });
        dialog_cancel.setOnClickListener(v1 -> {
            dialog.dismiss();
            dialogCallBack.cancel();
        });
        dialog.show();
    }

    public void showDelDialog(Context mContext, DialogCallBack dialogCallBack){
        showDelDialog(mContext, "确定要执行此操作？", dialogCallBack);
    }

    public interface DialogCallBack{
        void ok();
        void cancel();
    }

    public interface PopupCallBack{
        void click(Type type);
    }

}
