package com.way.util;

import com.way.adapter.DatabaseAdapter;

/**
 * Auther：joahluo
 * E-mail：joahluo@163.com
 * Time：2017/7/16 14:41
 */
public class SmartSwitchUtils {
          public final  static SwitchInfo controlProtocolResolve(byte[] bytes, DatabaseAdapter dbAdapter,int sum){
                    SwitchInfo switchInfo=new SwitchInfo();
                    byte[] deviceId = new byte[4];
                    for (int i = 0; i < 10; i++) {
                              if (((bytes[5 + 6 * i] & 0xf0) == 0xf0)) {

                                        for (int j = 0; j < 4; j++) {
                                                  deviceId[j] = bytes[j + 1 + i * 6];
                                        }
                                        String mac = ConvertUtil.byteStringToHexString(deviceId).toUpperCase();
                                        //获取数据库数据
                                        switchInfo = dbAdapter.findSwitchInfoStatus(mac);
                                        //开关类型 状态
                                        switch ((int) bytes[0 + 6 * i]) {
                                                  case (int) ControlProtocol.DevType.SWITCH_THREE:
                                                            if ((bytes[5 + 6 * i] & 0x4) == 0x4) {
                                                                      switchInfo.setStatus3(1);
                                                            } else {
                                                                      switchInfo.setStatus3(0);
                                                            }
                                                  case (int) ControlProtocol.DevType.SWITCH_TWO:
                                                            if ((bytes[5 + 6 * i] & 0x2) == 0x2) {
                                                                      switchInfo.setStatus2(1);
                                                            } else {
                                                                      switchInfo.setStatus2(0);
                                                            }
                                                  case (int) ControlProtocol.DevType.SWITCH_ONE:
                                                            if (mac.equals("00000000")) {
                                                                      break;
                                                            }
                                                            if ((bytes[5 + 6 * i] & 0x1) == 0x1) {
                                                                      switchInfo.setStatus1(1);
                                                            } else {
                                                                      switchInfo.setStatus1(0);
                                                            }
                                                            sum++;
                                                            break;
                                                  case (int) ControlProtocol.DevType.PLUG_FIVE:
                                                            if ((bytes[5 + 6 * i] & 0x1) == 0x1) {
                                                                      switchInfo.setStatus1(1);
                                                            } else {
                                                                      switchInfo.setStatus1(0);
                                                            }
                                                            sum++;
                                                            break;
                                        }
                              }

                    }
                    return switchInfo;
          }
}
