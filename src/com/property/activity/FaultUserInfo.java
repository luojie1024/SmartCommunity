package com.property.activity;

import java.util.List;

public class FaultUserInfo {
	private int statusp; // int 接口处理状态 成功1 无数据0
	private String msg; // string 接口返回信息
	private info info; // array 业主信息数组

	public int getStatusp() {
		return statusp;
	}

	public void setStatusp(int statusp) {
		this.statusp = statusp;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public info getInfo() {
		return info;
	}

	public void setInfo(info info) {
		this.info = info;
	}

	public class info {
		private String name; // string 业主姓名
		private String mobile; // string 业主电话
		private String berth; // string 铺位

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getBerth() {
			return berth;
		}

		public void setBerth(String berth) {
			this.berth = berth;
		}

	}
}
