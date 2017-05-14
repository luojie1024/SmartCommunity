package com.way.util;

public class Allmesinfo {
	
	private int id;
	private String name;
	private String devicename;
	private String mes;
	public Allmesinfo(int id, String name, String devicename, String mes) {
		super();
		this.id = id;
		this.name = name;
		this.devicename = devicename;
		this.mes = mes;
	}
	public Allmesinfo() {
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
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	

}
