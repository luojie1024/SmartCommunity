package com.property.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.util.AbDialogUtil;
import com.property.base.BaseActivity;
import com.property.duotushangchuan.AlbumActivity;
import com.property.duotushangchuan.Bimp;
import com.property.duotushangchuan.FileUtils;
import com.property.duotushangchuan.GalleryActivity;
import com.property.duotushangchuan.ImageItem;
import com.property.duotushangchuan.PublicWay;
import com.property.duotushangchuan.Res;
import com.property.utils.ScreenUtil;
import com.property.utils.SharedpfTools;
import com.property.utils.UrlConnector;
import com.property.view.MyGridView;
import com.way.tabui.gokit.R;

public class BaoxiushenqingActivity extends BaseActivity {

	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click = true)
	private TextView tvBack;
	@BindView(id = R.id.tv_fault_name)
	private TextView tvName;
	@BindView(id = R.id.tv_fault_mobile)
	private TextView tvMobile;
	@BindView(id = R.id.tv_fault_berth)
	private TextView tvBerth;
	@BindView(id = R.id.et_baoxiu_title)
	private EditText etTitle;
	@BindView(id = R.id.et_baoxiu_content)
	private EditText etContent;
	@BindView(id = R.id.gv_gallery)
	private MyGridView gvGallery;
	@BindView(id = R.id.tv_baoxiu_fabu, click = true)
	private TextView tvFabu;
	
	private SharedpfTools sharedpfTools;
	private KJHttp http;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private PopupWindow pop2 = null;
	private LinearLayout ll_popup2;
	private PopupWindow pop3 = null;
	private LinearLayout ll_popup3;
	public static Bitmap bimap;
	private GridAdapter adapter;
	private int state = 1;
	private List<ImageItem> list;
	private int listSize = 1;
	private FaultUserInfo faultUserInfo;
	
	private String user_id;	//int 用户id
	private String title;	//string 报障标题
	private String content;	//string 报障内容
	private String village_id = "0";	//int 小区id
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_baoxiushenqing);
	}

	@Override
	public void initData() {
		super.initData();							
		list = new ArrayList<ImageItem>();
		http = new KJHttp();
		sharedpfTools = SharedpfTools.getInstance(this);
		//user_id = sharedpfTools.getUid();
		user_id=100+"";
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add_image);
		PublicWay.activityList.add(this);
		Init();
		getUserInfo();//获得信息
		//Bimp.getBimp().tempSelectBitmap.clear();
		//Bimp.max = 0;
	}

	@Override
	protected void onResume() {
		super.onResume();
		DisplayMetrics outMetrics = new DisplayMetrics();  
	    getWindowManager().getDefaultDisplay().getMetrics(outMetrics);  
	    float density = outMetrics.density; // 像素密度
	    int spacingWidth = (int) (3*density);  
		if (Bimp.getBimp().tempSelectBitmap.size() == 9) {
			listSize = 9;
		}else{
			listSize = Bimp.getBimp().tempSelectBitmap.size()+1;
		}
		ViewGroup.LayoutParams params = gvGallery.getLayoutParams();  
		int itemWidth = ScreenUtil.getScreenWidth(getApplication())/4-20;  		       
		params.width = itemWidth*listSize+(listSize-1)*spacingWidth;
		gvGallery.setStretchMode(GridView.NO_STRETCH); // 设置为禁止拉伸模式  
		gvGallery.setNumColumns(listSize);  
		gvGallery.setHorizontalSpacing(spacingWidth); 
		gvGallery.setColumnWidth(itemWidth);  
		gvGallery.setLayoutParams(params);	
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Bimp.getBimp().tempSelectBitmap != null
				&& Bimp.getBimp().tempSelectBitmap.size() > 0) {
			Bimp.getBimp().max = 0;
			Bimp.getBimp().tempSelectBitmap.clear();
		}
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.iv_back:
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_baoxiu_fabu:
			if(etTitle.getText().toString().trim() == null || etTitle.getText().toString().trim().length()<=0){
				Toast.makeText(getApplication(), "请输入报障标题", Toast.LENGTH_SHORT).show();				
			}else if(etContent.getText().toString().trim() == null || etContent.getText().toString().trim().length()<=0){
				Toast.makeText(getApplication(), "请输入报障内容", Toast.LENGTH_SHORT).show();
			}else{
				title = etTitle.getText().toString().trim();
				content = etContent.getText().toString().trim();
				sendPost();
			}
			break;
		default:
			break;
		}
	}

	public void getUserInfo(){
		HttpParams params = new HttpParams();		
		params.put("uid", user_id);	
		
		tvName.setText("姓名：小明");
		tvMobile.setText("电话：8888888");
		tvBerth.setText("铺位:E3-420");
		
//		http.post(UrlConnector.FAULT_USER_INFO, params, false,
//				new HttpCallBack() {
//					@Override
//					public void onFailure(int errorNo, String strMsg) {
//						super.onFailure(errorNo, strMsg);
//						Toast.makeText(getApplication(), "请求失败",
//								Toast.LENGTH_LONG).show();
//					}
//
//					@Override
//					public void onSuccess(String t) {
//						super.onSuccess(t);
//						faultUserInfo = gson.fromJson(t, FaultUserInfo.class);
//						tvName.setText("姓名："+faultUserInfo.getInfo().getName());
//						tvMobile.setText("电话："+faultUserInfo.getInfo().getMobile());
//						tvBerth.setText("铺位："+faultUserInfo.getInfo().getBerth());
//					}
//				});
	}
	
	public void sendPost() {			
		AbDialogUtil.showProgressDialog(this, 0, "正在提交...");
		tvFabu.setClickable(false);
		HttpParams params = new HttpParams();		
		params.put("user_id", user_id);
		params.put("title", title);		
		params.put("content", content);
		params.put("village_id", village_id);		
		for (int i = 0; i < Bimp.getBimp().tempSelectBitmap.size(); i++) {
			params.put("file"+i, new File(Bimp.getBimp().tempSelectBitmap.get(i).getImagePath()));
		}		
		http.post(UrlConnector.FAULT_ADD, params, false,
				new HttpCallBack() {
					@Override
					public void onFailure(int errorNo, String strMsg) {
						super.onFailure(errorNo, strMsg);
						Toast.makeText(getApplication(), "请求失败",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						try {
							JSONObject object = new JSONObject(t);
							String msg = object.getString("msg");
							Toast.makeText(getApplication(), msg,
									Toast.LENGTH_LONG).show();
							finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}	
		
	public void Init() {
		parentView = getLayoutInflater().inflate(R.layout.activity_baoxiushenqing, null);
		pop = new PopupWindow(BaoxiushenqingActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), AlbumActivity.class).putExtra("state", state);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
//		handler = new Handler() {
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 1:
//					list.addAll(Bimp.getBimp().tempSelectBitmap);
//					Log.e("55555", list.size()+"");
//					initHotData(list, llHotContent1, hsvScroll1);
//					break;
//				}
//				super.handleMessage(msg);
//			}
//		};
//		loading();
//		list.addAll(Bimp.getBimp().tempSelectBitmap);
//		Log.e("55555", list.size()+"");
//		initHotData(list, llHotContent1, hsvScroll1);
		gvGallery.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		gvGallery.setAdapter(adapter);
		gvGallery.setOnItemClickListener(new OnItemClickListener() {

			//TODO
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == Bimp.getBimp().tempSelectBitmap.size()) {
					ll_popup.startAnimation(
							AnimationUtils.loadAnimation(getApplication(), R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.CENTER, 0, 0);
				} else {
					Intent intent = new Intent(getApplication(), GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}
			
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.getBimp().tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.getBimp().tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida1,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
				int width = wm.getDefaultDisplay().getWidth()/4-20;
				int height = width;
				LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.image.getLayoutParams();
				linearParams.width = width;
				linearParams.height = height; 		  		  
				holder.image.setLayoutParams(linearParams);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.getBimp().tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_image));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.getBimp().tempSelectBitmap
						.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.getBimp().tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}	
	
	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();		
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;
	
	//TODO
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				if (Bimp.getBimp().tempSelectBitmap.size() < 9
						&& resultCode == RESULT_OK) {

					String fileName = String
							.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUtils.saveBitmap(bm, fileName);

					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);
					takePhoto
							.setImagePath(FileUtils.SDPATH + fileName + ".jpg");
					Bimp.getBimp().tempSelectBitmap.add(takePhoto);
				}
				break;			
			}
		}
	}		
}
