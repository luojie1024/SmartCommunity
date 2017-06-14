package com.way.util;

/**
 * Created by CHR on 2017/6/14.
 */

public class CurtainInfo {
    private int _id;
    private String  name;
    private  String address;
    private  String bindgiz;
    private String userid;
    private int flag;
    public CurtainInfo(int id, String name, String address,String bindgiz, String userid,int flag) {
        super();
        this._id = id;
        this.name = name;
        this.address = address;
        this.bindgiz=bindgiz;
        this.userid=userid;
        this.flag = flag;
    }

    public CurtainInfo(String name, String address,String bindgiz, String userid,int flag) {
        super();
        this.name = name;
        this.address = address;
        this.bindgiz=bindgiz;
        this.flag = flag;
        this.userid=userid;
    }
    public CurtainInfo() {
        super();
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
