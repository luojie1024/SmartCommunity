package com.property.utils;

import android.os.Handler;
import android.os.Message;

import com.property.activity.JiaofeiListEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/5/14 15:44
 * 查询mysql数据库中的数据
 */
public class QueryAll {
          public static final String DB_Url = "jdbc:mysql://192.168.31.145:3306/wysql";
          public static final String DB_UesrName = "root";
          public static final String DB_PassWord = "123456";
          private static JiaofeiListEntity jiaofeiListEntity;
          private static Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                              super.handleMessage(msg);
                              switch (msg.arg1) {
                                        case 0:
                                                  break;

                                        case 1:
                                                  System.out.println("查询完毕！");
                                                  break;
                              }

                    }
          };
          /***
           * @param type 记录类型 1水费 2电费 ....
           * @throws Exception
           */
          public static JiaofeiListEntity Query_record(int type) throws Exception {
                    jiaofeiListEntity = new JiaofeiListEntity();
                    new Thread() {
                              @Override
                              public void run() {
                                        super.run();
                                        try {
                                                  // 1.注册驱动
                                                  Class.forName("com.mysql.jdbc.Driver");
                                                  // 2.获取连接
                                                  Connection conn = null;
                                                  conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sysql", "joahluo", "456789");
                                                  // 3.创建执行sql语句的对象
                                                  Statement stmt = null;
                                                  stmt = conn.createStatement();
                                                  // 4.书写一个sql语句
                                                  String sql = "SELECT * FROM pay_record";
                                                  // 5.执行sql语句
                                                  ResultSet rs = null;
                                                  rs = stmt.executeQuery(sql);
                                                  // 6.对结果集进行处理
                                                  if (rs.next()) {
                                                            System.out.println("恭喜您，登录成功!");
                                                            System.out.println(sql);
                                                            System.out.println(rs.getString(0) + ":" + rs.getString(1) + " ：" + rs.getString(2));
                                                  } else {
                                                            //无数据设置为空
                                                            jiaofeiListEntity.setStatus(0);
                                                            System.out.println("账号或密码错误!");
                                                  }
                                                  if (rs != null)
                                                            rs.close();
                                                  if (stmt != null)
                                                            stmt.close();
                                                  if (conn != null)
                                                            conn.close();

                                                  //c查询完毕
                                                  Message msg = handler.obtainMessage();
                                                  msg.arg1 =1;
                                                  handler.sendMessage(msg);

                                        } catch (ClassNotFoundException e) {
                                                  System.out.println("没有发现类！");
                                                  e.printStackTrace();
                                        } catch (SQLException e) {
                                                  System.out.println("连接数据库错误！");
                                                  e.printStackTrace();
                                        }
                              }
                    }.start();
                    return null;
          }
}
