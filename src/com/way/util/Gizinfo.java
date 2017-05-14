package com.way.util;

public class Gizinfo {
	private int id;
	private String  name;
	private  String address;
	private  String bindgiz;
	private String userid;
	private int flag;
	public Gizinfo(int id, String name, String address,String bindgiz, String userid,int flag) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.bindgiz=bindgiz;
		this.userid=userid;
		this.flag = flag;
	}
	
	public Gizinfo(String name, String address,String bindgiz, String userid,int flag) {
		super();
		this.name = name;
		this.address = address;
		this.bindgiz=bindgiz;
		this.flag = flag;
		this.userid=userid;
	}
	
	public String getBindgiz() {
		return bindgiz;
	}
	public void setBindgiz(String bindgiz) {
		this.bindgiz = bindgiz;
	}
	public Gizinfo() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		return "Gizinfo [id=" + id + ", name=" + name + ", address=" + address+ ", bindgiz=" + bindgiz
				+", uesrid=" + userid+ ", flag=" + flag + "]";
	}
	
	
	
	

}
