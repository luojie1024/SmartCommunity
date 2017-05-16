package com.way.tabui.gokit;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;

import com.way.tabui.commonmodule.GosBaseActivity;

public class SmartDoorActivity extends GosBaseActivity {

    boolean isunlock=false;
    private ImageButton ib_unclock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_door);
        setActionBar(true, true, getResources().getString(R.string.title_activity_smart_door));
        initView();

    }
    private void initView(){
        ib_unclock = (ImageButton) findViewById(R.id.ib_unclock);
    }
    public void simpe(){
      Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }
    public void doorClick(View view){
        simpe();
        if (isunlock){
            isunlock=false;
            ib_unclock.setImageResource(R.drawable.unlock_normal);
        }else{
            isunlock=true;
            ib_unclock.setImageResource(R.drawable.unlocked_normal);
        }
    }
}
