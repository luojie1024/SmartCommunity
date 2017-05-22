package com.property.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/5/17 19:28
 */
public class BaoxiushenqingEntity  extends BmobObject {
    private String date;//时间
    private String user_id;//用户名
    private String mobile;//电话
    private String village_id;//铺位
    private String title;//标题
    private String content;//内容
    private  List<String> imageUrls;//图片Url

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }
}
