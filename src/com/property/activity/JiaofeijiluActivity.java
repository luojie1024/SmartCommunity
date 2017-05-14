package com.property.activity;

import org.kymjs.kjframe.ui.BindView;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.property.base.BaseActivity;
import com.way.tabui.gokit.R;

public class JiaofeijiluActivity extends BaseActivity {

	@BindView(id=R.id.ll_shifouyijiaofei,click=true)
	private LinearLayout llShifouyijiaofei;
	@BindView(id=R.id.ll_jiaofeileixing,click=true)
	private LinearLayout llJiaofeileixing;
	@BindView(id=R.id.ll_shaixuan,click=true)
	private LinearLayout llShaixuan;
	@BindView(id=R.id.tv_shifouyijiaofei)
	private TextView tvShifouyijiaofei;
	@BindView(id=R.id.tv_jiaofeileixing)
	private TextView tvJiaofeileixing;
	@BindView(id=R.id.tv_shaixuan)
	private TextView tvShaixuan;
	@BindView(id=R.id.iv_shifouyijiaofei)
	private ImageView ivShifouyijiaofei;
	@BindView(id=R.id.iv_jiaofeileixing)
	private ImageView ivJiaofeileixing;
	@BindView(id=R.id.iv_shaixuan)
	private ImageView ivShaixuan;
	
	private PopupWindow popupWindow;
	private String [] str = {"全部","已付款","未付款"};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_jiaofeijilu);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.ll_shifouyijiaofei:
			View view = getLayoutInflater().inflate(R.layout.popupwindow_shifouyifukuan,
					null, false);
			final ListView lvShifouyifukuan = (ListView) view
					.findViewById(R.id.lv_shifouyifukuan);
			clickInflater(v, view, tvShifouyijiaofei, ivShifouyijiaofei);
			break;
		case R.id.ll_jiaofeileixing:
			
			break;
		case R.id.ll_shaixuan:
	
			break;
		default:
			break;
		}
	}
	
	private void clickInflater(View v, View inflateView, TextView tvFilter,
			final ImageView ivArrow) {
		if (popupWindow == null || !popupWindow.isShowing()
				|| (popupWindow != null && popupWindow.isShowing())) {
			if (popupWindow != null && popupWindow.isShowing()) {
				setImage();
				setTextColor();
				if (popupWindow.getContentView().getTag().equals(v.getId())) {
					popupWindow.dismiss();
					popupWindow = null;
					return;
				}
				popupWindow.dismiss();
				popupWindow = null;
			}
			inflateView.getBackground().setAlpha(150);
			popupWindow = new PopupWindow(inflateView,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			popupWindow.showAsDropDown(v, 0, 2);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			ivArrow.setImageDrawable(getResources().getDrawable(
					R.drawable.sort_arrow_red));
			tvFilter.setTextColor(Color.parseColor("#F86D52"));
			popupWindow.getContentView().setTag(v.getId());// 设置tag用来比较第二次弹出

			popupWindow.getContentView().setOnTouchListener(
					new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (popupWindow != null && popupWindow.isShowing()) {
								ivArrow.setImageDrawable(getResources()
										.getDrawable(R.drawable.sort_arrow));
								setTextColor();
								popupWindow.dismiss();
								popupWindow = null;
							}
							return false;
						}
					});
			inflateView.setFocusableInTouchMode(true);
			inflateView.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if (popupWindow != null && popupWindow.isShowing()) {
							ivArrow.setImageDrawable(getResources()
									.getDrawable(R.drawable.sort_arrow));
							setTextColor();
							popupWindow.dismiss();
							popupWindow = null;
						}
					}
					return false;
				}
			});
		}
	}
	
	/**
	 * 设置取消筛选后箭头的图片
	 */
	private void setImage() {
		ivShifouyijiaofei.setImageDrawable(getResources().getDrawable(
				R.drawable.sort_arrow));
		ivJiaofeileixing.setImageDrawable(getResources().getDrawable(
				R.drawable.sort_arrow));
		ivShaixuan.setImageDrawable(getResources().getDrawable(
				R.drawable.sort_arrow));
	}

	private void setTextColor() {
		tvShifouyijiaofei.setTextColor(Color.parseColor("#898888"));
		tvJiaofeileixing.setTextColor(Color.parseColor("#898888"));
		tvShaixuan.setTextColor(Color.parseColor("#898888"));
	}
}
