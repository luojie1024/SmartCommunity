package com.way.util;

public class Alertinfo {
	private int _id;
	private String  name;
	private  String time;
	private  String bindgiz;
	private String userid;
	private int flag;
	
	public Alertinfo(int id, String name, String time, String bindgiz,
			String userid, int flag) {
		super();
		this._id = id;
		this.name = name;
		this.time = time;
		this.bindgiz = bindgiz;
		this.userid = userid;
		this.flag = flag;
	}

	public Alertinfo(String name, String time, String bindgiz, String userid,
			int flag) {
		super();
		this.name = name;
		this.time = time;
		this.bindgiz = bindgiz;
		this.userid = userid;
		this.flag = flag;
	}

	public Alertinfo() {
		super();
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBindgiz() {
		return bindgiz;
	}

	public void setBindgiz(String bindgiz) {
		this.bindgiz = bindgiz;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "Alertinfo [id=" + _id + ", name=" + name + ", time=" + time
				+ ", bindgiz=" + bindgiz + ", userid=" + userid + ", flag="
				+ flag + "]";
	}
	
	
	
	
	
	
	

}
