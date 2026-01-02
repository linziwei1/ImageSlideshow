package com.slideshow.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机自启动广播接收器
 * 监听系统开机完成广播，自动启动MainActivity
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // 检查是否是开机完成广播
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(action)) {

            // 启动主Activity
            Intent launchIntent = new Intent(context, MainActivity.class);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(launchIntent);
        }
    }
}
