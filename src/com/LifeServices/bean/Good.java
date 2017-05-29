package com.LifeServices.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 商品实体类
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/5/25 11:24
 */

public class Good extends BmobObject implements Serializable {
	

	//private String id;  商品ID, 默认
	
	private String shopID = ""; 		// 商店ID

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	private String shopName = "";		//商店名称
	private String name = ""; 		// 名称
	private String price = ""; 		// 价格
	
	private BmobFile picGood = null; 	// 商品主图
	
	public Good(String name, String price) {
		this.name = name;
		this.price  = price;
	}
	
	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
