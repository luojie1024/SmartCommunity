package com.way.util;

/**
 * Auther：joahluo
 * E-mail：joahluo@163.com
 * Time：2017/6/27 20:03
 */
public class ControlProtocol {

          public class DevStatus {
                    //开
                    public static final byte SWITCH_OPEN=0x01;
                    //关
                    public static final byte SWITCH_CLOSE=0x00;
          }

          public class DevType{
                    //一位开关
                    public static final byte SWITCH_ONE=0x00;
                    //二位开关
                    public static final byte SWITCH_TOW=0x01;
                    //三位开关
                    public static final byte SWITCH_THREE=0x02;
                    //四位开关
                    public static final byte SWITCH_FOUR=0x03;
                    //五孔插座
                    public static final byte PLUG_FIVE=0x10;
                    //五孔86插座
                    public static final byte PLUG_FIVE_86=0x11;
                    //红外转发空调
                    public static final byte CONTROL_AIR=0x20;
                    //红外转发电视
                    public static final byte CONTROL_TV=0x21;
                    //窗帘
                    public static final byte CONTROL_WINDOW=0x30;
                    //锁
                    public static final byte CONTROL_LOCK=0x40;
                    //增氧机
                    public static final byte CONTROL_OXYGEN=0x50;
          }

}
