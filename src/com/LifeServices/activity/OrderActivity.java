package com.LifeServices.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.LifeServices.bean.Good;
import com.LifeServices.bean.Order;
import com.LifeServices.bean.Shop;
import com.LifeServices.utils.Util;
import com.way.tabui.gokit.R;

import java.util.Calendar;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 应用主界面
 *
 * @author Stone
 * @date 2014-5-13
 */
public class OrderActivity extends Activity implements OnClickListener {

          private TextView tvOrderShop;        // 店名
          private TextView tvOrderGood;        // 菜名
          private TextView tvOrderCount;        // 数量
          private TextView tvOrderTime;        // 取餐时间
          private TimePicker tpOrderTime;    // 时间选择控件
          private EditText etOrderPhone;        // 联系电话
          private EditText etOrderWords;        // 附加留言
          private Button btnOrderCountMore;    // 增加数量
          private Button btnOrderCountLess;    // 减少数量

          private Button btnOrderSetTime;        // 设置时间

          @SuppressWarnings("unused")
          private Button btnDlgOk;            // 设置时间完成
          private Button btnOrderSubmit;        // 提交订单

          //	private DialogOrder dlgSetOrderTime;
//	private View dlgOrderView;
          private int mHour;
          private int mMinute;
          private String time = "12 : 30";
          private TimePickerDialog dlgSetOrderTime;
          private TimePickerDialog.OnTimeSetListener mTimeSetListener =
               new TimePickerDialog.OnTimeSetListener() {

                         public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                   mHour = hourOfDay;
                                   mMinute = minute;
                                   updateDisplay();
                         }
               };

          // 从上级页面中传入的数据
          private Shop shop; // 当期选择的Shop
          private Good good; //当前选择的商品
          private String shopID; // 当前选择的Shop的ID

          @Override
          protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_order);

                    shop = (Shop) getIntent().getSerializableExtra("shop");
                    good = (Good) getIntent().getSerializableExtra("good");
                    shopID = getIntent().getStringExtra("shopID");

                    initView();
                    //initDlgView();
          }

          private void initView() {

                    tvOrderShop = (TextView) findViewById(R.id.tv_order_shop);
                    tvOrderGood = (TextView) findViewById(R.id.tv_order_good);
                    tvOrderCount = (TextView) findViewById(R.id.tv_order_count);
                    tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
                    tvOrderShop.setText(shop.getName());
                    tvOrderGood.setText(good.getName());


                    etOrderPhone = (EditText) findViewById(R.id.et_order_phone);
                    etOrderWords = (EditText) findViewById(R.id.et_order_words);

                    btnOrderCountMore = (Button) findViewById(R.id.btn_order_count_more);
                    btnOrderCountLess = (Button) findViewById(R.id.btn_order_count_less);
                    btnOrderSetTime = (Button) findViewById(R.id.btn_set_time);
                    btnOrderSubmit = (Button) findViewById(R.id.btn_order_submit);
                    btnOrderCountMore.setOnClickListener(this);
                    btnOrderCountLess.setOnClickListener(this);
                    btnOrderSetTime.setOnClickListener(this);
                    btnOrderSubmit.setOnClickListener(this);

          }



          @Override
          public void onClick(View v) {
                    int count = 1;
                    switch (v.getId()) {
                              case R.id.btn_order_count_more:
                                        count = Integer.parseInt(tvOrderCount.getText().toString());
                                        if (count == 4) {
                                                  toast("亲， 每份订单数量不能超过 4 哦");
                                        } else {
                                                  tvOrderCount.setText((count + 1) + "");
                                        }
                                        break;
                              case R.id.btn_order_count_less:
                                        count = Integer.parseInt(tvOrderCount.getText().toString());
                                        if (count == 1) {
                                                  toast("亲， 每份订单数量至少为 1 哦");
                                        } else {
                                                  tvOrderCount.setText((count - 1) + "");
                                        }
                                        break;
                              case R.id.btn_set_time:
                                        final Calendar c = Calendar.getInstance();
                                        mHour = c.get(Calendar.HOUR_OF_DAY);
                                        mMinute = c.get(Calendar.MINUTE);
                                        dlgSetOrderTime = new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
                                        dlgSetOrderTime.show();
                                        break;
                              case R.id.btn_dlg_ok:
                                        time = tpOrderTime.getCurrentHour() + " : " + tpOrderTime.getCurrentMinute();
                                        dlgSetOrderTime.dismiss();
                              case R.id.btn_order_submit:
                                        //判断电话是否正确
                                        if (!Util.isPhoneNumberValid(etOrderPhone.getText().toString())) {
                                                  toast("请输入正确的联系电话, 方便取餐");
                                        } else {
                                                  postOrder();
//                                                  finish();
                                        }
                                        //back();
                                        break;
                              default:
                                        break;
                    }

          }

          /**
           * 提交订单数据
           */
          private void postOrder() {
                    String count = tvOrderCount.getText().toString();
                    String phone = etOrderPhone.getText().toString();
                    String words = etOrderWords.getText().toString();
                    float price = Integer.parseInt(count) * Float.parseFloat(good.getPrice());
                    Order order = new Order();
//                    BmobUser user = BmobUser.getCurrentUser();
//                    order.setUserName(user.getUsername());
                    order.setUserName("小明");
                    order.setGoodID(good.getObjectId());
                    order.setGoodName(good.getName());
                    order.setShopID(shop.getObjectId());
                    order.setShopName(shop.getName());
                    order.setCount(count);
                    order.setTime(time);
                    order.setPrice(price + "");
                    order.setPhone(phone);
                    order.setTips(words);
                    order.save(new SaveListener<String>() {

                              @Override
                              public void done(String objectId, BmobException e) {
                                        if (e == null) {
                                                  toast("订单提交成功，将尽快为您派件！");
                                                  finish();
                                        } else {
                                                  toast("订单提交失败，请检查网络是否通畅！");
                                        }
                              }
                    });
          }

          private void updateDisplay() {
                    time = mHour + " : " + mMinute;
                    tvOrderTime.setText(time);
          }

          @SuppressWarnings("unused")
          private void back() {
                    Intent back = new Intent(OrderActivity.this, ShopItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", shop);
                    bundle.putString("shopID", shopID); //商铺的ID需要单独传递,否则获取到的是null
                    back.putExtras(bundle);
                    startActivity(back);
          }

          private void toast(String toast) {
                    Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
          }

}
