package com.property.activity;

import java.util.List;

public class JiaofeiListEntity {
	private int status; // int 查询状态（0暂无数据 1成功）
	private String msg; // string 提示信息
	private List<pay_record> pay_record; // array 缴费记录数组

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

	public List<pay_record> getPay_record() {
		return pay_record;
	}

	public void setPay_record(List<pay_record> pay_record) {
		this.pay_record = pay_record;
	}

	public class pay_record {
		private String id; // int 缴费记录id
		private String type;// 缴费类型id
		private String pay_time; // string 缴费时间(未交费则为空)
		private String pay_amount; // float 缴费金额
		private String month; // int 月份

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPay_time() {
			return pay_time;
		}

		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}

		public String getPay_amount() {
			return pay_amount;
		}

		public void setPay_amount(String pay_amount) {
			this.pay_amount = pay_amount;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}
}
