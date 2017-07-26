package com.showmo.demo.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
public class DialogPopupSwitch extends BasePopupWindow implements View.OnClickListener {

          private int type;
          private Context context;
          private TextView ok;
          private TextView cancel;
          private LinearLayout ll_switch1, ll_switch2, ll_switch3, ll_switch4;
          private SwitchButton switch_btn1, switch_btn2, switch_btn3, switch_btn4;
          //UI更新广播
          public static final String switch_action = "com.device.control.switch.action";

          private Intent intent;
          private DeviceStutasReceiver deviceStutasReceiver;
          //按钮状态
          private boolean checked1 = false, checked2 = false, checked3 = false, checked4 = false;
          //设备地址
          private String address;
          //flag防止开关状态更新触发数据发送
          private int flag_ui = 0;

          public DialogPopupSwitch(Activity context, int type, String address) {
                    super(context);
                    this.context = context;
                    this.type = type;
                    this.address = address;
                    intent = new Intent(switch_action);


          }

          //初始化
          public void init() {
                    initSwitchView();
                    initUI();
                    initEvent();
                    initReceiver();
          }

          //设置按钮状态
          public void setDeviceStutas(int stutas1, int stutas2, int stutas3) {
                    switch (type) {
                              case 3:
                                        checked3 = (stutas3 == 1 ? true : false);
                              case 2:
                                        checked2 = (stutas2 == 1 ? true : false);
                              case 1:
                                        checked1 = (stutas1 == 1 ? true : false);
                                        break;
                              case 4:
                                        checked4 = (stutas1 == 1 ? true : false);
                                        break;
                    }
          }

          //初始化UI
          private void initUI() {
                    switch_btn1.setChecked(checked1);
                    switch_btn2.setChecked(checked2);
                    switch_btn3.setChecked(checked3);
                    switch_btn4.setChecked(checked4);
          }

          //UI更新
          private void updateUI() {
                    initUI();
          }

          /**
           * description:设备状态接收
           * auther：joahluo
           * time：2017/7/24 10:22
           */
          private void initReceiver() {
                    deviceStutasReceiver = new DeviceStutasReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("com.device.status.update.action");
                    context.registerReceiver(deviceStutasReceiver, filter);
          }

          @Override
          public void onDismiss() {
                    super.onDismiss();
                    context.unregisterReceiver(deviceStutasReceiver);
          }


          //根据开关类型隐藏布局
          private void initSwitchView() {
                    ok = (TextView) findViewById(R.id.ok);
                    cancel = (TextView) findViewById(R.id.cancel);
                    switch_btn1 = (SwitchButton) findViewById(R.id.switch1_button);
                    switch_btn2 = (SwitchButton) findViewById(R.id.switch2_button);
                    switch_btn3 = (SwitchButton) findViewById(R.id.switch3_button);
                    switch_btn4 = (SwitchButton) findViewById(R.id.switch4_button);
                    ll_switch1 = (LinearLayout) findViewById(R.id.ll_switch1);
                    ll_switch2 = (LinearLayout) findViewById(R.id.ll_switch2);
                    ll_switch3 = (LinearLayout) findViewById(R.id.ll_switch3);
                    ll_switch4 = (LinearLayout) findViewById(R.id.ll_switch4);
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
                                        if (flag_ui == 0) {//非状态同步产生的变化才发送数据
                                                  intent.putExtra("isChecked", isChecked);
                                                  intent.putExtra("switch", 1);
                                                  context.sendBroadcast(intent);
                                        } else {
                                                  flag_ui--;
                                        }
                              }
                    });
                    switch_btn2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                        if (flag_ui == 0) {//非状态同步产生的变化才发送数据
                                                  intent.putExtra("isChecked", isChecked);
                                                  intent.putExtra("switch", 2);
                                                  context.sendBroadcast(intent);
                                        }else {
                                                  flag_ui--;
                                        }
                              }
                    });
                    switch_btn3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                        if (flag_ui == 0) {//非状态同步产生的变化才发送数据
                                                  intent.putExtra("isChecked", isChecked);
                                                  intent.putExtra("switch", 3);
                                                  context.sendBroadcast(intent);
                                        }else {
                                                  flag_ui--;
                                        }
                              }
                    });
                    switch_btn4.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                        if (flag_ui == 0) {//非状态同步产生的变化才发送数据
                                                  intent.putExtra("isChecked", isChecked);
                                                  intent.putExtra("switch", 4);
                                                  context.sendBroadcast(intent);
                                        }else {
                                                  flag_ui--;
                                        }
                              }
                    });
          }

          @Override
          protected Animation initShowAnimation() {
                    AnimationSet set = new AnimationSet(false);
                    Animation shakeAnima = new RotateAnimation(0, 15, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
                    switch (v.getId()) {
                              case R.id.ok:
                                        break;
                              case R.id.cancel:
                                        break;
                              case R.id.switch1_button:
                                        intent.putExtra("control", CurtainControlUtils.CLOSE);
                                        context.sendBroadcast(intent);
                                        break;
                              case R.id.switch2_button:
                                        intent.putExtra("control", CurtainControlUtils.OPEN);
                                        context.sendBroadcast(intent);
                                        break;
                              case R.id.switch3_button:
                                        intent.putExtra("control", CurtainControlUtils.STOP);
                                        context.sendBroadcast(intent);
                                        break;
                              case R.id.switch4_button:
                                        intent.putExtra("control", CurtainControlUtils.REDIC);
                                        context.sendBroadcast(intent);
                                        break;
                              default:
                                        break;
                    }

          }

          /**
           * description:广播接收，响应
           * auther：joahluo
           * time：2017/7/22 11:28
           */
          public class DeviceStutasReceiver extends BroadcastReceiver {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                              //所选设备状态更新
                              if (address.equals(intent.getStringExtra("address"))) {
                                        switch (type) {
                                                  case 3:
                                                            //状态不同
                                                            if (checked3 != (intent.getIntExtra("stutas3", 0) == 1 ? true : false)) {
                                                                      //变化数量
                                                                      flag_ui++;
                                                                      checked3=(intent.getIntExtra("stutas3", 0) == 1 ? true : false);
                                                            }
                                                  case 2:
                                                            //状态不同
                                                            if (checked2 != (intent.getIntExtra("stutas2", 0) == 1 ? true : false)) {
                                                                      //变化数量
                                                                      flag_ui++;
                                                                      checked2=(intent.getIntExtra("stutas2", 0) == 1 ? true : false);
                                                            }
                                                  case 1:
                                                            //状态不同
                                                            if (checked1 != (intent.getIntExtra("stutas1", 0) == 1 ? true : false)) {
                                                                      //变化数量
                                                                      flag_ui++;
                                                                      checked1=(intent.getIntExtra("stutas1", 0) == 1 ? true : false);
                                                            }
                                                  case 4:
                                                            //状态不同
                                                            if (checked4 != (intent.getIntExtra("stutas4", 0) == 1 ? true : false)) {
                                                                      //变化数量
                                                                      flag_ui++;
                                                                      checked4=(intent.getIntExtra("stutas4", 0) == 1 ? true : false);
                                                            }
                                        }
                                        //更新UI
                                        updateUI();

                              }

                    }

          }

}
