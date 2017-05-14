package com.property.bean;

import java.util.List;

public class FaultListEntity {
	private List<data> data; // array 数组
	
	public List<data> getData() {
		return data;
	}

	public void setList(List<data> data) {
		this.data = data;
	}

	public class data {
		private String fault_id; // int 报障id
		private String user_id; // int 用户id
		private String user_name; // string 用户名字
		private String title; // string 报障标题
		private String content; // string 报障内容
		private String add_time; // string 时间
		private String update_time; //string 维修最后更新时间
		private String worker_id; // int 维修工人id
		private String worker_name; // string 维修工人名字
		private String status; // int 状态 待分配0，待维修1，维修中2，待验收3，已完成4
		private List<gallery> gallery; // array 图片数组

		public String getFault_id() {
			return fault_id;
		}

		public void setFault_id(String fault_id) {
			this.fault_id = fault_id;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getAdd_time() {
			return add_time;
		}

		public void setAdd_time(String add_time) {
			this.add_time = add_time;
		}

		public String getUpdate_time() {
			return update_time;
		}

		public void setUpdate_time(String update_time) {
			this.update_time = update_time;
		}

		public String getWorker_id() {
			return worker_id;
		}

		public void setWorker_id(String worker_id) {
			this.worker_id = worker_id;
		}

		public String getWorker_name() {
			return worker_name;
		}

		public void setWorker_name(String worker_name) {
			this.worker_name = worker_name;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public List<gallery> getGallery() {
			return gallery;
		}

		public void setGallery(List<gallery> gallery) {
			this.gallery = gallery;
		}

		public class gallery {
			private String id; // int 图片id
			private String img_source; // string 图片地址

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getImg_source() {
				return img_source;
			}

			public void setImg_source(String img_source) {
				this.img_source = img_source;
			}

		}
	}
}
