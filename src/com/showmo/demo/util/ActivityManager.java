package com.showmo.demo.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
public class ActivityManager {

    List<Activity> list;

    private static ActivityManager manager = new ActivityManager();
    private ActivityManager(){
        list = new ArrayList<Activity>();
    }
    public static ActivityManager getInstance(){
        return manager;
    }

    public void addActivity(Activity act){
        list.add(act);
    }

    public void quit(){
        for(int i=0;i<list.size();i++){
            list.get(i).finish();
        }
    }
}
