package com.property.activity;

public class AnnouncementInfoEntity {
	private int status; // 查询状态 (0失败 1成功)
	private String msg; // string 提示信息
	private announcement_info announcement_info; // array 公告详情数组

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

	public announcement_info getAnnouncement_info() {
		return announcement_info;
	}

	public void setAnnouncement_info(announcement_info announcement_info) {
		this.announcement_info = announcement_info;
	}

	public class announcement_info {
		private String aid; // int 公告id
		private String title; // string 文章标题
		private String cat_id; // int 公告分类id
		private String cat_name; // string 分类名称
		private String summary; // string 公告摘要
		private String content; // string 公告内容
		private String tag; // string 公告标签
		private String update_time; // string 公告修改时间
		private String look_count; // int 公告浏览量
		private String img; // string 图片地址

		public String getAid() {
			return aid;
		}

		public void setAid(String aid) {
			this.aid = aid;
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

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getUpdate_time() {
			return update_time;
		}

		public void setUpdate_time(String update_time) {
			this.update_time = update_time;
		}

		public String getLook_count() {
			return look_count;
		}

		public void setLook_count(String look_count) {
			this.look_count = look_count;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

	}
}
