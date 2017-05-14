package com.property.activity;

import java.util.List;

public class AnnouncementListEntity {
	private String page_num; // int 当前页码
	private String page_size;
	private String cat_id; // int 查询公告分类
	private String count; // int 所有公告数量
	private List<list> list; // array 公告记录数组
	private List<ad_top> ad_top; // array 广告数组

	public String getPage_num() {
		return page_num;
	}

	public void setPage_num(String page_num) {
		this.page_num = page_num;
	}

	public String getPage_size() {
		return page_size;
	}

	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<list> getList() {
		return list;
	}

	public void setList(List<list> list) {
		this.list = list;
	}

	public List<ad_top> getAd_top() {
		return ad_top;
	}

	public void setAd_top(List<ad_top> ad_top) {
		this.ad_top = ad_top;
	}

	public class list {
		private String aid; // int 公告id
		private String title; // string 公告标题
		private String cat_name; // string 分类名称
		private String tag; // string 标签
		private String update_time; // int 公告添加时间
		private String img; // string 公告图片

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

		public String getCat_name() {
			return cat_name;
		}

		public void setCat_name(String cat_name) {
			this.cat_name = cat_name;
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

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

	}

	public class ad_top {
		private String ad_id; // int 广告id
		private String content; // string 图片地址
		private String url; // string 链接
		private String ad_name; // string 广告名称

		public String getAd_id() {
			return ad_id;
		}

		public void setAd_id(String ad_id) {
			this.ad_id = ad_id;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getAd_name() {
			return ad_name;
		}

		public void setAd_name(String ad_name) {
			this.ad_name = ad_name;
		}

	}
}
