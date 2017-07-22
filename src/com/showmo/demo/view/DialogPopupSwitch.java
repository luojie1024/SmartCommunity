package com.showmo.demo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.way.tabui.gokit.R;
import com.way.util.CurtainControlUtils;

import razerdp.basepopup.BasePopupWindow;

/**
 * Auther：joahluo
 * E-mail：joahluo@163.com
 * Time：2017/7/22 10:03
 */
public class DialogPopupSwitch extends BasePopupWindow implements View.OnClickListener{

          private int type;
          private Context context;
          private TextView ok;
          private TextView cancel;
          private LinearLayout ll_switch1,ll_switch2,ll_switch3,ll_switch4;
          private SwitchButton switch_btn1,switch_btn2,switch_btn3,switch_btn4;
          //UI更新广播
          public static final String switch_action = "com.device.control.switch.action";

          private Intent intent;

          public DialogPopupSwitch(Activity context,int type) {
                    super(context);
                    this.context=context;
                    this.type=type;
                    intent = new Intent(switch_action);
                    ok= (TextView) findViewById(R.id.ok);
                    cancel= (TextView) findViewById(R.id.cancel);
                    switch_btn1=(SwitchButton) findViewById(R.id.switch1_button);
                    switch_btn2=(SwitchButton) findViewById(R.id.switch2_button);
                    switch_btn3=(SwitchButton) findViewById(R.id.switch3_button);
                    switch_btn4=(SwitchButton) findViewById(R.id.switch4_button);
                    ll_switch1=(LinearLayout) findViewById(R.id.ll_switch1);
                    ll_switch2=(LinearLayout) findViewById(R.id.ll_switch2);
                    ll_switch3=(LinearLayout) findViewById(R.id.ll_switch3);
                    ll_switch4=(LinearLayout) findViewById(R.id.ll_switch4);
                    initSwitchView();
                    initEvent();
          }

          //根据开关类型隐藏布局
          private void initSwitchView() {
                    switch (this.type) {
                              case 1:
                                        ll_switch2.setVisibility(View.GONE);
                              case 2:
                                        ll_switch3.setVisibility(View.GONE);
                              case 3:
                                        ll_switch4.setVisibility(View.GONE);
                                        break;
                              case 4:
                                        ll_switch1.setVisibility(View.GONE);
                                        ll_switch2.setVisibility(View.GONE);
                                        ll_switch3.setVisibility(View.GONE);
                                        break;
                    }
          }

          //开关响应事件
          private void initEvent() {
                    switch_btn1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                        intent.putExtra("isChecked",isChecked);
                                        intent.putExtra("switch",1);
                                        context.sendBroadcast(intent);
                              }
                    });
                    switch_btn2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                        intent.putExtra("isChecked",isChecked);
                                        intent.putExtra("switch",2);
                                        context.sendBroadcast(intent);
                              }
                    });
                    switch_btn3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                        intent.putExtra("isChecked",isChecked);
                                        intent.putExtra("switch",3);
                                        context.sendBroadcast(intent);
                              }
                    });
                    switch_btn4.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                        intent.putExtra("isChecked",isChecked);
                                        intent.putExtra("switch",4);
                                        context.sendBroadcast(intent);
                              }
                    });
          }

          @Override
          protected Animation initShowAnimation() {
                    AnimationSet set=new AnimationSet(false);
                    Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    shakeAnima.setInterpolator(new CycleInterpolator(5));
                    shakeAnima.setDuration(400);
                    set.addAnimation(getDefaultAlphaAnimation());
                    set.addAnimation(shakeAnima);
                    return set;
          }

          @Override
          public View getClickToDismissView() {
                    return getPopupWindowView();
          }

          @Override
          public View onCreatePopupView() {
                    return createPopupById(R.layout.popupwindow_device_control_switch);
          }

          @Override
          public View initAnimaView() {
                    return findViewById(R.id.popup_anima);
          }

          @Override
          public void onClick(View v) {
                    switch (v.getId()){
                              case R.id.ok:
                                        break;
                              case R.id.cancel:
                                        break;
                              case R.id.switch1_button:
                                        intent.putExtra("control",CurtainControlUtils.CLOSE);
                                        context.sendBroadcast(intent);
                                        break;
                              case R.id.switch2_button:
                                        intent.putExtra("control", CurtainControlUtils.OPEN);
                                        context.sendBroadcast(intent);
                                        break;
                              case R.id.switch3_button:
                                        intent.putExtra("control",CurtainControlUtils.STOP);
                                        context.sendBroadcast(intent);
                                        break;
                              case R.id.switch4_button:
                                        intent.putExtra("control",CurtainControlUtils.REDIC);
                                        context.sendBroadcast(intent);
                                        break;
                              default:
                                        break;
                    }

          }
}
