
package com.showmo.demo.util;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {


	public static boolean checkFileEnd(String filename,String[] filter){
		if(filter==null||filter.length==0){
			return true;
		}
		for (String end:filter){
			if(filename.endsWith(end)){
				return true;
			}
		}
		return false;
	}

	public static boolean isNotEmpty(String obj) {
		if (obj == null) {
			return false;
		}

		if (obj.trim().length() == 0) {
			return false;
		}

		if (obj.equalsIgnoreCase("null")) {
			return false;
		}
		return true;
	}
	public static byte[] getBytesFromAssert(Context context,String file, int maxLen){
		try {
			InputStream is= context.getAssets().open(file);
			byte[] bytes=new byte[maxLen];
			int d=is.read(bytes);
			return bytes;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}

	public static String email2LowerCase(String email )
	{
		/*String[] all = email.split("@");
		if(all.length == 1 || all.length != 2 ){
			return null ;
		}*/
//		char[] data = email.toCharArray();
//		int dist = 'a' - 'A';
//		for (int i = 0 ; i < email.length(); i++)
//		{
//			if (data[i] >= 'A' && data[i] <= 'Z')
//			{
//				data[i] += dist;
//			}
//		}

		return email.toLowerCase();

	}


	public static boolean checkPsw(String s){
		if(isNotEmpty(s)){
			return s.matches("\\w{6,18}");
		}else{
			return false ;
		}

	}
	
	public static boolean checkPswRe(String psw,String pswre){
		
		if(isNotEmpty(psw) && isNotEmpty(pswre) ){
		    return	psw.equals(pswre);
		}else{
			return false ;
		}

	}

	public static boolean checkEmail(String s){
		if(isNotEmpty(s)){
			return s.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		}else{
			return false ;
		}

	}


	/*

	 *130/131/132/155/156/185/186/145/176
	 *133/153/180/181/189/177
	 *134/135/136/137/138/139/150/151/152/157/158/159/182/183/184/187/188/147/178
	 * 
	 * 13 0-9
	 * 14 5 7
	 * 15 0-3 5-8
	 * 17 6-8
	 * 18 0-9
	 */
	public static boolean checkPhoneNumber(String s){
		if(isNotEmpty(s)){
			Log.e("login", "checkPhoneNumber  " + s);
			boolean bret=s.matches("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
			Log.e("login", "checkPhoneNumber res " + bret);
			return bret;
		}else{
			return false ;
		}
	}
	
	public static boolean checkVerificationCode(String s){
		if(isNotEmpty(s)){
			return s.matches("\\d{6}");
		}else{
			return false ;
		}
		
		
	}

	public static List<String> splitWithChar(String src,char c){
		List<String> strlist=new ArrayList<String>();
		String str=new String();
		for(int i=0;i<src.length();i++){
			if(src.charAt(i)==c){
				strlist.add(str);
				str=new String();
			}else{
				str=str+String.valueOf(src.charAt(i));
			}
		}
		strlist.add(str);
		return strlist;
	}
	public static String StringFilter(String src,String regEx){
		Pattern pattern=Pattern.compile(regEx);
		Matcher matcher= pattern.matcher(src);
		String[] strArr=pattern.split(src);
		for (int i=0;i<strArr.length;i++){
			Log.e("filter", "strArr " + strArr[i]);
		}
		Log.e("filter", "src " + matcher.toString());
		Log.e("filter", "result1 " + matcher.toString());
		Log.e("filter", "result2 " + matcher.replaceAll("") + "  count " + matcher.groupCount());
//		for (int i=0;i<matcher.groupCount();i++){
//			LogUtils.e("filter","result2while "+matcher.g);
//		}
		return matcher.replaceAll("");
	}
	public static String specielFilter(String src){
		String regEx="[~`!@#$%\\^&*\\(\\)_+=-\\?<>,.;:'\"\n\t\\|\\\\/\\[\\]\\{\\}]";
		return filter(src,regEx);
	}
	public static String filter(String src,String regEx){
		Pattern pattern=Pattern.compile(regEx);
		Matcher matcher =pattern.matcher(src);
		return matcher.replaceAll("");
	}
	public static boolean isChinese(String str){
		String regEx="\\w*([\\u4e00-\\u9fa5]+)\\w*";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		boolean bres=m.matches();
		if(bres){
			Log.e("regex", "str:" + str + " matches " + bres);
		}
		return bres;
	}
	public static String[] sortByDesc(String[] name){
		String lang = Locale.getDefault().getLanguage();
		Comparator comp = null;
		if(lang.equals("zh")){
			comp = Collator.getInstance(Locale.ENGLISH);
		}
		else if(lang.equals("ja")){
			comp = Collator.getInstance(Locale.JAPAN);
		}
		else if(lang.equals("en")){
			comp = Collator.getInstance(Locale.ENGLISH);
		}
		else {
			comp = Collator.getInstance(Locale.ENGLISH);
		}
		Arrays.sort(name, comp);
		return name;
	}
}
