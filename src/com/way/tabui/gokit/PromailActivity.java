package com.way.tabui.gokit;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.way.tabui.commonmodule.GosBaseActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PromailActivity extends GosBaseActivity {

	  String host = null;   //发件人使用发邮件的电子信箱服务器
//	    String from = "chen13024@163.com";    //发邮件的出发地（发件人的信箱）
	  
	    
	    private EditText ed_mail_rev,ed_mail_text,ed_mail_title;
	    private Button bt_send_mail;
	    private String email,emailpassword;
	    private Handler mHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch(msg.what){
				case 1:
					progressDialog.cancel();
					Toast.makeText(getApplicationContext(), "创建邮件失败..", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					progressDialog.cancel();
					Toast.makeText(getApplicationContext(), "用户验证失败..", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					progressDialog.cancel();
					Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
					Log.i("==", msg.obj.toString());
					break;
				case 4:
					progressDialog.cancel();
					Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
					break;
				default:
						break;
				
				}
			};
		};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promail);
		initview();
		initevent();
		initdata();
		setProgressDialog();
		if(email==null||emailpassword==null){
			Toast.makeText(getApplicationContext(), "未设置邮箱账号，请前往设置...", Toast.LENGTH_SHORT).show();
			IntentToSet();
			}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initdata();
		super.onResume();
	}
	private void initview(){
		ed_mail_rev=(EditText)findViewById(R.id.ed_mail_rev);
		ed_mail_text=(EditText)findViewById(R.id.ed_mail_text);
		bt_send_mail=(Button) findViewById(R.id.bt_send_mail);
		ed_mail_title=(EditText)findViewById(R.id.ed_mail_title);
	}
	
	private void initdata(){
		email=spf.getString("email", null);
		emailpassword=spf.getString("emailpassword", null);
		host=spf.getString("emailhost", null);
		
	}
	SendThread thread;
//	ProgressDialog progressDialog;
	private void initevent(){
		bt_send_mail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog.setMessage("发送中...");
				boolean ismail = isEmail(ed_mail_rev.getText().toString());
				if(ismail){
					if(email==null||emailpassword==null){
						Toast.makeText(getApplicationContext(), "未设置邮箱账号，请前往设置...", Toast.LENGTH_SHORT).show();
						IntentToSet();
						}else{
//				Toast.makeText(getApplicationContext(), "发送中...", Toast.LENGTH_SHORT).show();
							progressDialog.show();
				 thread  = new SendThread();
				 thread.start();
				 }
//				 thread.interrupt();
				 }else{
					 Toast.makeText(getApplicationContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
				 }
				 
				
			}
		});
	}
	
	private void IntentToSet(){
		Intent intent = new Intent(PromailActivity.this,SetMailActivity.class);
		startActivity(intent);
	}
	/**
	   * 判断邮箱是否合法
	   * @param email
	   * @return
	   */
	  public static boolean isEmail(String email){  
	    if (null==email || "".equals(email)) return false;	
	    //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配  
	    Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  
	    Matcher m = p.matcher(email);  
	    return m.matches();  
	  }
	
	android.os.Message mas;
	
	@SuppressWarnings("static-access")
	public void sendmail() {
		 Properties props = System.getProperties();
		 props.put("mail.smtp.host", host);
		 props.put("mail.smtp.auth", "true");
		 PopupAuthenticator auth = new PopupAuthenticator();
		 
		 Session session = Session.getInstance(props, auth);
		 
		 MimeMessage message = new MimeMessage(session);
		 Transport trans;

		 
		 
//		 创建邮件体:
			 try {
			 message.setSubject(ed_mail_title.getText().toString());
			 message.setText(ed_mail_text.getText().toString());
			 message.setFrom(new InternetAddress(email));
			 message.addRecipient(Message.RecipientType.TO,new InternetAddress(ed_mail_rev.getText().toString()));
			 message.saveChanges();
			
			 } catch (MessagingException e) {
					// TODO Auto-generated catch block
				 mas= mHandler.obtainMessage();
				 mas.what=1;
				 mHandler.sendMessage(mas);
				}
			boolean flag=true;
			
			 try {  
                 session.setDebug(true);  
                 trans  = session.getTransport("smtp");  
                 trans.connect(host,email,emailpassword);
                 trans.send(message);
 				 trans.close();
          } catch (AuthenticationFailedException ae) {  
        	    mas= mHandler.obtainMessage();
        	    mas.what=2;
				mHandler.sendMessage(mas);
				 flag=false;
//        	  Toast.makeText(getApplicationContext(), "用户验证失败..", Toast.LENGTH_SHORT).show();

          } catch (MessagingException mex) {
        	  mas= mHandler.obtainMessage();
        	  mas.what=3;
        	  mas.obj=mex;
        	  mHandler.sendMessage(mas);
        	  
        	  flag=false;
//        	  Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
         }   
			 if(flag){
			 mas= mHandler.obtainMessage();
			 mas.what=4;
       	     mHandler.sendMessage(mas);
       	     }
			 
			 
	}
	
	private class SendThread extends Thread{
		@Override
		public void run() {

			sendmail();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.set_mail, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.settings_mail:
			IntentToSet();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
		
		
	}

	
	class PopupAuthenticator extends Authenticator {
       public PasswordAuthentication getPasswordAuthentication() {
      String username = email; 
      String pwd = emailpassword; 
      return new PasswordAuthentication(username, pwd);
      }
    }
	
	
	
	
}
