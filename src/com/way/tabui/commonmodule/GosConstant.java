package com.way.tabui.commonmodule;

import java.util.List;

public class GosConstant  {
/*64位测试机*/
//	// 在机智云开发者账号下申请的App_ID a61ed92da3764cca848f3dbab8481149//4d25e29be7e74a3aa18e2e7921cecb51
//	public static String App_ID = "6fcbb295af17438aab3e3c19a82582c1";
//	// 在机智云开发者账号下申请的App_Screct(必须与上述App_ID对应)57c13265403549ac83d828e50639c37a//84d1094f9dfe4911a961e5ef79b8e4f0
//	public static  String App_Screct = "dd7b9317441541b6ad7e2a1799ecb1ff";
//	// 在机智云开发者账号下创建的产品ProductKey330b43e5cd9b4aa9a03fc97c5f6f52a4//2246c7de027244038dc8bae975453eb6
//	public static String device_ProductKey = "2246c7de027244038dc8bae975453eb6";
// //e4426f0aa0e446a3a34260ec2064d609//ff1a9d62c35b4a4b9ca0c8eea0d120a2
//    public static String Product_Secret="ff1a9d62c35b4a4b9ca0c8eea0d120a2";

/*正式用户使用*/
    // 在机智云开发者账号下申请的App_ID a61ed92da3764cca848f3dbab8481149//4d25e29be7e74a3aa18e2e7921cecb51
    public static String App_ID = "a61ed92da3764cca848f3dbab8481149";
    // 在机智云开发者账号下申请的App_Screct(必须与上述App_ID对应)57c13265403549ac83d828e50639c37a//84d1094f9dfe4911a961e5ef79b8e4f0
    public static  String App_Screct = "57c13265403549ac83d828e50639c37a";
    // 在机智云开发者账号下创建的产品ProductKey330b43e5cd9b4aa9a03fc97c5f6f52a4//2246c7de027244038dc8bae975453eb6
    public static String device_ProductKey = "330b43e5cd9b4aa9a03fc97c5f6f52a4";
    //e4426f0aa0e446a3a34260ec2064d609//ff1a9d62c35b4a4b9ca0c8eea0d120a2
    public static String Product_Secret="e4426f0aa0e446a3a34260ec2064d609";

	// Gokit设备热点默认前缀
	public static final String SoftAP_Start = "XPG-GAgent";

	// Gokit设备热点默认密码
	public static final String SoftAP_PSW = "123456789";

	// 存储器默认名称
	public static final String SPF_Name = "set";

	// 在腾讯开发者帐号下申请的用于QQ登录的APP_ID
	public static final String Tencent_APP_ID = "your_tencent_app_id";

	// 在百度开发者帐号下申请的用于百度推送的AppKey
	public static final String BaiDuPush_AppKey = "your_baidu_push_app_key";
	
	// 待过滤的设备ProductKey列表(此变量用于全局访问，开发者不需要修改)
	public static List<String> ProductKeyList = null;
}
