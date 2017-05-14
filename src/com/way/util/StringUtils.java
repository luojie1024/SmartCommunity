package com.way.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ls on 15/2/5.
 */
public class StringUtils {

    public static String format(int i) {
        String s = "" + i;
        return i>=0&&s.length()<3?(s.trim().length() == 1)?"0"+s.trim():""+i:"00";
    }

    public static String format(String i) {
        return !TextUtils.isEmpty(i)?((i.trim().length() == 1)?"0"+i.trim():""+i):"00";
    }

    public static String formatAllTime(String i) {
        if (TextUtils.isEmpty(i)){
            return "00";
        }
        String[] starts = i.split(":");
        return (starts.length == 2)?format(starts[0])+":"+format(starts[1]):"00";
    }

    public static boolean checkPhoneNumber(String username) {
        boolean flag = false;
        if(username.length()!=11){
            return flag;
        }
        try {
            String check = "[0-9]*";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(username);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean checkPassword(String username) {
        boolean flag = false;
        if(username.length()<6)
            return flag;
        try {
            String check = "^[a-zA-Z0-9/?%+/#@!&=-_]*$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(username);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean checkEmail(String email) {
        boolean flag = false;
        if(email.equals("")){
            return true;
        }
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean checkUsername(String username) {
        // ^[a-zA-Z0-9_\s]*$
        boolean flag = false;
        if(username.length()<6||username.length()>18){
            return flag;
        }
        if (username != null && !"".equals(username)) {
            // try {
            // int i = Integer.parseInt(username);
            // return false;
            // } catch (Exception e) {
            // // TODO: handle exception
            // }
            if (checkFirstIfEng(username)) {
                try {
                    String check = "^[a-zA-Z0-9_]*$";
                    Pattern regex = Pattern.compile(check);
                    Matcher matcher = regex.matcher(username);
                    flag = matcher.matches();
                } catch (Exception e) {
                    flag = false;
                }
            }
        }
        return flag;
    }
    public static boolean checkFirstIfEng(String str) {
        boolean flag = false;
        try {
            String firstStr = str.substring(0, 1);
            Pattern regex = Pattern.compile("[a-zA-Z]+");
            Matcher matcher = regex.matcher(firstStr);
            flag = matcher.matches();
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    
    
    /** 
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0 
     * @param version1 
     * @param version2 
     * @return 
     */  
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {  
            return 0;
        }  
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");

        int idx = 0;  
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值

        int diff = 0;
//        while (idx < minLength
//                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
//                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
//            ++idx;
//
//        }

        while (idx < minLength
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;

        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;  
        return diff;  
    }  
    
    
    
    public static boolean isNumeric(String str){
  	  for (int i = 0; i < str.length(); i++){
  	   System.out.println(str.charAt(i));
  	   if (!Character.isDigit(str.charAt(i))){
  	    return false;
  	   }
  	  }
  	  return true;
	}
}
