package com.way.tabui.settingsmodule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.gokit.R;

public class SetBundMesActivity extends GosBaseActivity {

	private EditText ed_ProductKey, ed_appid, ed_appscrect;
	private Button bt_bdmes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_bund_mes);
		setActionBar(true, true, R.string.title_activity_set_bund_mes);
		 initview();
		 inintEvevt();
	}
	private void initview(){
		ed_ProductKey = (EditText) findViewById(R.id.ed_ProductKey);
		ed_ProductKey.setText(GosConstant.device_ProductKey.toString());
		ed_appid = (EditText) findViewById(R.id.ed_appid);
		ed_appid.setText(GosConstant.App_ID.toString());
		ed_appscrect = (EditText) findViewById(R.id.ed_appscrect);
		ed_appscrect.setText(GosConstant.App_Screct.toString());
		bt_bdmes = (Button) findViewById(R.id.bt_bdmes);
	}
	private void inintEvevt(){
		bt_bdmes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spf.edit()
						.putString("prroductkey",
								ed_ProductKey.getText().toString()).commit();
				spf.edit().putString("appid", ed_appid.getText().toString())
						.commit();
				spf.edit()
						.putString("appscrect",
								ed_appscrect.getText().toString()).commit();
				
				Toast.makeText(getApplicationContext(),
						"存储信息完毕,重新启动APP中...请稍等候..", Toast.LENGTH_SHORT).show();
				Intent reintent = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
								getBaseContext().getPackageName());
				PendingIntent restartIntent = PendingIntent.getActivity(
						getApplicationContext(), 0, reintent,
						PendingIntent.FLAG_ONE_SHOT);
				AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis(),
						restartIntent);
				Intent intent = new Intent();
				intent.setAction("com.way.util.exit_app");
				sendBroadcast(intent);
			}
		});
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
