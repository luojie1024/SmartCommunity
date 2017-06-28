package com.way.util;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/6/21 14:03
 */
public class ConvertUtil {
          //java 合并两个byte数组
          public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
                    byte[] byte_3 = new byte[byte_1.length+byte_2.length];
                    System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
                    System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
                    return byte_3;
          }

          //把16进制字符串转换成字节数组
          public static byte[] hexStringToByte(String hex) {
                    int len = (hex.length() / 2);
                    byte[] result = new byte[len];
                    char[] achar = hex.toCharArray();
                    for (int i = 0; i < len; i++) {
                              int pos = i * 2;
                              result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
                    }
                    return result;
          }

          //char转byte
          private static byte toByte(char c) {
                    byte b = (byte) "0123456789ABCDEF".indexOf(c);
                    return b;
          }

          //java int转byer
          public static byte[] intToByteArray(int i) {
                    byte[] result = new byte[4];
                    result[0] = (byte)((i >> 24) & 0xFF);
                    result[1] = (byte)((i >> 16) & 0xFF);
                    result[2] = (byte)((i >> 8) & 0xFF);
                    result[3] = (byte)(i & 0xFF);
                    return result;
          }


          //把字节数组转换成16进制字符串
          public static String byteStringToHexString(byte[] bytes) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    if (bytes == null || bytes.length <= 0) {
                              return null;
                    }
                    for (int i = 0; i < bytes.length; i++) {
                              int v = bytes[i] & 0xFF;
                              String hv = Integer.toHexString(v);
                              if (hv.length() < 2) {
                                        stringBuilder.append(0);
                              }
                              stringBuilder.append(hv);
                    }
                    return stringBuilder.toString();
          }

}
