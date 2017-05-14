package com.property.activity;

import java.util.List;

public class HuodongListEntity {
	private int status; // int 查询状态（0失败 1成功）
	private String msg; // string 提示信息
	private List<active_list> active_list; // array 活动记录数组

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

	public List<active_list> getActive_list() {
		return active_list;
	}

	public void setActive_list(List<active_list> active_list) {
		this.active_list = active_list;
	}

	public class active_list {
		private String id; // int 活动id
		private String title; // string 活动标题
		private String cat_id; // int 文章分类id
		private String cat_name; // string 分类名称
		private String add_time; // string 活动添加时间
		private String start_time; // string 活动开始时间
		private String end_time; // string 活动结束时间
		private String img; // string 活动图片

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getCat_id() {
			return cat_id;
		}

		public void setCat_id(String cat_id) {
			this.cat_id = cat_id;
		}

		public String getCat_name() {
			return cat_name;
		}

		public void setCat_name(String cat_name) {
			this.cat_name = cat_name;
		}

		public String getAdd_time() {
			return add_time;
		}

		public void setAdd_time(String add_time) {
			this.add_time = add_time;
		}

		public String getStart_time() {
			return start_time;
		}

		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

	}
}
