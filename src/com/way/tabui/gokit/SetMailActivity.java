package com.way.tabui.gokit;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.way.tabui.commonmodule.GosBaseActivity;

public class SetMailActivity extends GosBaseActivity {

	private EditText ed_mail,ed_password;
	private Button set_mes;
	private String mail,password;
	private String host=null; 
	private SetThread setThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_mail);
		setProgressDialog();
		initview();
		initevent();
		initdata();
	}
	public void initview(){
		ed_mail = (EditText) findViewById(R.id.ed_mail);
		ed_password =(EditText) findViewById(R.id.ed_password);
		set_mes =(Button) findViewById(R.id.set_mes);
	}
	
	private void initdata(){
		ed_mail.setText(spf.getString("email", null));
		ed_password.setText(spf.getString("emailpassword",null));
//		host=spf.getString("emailhost", null);
		
	}
	private void initevent(){
		set_mes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mail=ed_mail.getText().toString();
				password=ed_password.getText().toString();
				if(isEmail(mail)){
					if(mail.contains("@163.com"))
						host = "smtp.163.com";
//					else if(mail.contains("@qq.com"))
//						host = "smtp.qq.com";
//					else if(mail.contains("@vip.qq.com"))
//						host = "smtp.qq.com";
					else if(mail.contains("@sina.com"))
						host = "smtp.sina.com";
					else if(mail.contains("@126.com"))
							host = "smtp.126.com";
					else if(mail.contains("@sohu.com"))
							host = "smtp.sohu.com";
					else
						Toast.makeText(getApplicationContext(), "暂不支持该邮箱...", Toast.LENGTH_SHORT).show();
					if(host!=null){
						progressDialog.setMessage("验证中...");
						progressDialog.show();
						setThread= new SetThread();
						setThread.start();
				
					  }
				}else{
					 Toast.makeText(getApplicationContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private class SetThread extends Thread{
		@Override
		public void run() {
			setmail();
		}
	}
	   private Handler mHandler = new Handler(){
				public void handleMessage(android.os.Message msg) {
					switch(msg.what){
					case 1:
						progressDialog.cancel();
						 Toast.makeText(getApplicationContext(), "邮箱验证失败...", Toast.LENGTH_SHORT).show();
						 Log.i("==", msg.obj.toString());
						 break;
					case 2:
						progressDialog.cancel();
						  spf.edit().putString("email",mail).commit();
						  spf.edit().putString("emailpassword",password).commit();
						  spf.edit().putString("emailhost", host).commit();
						  Toast.makeText(getApplicationContext(), "验证完成,保存信息成功", Toast.LENGTH_SHORT).show();
						  finish();
						break;
					default:
							break;
					
					}
				};
			};
	android.os.Message mas;
	public void setmail(){
		Properties props = System.getProperties();
		 props.put("mail.smtp.host", host);
		 props.put("mail.smtp.auth", "true");
		 PopupAuthenticator auth = new PopupAuthenticator();
		 Transport trans;
		 Session session = Session.getInstance(props, auth);
		  session.setDebug(true);  
		  boolean flag=true;
          try {
			trans  = session.getTransport("smtp");
			trans.connect(host,mail, password);
			trans.close();
		} catch (AuthenticationFailedException ae) { 
			 mas= mHandler.obtainMessage();
			 mas.what=1;
			 mas.obj=ae;
			 mHandler.sendMessage(mas);
//      	  Toast.makeText(getApplicationContext(), "邮箱验证失败...", Toast.LENGTH_SHORT).show();
      	 flag =false;
      } catch (MessagingException e) {
			// TODO Auto-generated catch block
    	  mas= mHandler.obtainMessage();
			 mas.what=1;
			 mHandler.sendMessage(mas);
			 mas.obj=e;
    	  Toast.makeText(getApplicationContext(), "邮箱验证错误...", Toast.LENGTH_SHORT).show();
    	  flag=false;
		}
          if(flag){
        	  mas= mHandler.obtainMessage();
 			 mas.what=2;
 			 mHandler.sendMessage(mas);
          }
		
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

	class PopupAuthenticator extends Authenticator {
	       public PasswordAuthentication getPasswordAuthentication() {
	      String username =mail ; 
	      String pwd = password; 
	      return new PasswordAuthentication(username, pwd);
	      }
	    }
}
