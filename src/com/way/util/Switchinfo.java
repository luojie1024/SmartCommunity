package com.way.util;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/6/15 13:54
 */
public class SwitchInfo {
          public int get_id() {
                    return _id;
          }

          public void set_id(int _id) {
                    this._id = _id;
          }

          public String getAddress() {
                    return address;
          }

          public void setAddress(String address) {
                    this.address = address;
          }

          public String getBindgiz() {
                    return bindgiz;
          }

          public void setBindgiz(String bindgiz) {
                    this.bindgiz = bindgiz;
          }

          public int getFlag() {
                    return flag;
          }

          public void setFlag(int flag) {
                    this.flag = flag;
          }

          public String getName() {
                    return name;
          }

          public void setName(String name) {
                    this.name = name;
          }

          public int getType() {
                    return type;
          }

          public void setType(int type) {
                    this.type = type;
          }

          public String getUserid() {
                    return userid;
          }

          public void setUserid(String userid) {
                    this.userid = userid;
          }

          public SwitchInfo() {
                    super();
          }

          private int _id;
          private String name;
          private String address;
          private String bindgiz;
          private String userid;
          private int flag;
          private int type;

          public SwitchInfo(int id, String name, String address, String bindgiz, String userid, int flag, int type) {
                    super();
                    this._id = id;
                    this.name = name;
                    this.address = address;
                    this.bindgiz=bindgiz;
                    this.userid=userid;
                    this.flag = flag;
                    this.type = type;
          }

          public SwitchInfo(String name, String address, String bindgiz, String userid, int flag, int type) {
                    super();
                    this.name = name;
                    this.address = address;
                    this.bindgiz=bindgiz;
                    this.flag = flag;
                    this.userid=userid;
                    this.type = type;
          }
}
