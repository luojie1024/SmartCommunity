package com.property.activity;

import java.util.List;

public class JiaofeifenleiEntity {
	private int status; // int 查询状态（0无数据 1成功）
	private String msg; // string 提示信息
	private List<pay_genre> pay_genre; // array 缴费类型数组

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

	public List<pay_genre> getPay_genre() {
		return pay_genre;
	}

	public void setPay_genre(List<pay_genre> pay_genre) {
		this.pay_genre = pay_genre;
	}

	public class pay_genre {
		private String id; // int 缴费类型id
		private String name; // string 缴费类型名称
		private String img; // string 缴费类型图片

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

	}
}
