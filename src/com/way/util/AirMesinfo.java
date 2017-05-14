package com.way.util;

public class AirMesinfo {
	private int _id;
	private String  name;
	private int brand;
	private int temperature;
	private int mode;
	private int speed;
	private int direction;
	private  String bindgiz;
	private String userid;
	private int flag;
	
	public AirMesinfo(int _id, String name, int brand, int temperature,
			int mode, int speed, int direction, String bindgiz, String userid,
			int flag) {
		super();
		this._id = _id;
		this.name = name;
		this.brand = brand;
		this.temperature = temperature;
		this.mode = mode;
		this.speed = speed;
		this.direction = direction;
		this.bindgiz = bindgiz;
		this.userid = userid;
		this.flag = flag;
	}
	
	public AirMesinfo() {
		super();
	}
	
	public AirMesinfo(String name, int brand, int temperature, int mode,
			int speed, int direction, String bindgiz, String userid, int flag) {
		super();
		this.name = name;
		this.brand = brand;
		this.temperature = temperature;
		this.mode = mode;
		this.speed = speed;
		this.direction = direction;
		this.bindgiz = bindgiz;
		this.userid = userid;
		this.flag = flag;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBrand() {
		return brand;
	}
	public void setBrand(int brand) {
		this.brand = brand;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
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
		return "AirMesinfo [_id=" + _id + ", name=" + name + ", brand=" + brand
				+ ", temperature=" + temperature + ", mode=" + mode
				+ ", speed=" + speed + ", direction=" + direction
				+ ", bindgiz=" + bindgiz + ", userid=" + userid + ", flag="
				+ flag + "]";
	}
	
	

}
