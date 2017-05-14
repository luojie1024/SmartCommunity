package com.way.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ToastUtil {
    public static void ToastShow(final Activity activity, final String showText){
        activity.runOnUiThread(new Runnable() {
           // @Override
            public void run() {
                Toast.makeText(activity,showText,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
