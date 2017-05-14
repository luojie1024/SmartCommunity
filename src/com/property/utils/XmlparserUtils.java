package com.property.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import com.property.bean.Inform;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2016/2/4 13:34
 */
public class XmlparserUtils {
          public static List<Inform> parserXml(InputStream in) {
                    List<Inform> informLists=null;
                    try {
                              Inform inform = null;
                              //[1]获取解析器
                              XmlPullParser parser = Xml.newPullParser();
                              //[2]设置解析器要解析的内容
                              parser.setInput(in, "utf-8");
                              //[3]获取解析事件
                              int type = parser.getEventType();
                              //[4]不停的向下解析
                              while (type != XmlPullParser.END_DOCUMENT) {
                                        //[5]具体判断一下解析的事哪个开始标签
                                        switch (type) {
                                                  case XmlPullParser.START_TAG:
                                                            //解析开始节点
                                                            if ("channel".equals(parser.getName())) {
                                                                      //创建一个List集合
                                                                      informLists = new ArrayList<Inform>();
                                                            } else if ("inform".equals(parser.getName())) {
                                                                      //创建一个news对象
                                                                      inform = new Inform();
                                                            } else if ("title".equals(parser.getName())) {
                                                                      inform.setTitle(parser.nextText());

                                                            } else if ("description".equals(parser.getName())) {
                                                                      inform.setDescription(parser.nextText());

                                                            } else if ("image".equals(parser.getName())) {
                                                                      inform.setImage(parser.nextText());

                                                            } else if ("text".equals(parser.getName())) {
                                                                      inform.setText(parser.nextText());

                                                            } else if ("time".equals(parser.getName())) {
                                                                      inform.setTime(parser.nextText());

                                                            }

                                                            break;
                                                  case XmlPullParser.END_TAG:
                                                            if ("inform".equals(parser.getName())) {
                                                                      //把javabean添加到集合
                                                                      informLists.add(inform);
                                                            }

                                                            break;
                                        }
                                        //不停的向下解析
                                        type=parser.next();
                              }
                    } catch (Exception e) {
                              e.printStackTrace();
                    }
                    return informLists;
          }


}
