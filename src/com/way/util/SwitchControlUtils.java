package com.way.util;

import static com.way.util.ConvertUtil.hexStringToByte;

/**
 * Auther：joahluo
 * E-mail：joahluo@163.com
 * Time：2017/7/22 10:24
 */
public class SwitchControlUtils {
          private byte[] BYTES_BESE = {(byte) 0x02,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x00, (byte) 0x00};
          //一位开关
          public static final byte SWITCH_ONE=0x00;
          //二位开关
          public static final byte SWITCH_TWO=0x01;
          //三位开关
          public static final byte SWITCH_THREE=0x02;
          //四位开关
          public static final byte SWITCH_FOUR=0x03;
          //五孔插座
          public static final byte PLUG_FIVE=0x10;
          //五孔86插座
          public static final byte PLUG_FIVE_86=0x11;

          //设置地址
          public void setSwitchAddress(String address) {
                    byte[] bytes = hexStringToByte(address);
                    for (int i=0;i<4;i++) {
                              BYTES_BESE[1+i]=bytes[i];
                    }
                    //填充长度
                    BYTES_BESE[6]=0;
          }
          //设置开关类型
          public void setSwitchType(int type) {
                    switch (type) {
                              case 1://一位开关
                                        BYTES_BESE[0]=SWITCH_ONE;
                                        break;
                              case 2://两位开关
                                        BYTES_BESE[0]=SWITCH_TWO;
                                        break;
                              case 3://三位开关
                                        BYTES_BESE[0]=SWITCH_THREE;
                                        break;
                              case 4://五孔插座
                                        BYTES_BESE[0]=PLUG_FIVE;
                                        break;
                    }
          }



          //获得指令
          public byte[] getControlCommand(boolean isChecked,int no_switch) {
                    if (isChecked) {//开
                              if (no_switch == 1) {
                                        if (BYTES_BESE[0] == SWITCH_ONE) {
                                                  BYTES_BESE[5]=0x1;
                                        } else if (BYTES_BESE[0] == SWITCH_TWO) {
                                                  BYTES_BESE[5]=0x3;
                                        }else if (BYTES_BESE[0] == SWITCH_THREE) {
                                                  BYTES_BESE[5]=0x7;
                                        };
                              } else if (no_switch==2) {
                                        if (BYTES_BESE[0] == SWITCH_TWO) {
                                                  BYTES_BESE[5]=0x5;
                                        } else if (BYTES_BESE[0] == SWITCH_THREE) {
                                                  BYTES_BESE[5]=0x9;
                                        }
                              } else if (no_switch==3) {
                                        BYTES_BESE[5]=0xB;
                              } else if (no_switch==4) {
                                        BYTES_BESE[5]=0xD;
                              }

                    } else {//关
                              if (no_switch == 1) {
                                        if (BYTES_BESE[0] == SWITCH_ONE) {
                                                  BYTES_BESE[5]=0x2;
                                        } else if (BYTES_BESE[0] == SWITCH_TWO) {
                                                  BYTES_BESE[5]=0x4;
                                        }else if (BYTES_BESE[0] == SWITCH_THREE) {
                                                  BYTES_BESE[5]=0x8;
                                        }
                              } else if (no_switch == 2) {
                                        if (BYTES_BESE[0] == SWITCH_TWO) {
                                                  BYTES_BESE[5]=0x6;
                                        } else if (BYTES_BESE[0] == SWITCH_THREE) {
                                                  BYTES_BESE[5]=0xA;
                                        }
                              } else if (no_switch == 3) {
                                        BYTES_BESE[5]=0xC;
                              } else if (no_switch==4) {
                                        BYTES_BESE[5]=0xE;
                              }

                    }
                    return BYTES_BESE;
          }

}
