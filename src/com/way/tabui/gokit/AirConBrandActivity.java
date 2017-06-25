package com.way.tabui.gokit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.tabui.commonmodule.GosBaseActivity;

import java.util.ArrayList;

public class AirConBrandActivity extends GosBaseActivity {

	private ListView listview_air_con_mes;
	ArrayList<String> mlist=new ArrayList<String>();
	/** 传过来正在连接中的的设备 */
	private GizWifiDevice device = null;
	MyAdapter adapter;
	//各个品牌的控制码范围
	int[] min={0,0,1,40,60,80,100,110,120,140,150,170,180,190,190,200,210};
	int[] max={1000,39,19,59,79,99,109,119,139,149,169,179,199,190,190,209,229};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_con_brand);
		setActionBar(true, true, getResources().getString(R.string.title_activity_air_con_brand));
		initDevice();
		initView();
		initData();
		initList();
		
	}
	
	public void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
	}
	
	private void initView(){
		listview_air_con_mes=(ListView) findViewById(R.id.listview_air_con_mes);
	}
	
	private void initList(){
		listview_air_con_mes.setAdapter(adapter);
		listview_air_con_mes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AirConBrandActivity.this,AirMateActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("GizWifiDevice", device);
				intent.putExtras(bundle);
				intent.putExtra("min", min[position]);
				intent.putExtra("max", max[position]);
				intent.putExtra("name", mlist.get(position));
				startActivityForResult(intent, 1000);	
			}
		});
		
	}
	
	private void initData(){
		String[] brands = getResources().getStringArray(R.array.airbrand);
		for (String str : brands) {
			mlist.add(str);
		}
		adapter = new MyAdapter(this, mlist);
	}
	
	private class MyAdapter extends BaseAdapter{

		Context context;
		ArrayList<String> mlist;

		public MyAdapter(Context context, ArrayList<String> mlist) {
			super();
			this.context = context;
			this.mlist=mlist;
		}
		
		@Override
		public int getCount() {
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = View.inflate(context, R.layout.listview_air_con_brand, null);
				viewHolder=new ViewHolder();
				viewHolder.tvAirBrand= (TextView) convertView.findViewById(R.id.tvAirBrand);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.tvAirBrand.setText(mlist.get(position));
			return convertView;
		}
		
		class ViewHolder{
		 public TextView tvAirBrand;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
       

			if(data!=null){
				boolean issave=data.getBooleanExtra("issave", false);
				if(issave){
					finish();
				}
			}
        
		 super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
