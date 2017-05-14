
/**  
* 文件名：ScreenInfo.java  
*  
* 版本信息：  
* 日期：2014年5月1日  
* Copyright 足下 Corporation 2014   
* 版权所有  
*  
*/

package com.way.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;


/**  
 *   
 * 项目名称：SmartCommunity  
 * 类名称：ScreenInfo  
 * 类描述： 屏幕信息
 * 创建人：Admin  
 * 创建时间：2014年5月1日 上午11:01:53  
 * 修改人：Admin  
 * 修改时间：2014年5月1日 上午11:01:53  
 * 修改备注：  
 * @version   
 *   
 */
public class ScreenInfo {
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
}
