package com.LifeServices.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 商店列表
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/5/24 22:22
 */
public class Shop extends BmobObject implements Serializable {

//          private static final long serialVersionUID = -8796635595320697255L;

          private String userID; 		// 主人
//          private String type; 		// 类型(11代表第一个GridView中的第一个)
          private String name; 		// 店名
          private String location; 	// 地理位置
          private String phone;		// 联系电话
          private String info; 		// 简介
          private String sale; 		// 促销信息
          private BmobFile picShop; 	// 商店主图

          public String getUserID() {
                    return userID;
          }

          public void setUserID(String userID) {
                    this.userID = userID;
          }

//          public String getType() {
//                    return type;
//          }
//
//          public void setType(String type) {
//                    this.type = type;
//          }

          public String getName() {
                    return name;
          }

          public void setName(String name) {
                    this.name = name;
          }

          public String getLocation() {
                    return location;
          }

          public void setLocation(String location) {
                    this.location = location;
          }

          public String getPhone() {
                    return phone;
          }

          public void setPhone(String phone) {
                    this.phone = phone;
          }

          public String getInfo() {
                    return info;
          }

          public void setInfo(String info) {
                    this.info = info;
          }

          public String getSale() {
                    return sale;
          }

          public void setSale(String sale) {
                    this.sale = sale;
          }

}
