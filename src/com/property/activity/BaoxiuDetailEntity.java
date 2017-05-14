package com.property.activity;

import java.util.List;

public class BaoxiuDetailEntity {
	private data data; // array 数组

	public data getData() {
		return data;
	}

	public void setData(data data) {
		this.data = data;
	}

	public class data {
		private String fault_id; // int 报障id
		private String user_id; // int 用户id
		private String user_name; // string 用户名字
		private String title; // string 报障标题
		private String content; // string 报障内容
		private String add_time; // string 报障时间
		private String update_time; // string 维修最后更新时间
		private String worker_id; // int 维修人员id
		private String worker_name; // string 维修人员名字
		private String fault_status; // int 报障状态
		private List<gallery> gallery; // array 图片数组
		private List<history> history; // array 报障进度数组

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

		public String getFault_status() {
			return fault_status;
		}

		public void setFault_status(String fault_status) {
			this.fault_status = fault_status;
		}

		public List<gallery> getGallery() {
			return gallery;
		}

		public void setGallery(List<gallery> gallery) {
			this.gallery = gallery;
		}

		public List<history> getHistory() {
			return history;
		}

		public void setHistory(List<history> history) {
			this.history = history;
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

		public class history {
			private String id; // int 报障进度id
			private String content; // string 进度信息
			private String from_status; // int 变更前状态
			private String to_status; // int 变更后状态
			private String user_id; // int 评论时间
			private String username; // string 回复内容
			private String create_time; // string 进度处理时间

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			public String getFrom_status() {
				return from_status;
			}

			public void setFrom_status(String from_status) {
				this.from_status = from_status;
			}

			public String getTo_status() {
				return to_status;
			}

			public void setTo_status(String to_status) {
				this.to_status = to_status;
			}

			public String getUser_id() {
				return user_id;
			}

			public void setUser_id(String user_id) {
				this.user_id = user_id;
			}

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getCreate_time() {
				return create_time;
			}

			public void setCreate_time(String create_time) {
				this.create_time = create_time;
			}

		}
	}
}
