package com.LifeServices.bean;

import cn.bmob.v3.BmobObject;

/**
 * 订单实体类
 * @date 2014-4-24
 * @author Stone
 */
@SuppressWarnings("serial")
public class Order extends BmobObject {

	private String userName;
	private String goodID; 		// 商品ID
	private String goodName;
	private String shopID; 		// 商店ID
	private String shopName;
	private String count; 		// 数量
	private String price;       // 价格
	private String time;		// 取餐时间
	private String phone;		// 联系电话
	private String state = "未取餐"; 		// 订单状态(已取, 未取)
	private String tips; 		// 附加信息
	
	public String getGoodID() {
		return goodID;
	}
	public String getGoodName() {
		return goodName;
	}
	public String getUserName() {
		return userName;
	}
	public String getShopID() {
		return shopID;
	}
	public String getShopName() {
		return shopName;
	}
	public String getCount() {
		return count;
	}
	public String getPrice() {
		return price;
	}
	public String getTime() {
		return time;
	}
	public String getPhone() {
		return phone;
	}
	public String getState() {
		return state;
	}
	public String getTips() {
		return tips;
	}
	public void setGoodID(String goodID) {
		this.goodID = goodID;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}


}
