package com.way.util;

/**
 * Auther：joahluo
 * E-mail：joahluo@163.com
 * Time：2017/7/7 16:21
 */
public class AirControlUtil {
//          public static final byte[] AIR_OPEN={(byte)0x04,(byte)0xFF,(byte)0x08,(byte)0x08,(byte)0x00};

          /**
           * 开 关 0:开 1：关
           * 327432-->04FF08
           * 262152-->040008
           * TODO
           */
          public static final byte[][] OPCL={{(byte)0x04,(byte)0xFF,(byte)0x08,(byte)0x08,(byte)0x00},{(byte)0x04,(byte)0x00,(byte)0x08,(byte)0x08,(byte)0x00}};

          /**
           * 模式 0：制冷 1：抽湿 2：送风 3：制热  4:自动
           */
          public static final byte[][] AIR_MOD = {{(byte)0x05,(byte)0x01,(byte)0x08,(byte)0x08,(byte)0x00}, {(byte)0x05,(byte)0x02,(byte)0x08,(byte)0x08,(byte)0x00}, {(byte)0x05,(byte)0x03,(byte)0x08,(byte)0x08,(byte)0x00}, {(byte)0x05,(byte)0x04,(byte)0x08,(byte)0x08,(byte)0x00}, {(byte)0x05,(byte)0x00,(byte)0x08,(byte)0x08,(byte)0x00}};

          /**
           * 发送温度
           */
          public static final byte[] AIR_TEM = {(byte)0x06,(byte)0x00,(byte)0x08,(byte)0x08,(byte)0x00}; // 06 xx xx

          /**
           * 风速 0:自动 1：低速 2：中速 3：高速
           */
          public static final byte[][] AIR_WS = {
               {(byte)0x07,(byte)0x00,(byte)0x08,(byte)0x08,(byte)0x00},
               {(byte)0x07,(byte)0x01,(byte)0x08,(byte)0x08,(byte)0x00},
               {(byte)0x07,(byte)0x02,(byte)0x08,(byte)0x08,(byte)0x00},
               {(byte)0x07,(byte)0x03,(byte)0x08,(byte)0x08,(byte)0x00}}; // 06 xx xx

          /**
           * 风向 0:自动 1：手动
           */
          public static final byte[][] AIR_WD={
               {(byte)0x08,(byte)0x00,(byte)0x08,(byte)0x08,(byte)0x00},
               {(byte)0x08,(byte)0x01,(byte)0x08,(byte)0x08,(byte)0x00}
          };

          /**
           * 发送遥控类型
           */
          public static final byte[] AIR_TYPE = {(byte)0x02,(byte)0x00,(byte)0x08,(byte)0x08,(byte)0x00};// 02 xx xx
          /**
           * 初始化自动
           */
          public static final byte[] AIR_INITAUTO = {(byte)0xAA,(byte)0xAA,(byte)0x08,(byte)0x08,(byte)0x00};// AA AA 08

          /**
           * 初始化结束
           */
          public static final byte[] AIR_INITOVER = {(byte)0xCC,(byte)0xCC,(byte)0x08,(byte)0x08,(byte)0x00};// CC CC 08

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

          /***
           * 生成发送的数据
           * @param head      数据头
           * @param brand     品牌
           * @return          指令数据
           */
          public static final byte[] getAirControlDate(byte[] head,int brand){
                    byte[] set_type ={(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x00};
                    byte[] brands;
                              if (brand <255) {//1字节转化
                                        brands = ConvertUtil.intToByteArray(brand);
                                        set_type[2]=brands[3];
                              } else {//2字节转化
                                        brands = ConvertUtil.intToByteArray(brand);
                                        set_type[1]=brands[2];
                                        set_type[2]=brands[3];
                              }
                              // TODO: 2017/7/7  空调类型异或校验
                              set_type[4]= (byte) (set_type[0]^set_type[1]^set_type[2]^set_type[3]);
                    return ConvertUtil.byteMerger(head,set_type);
          }

}
