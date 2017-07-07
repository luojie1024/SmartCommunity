package com.way.util;

/**
 * Auther：joahluo
 * E-mail：joahluo@163.com
 * Time：2017/7/7 16:21
 */
public class ControlUtil {
          /**
           * description:穿入设备ID ，返回数据头
           * auther：joahluo
           * time：2017/7/7 16:23
           */
          public static final byte[] getControlHead(byte type, String ID, byte length) {
                    byte[] head = new byte[7];
                    head[0] = type;
                    byte[] bytes_id = ConvertUtil.hexStringToByte(ID);
                    //填充ID
                    for (int i = 0; i < 4; i++) {
                              head[i + 1] = bytes_id[i];
                    }
                    head[5] = ControlProtocol.CMD;
                    head[6] = length;
                    return head;
          }

          /**
           * description:获得空调控制头
           * auther：joahluo
           * time：2017/7/7 16:35
           */
          public static final byte[] getAirControlHead(String ID) {
                    byte[] head = new byte[7];
                    head[0] = ControlProtocol.DevType.CONTROL_AIR;
                    byte[] bytes_id = ConvertUtil.hexStringToByte(ID);
                    //填充ID
                    for (int i = 0; i < 4; i++) {
                              head[i + 1] = bytes_id[i];
                    }
                    head[5] = ControlProtocol.CMD;
                    head[6] = ControlProtocol.Instruction_Length.AIR_LENGTH;
                    return head;
          }
}
