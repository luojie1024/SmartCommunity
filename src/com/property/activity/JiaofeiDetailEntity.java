package com.property.activity;

public class JiaofeiDetailEntity {
	private int status; // int 查询状态（0暂无数据 1成功）
	private String msg; // string 提示信息
	private pay_record pay_record; // array 缴费记录数组

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public pay_record getPay_record() {
		return pay_record;
	}

	public void setPay_record(pay_record pay_record) {
		this.pay_record = pay_record;
	}

	public class pay_record {
		private String id; // int 缴费记录id
		private String pay_amount; // float 缴费金额
		private String payee; // string 缴费单位
		private String account_name; // string 缴费户号（只有水电费有）
		private String username; // string 缴费户名
		private String pay_time; // string 缴费时间

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPay_amount() {
			return pay_amount;
		}

		public void setPay_amount(String pay_amount) {
			this.pay_amount = pay_amount;
		}

		public String getPayee() {
			return payee;
		}

		public void setPayee(String payee) {
			this.payee = payee;
		}

		public String getAccount_name() {
			return account_name;
		}

		public void setAccount_name(String account_name) {
			this.account_name = account_name;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPay_time() {
			return pay_time;
		}

		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}

	}
}
