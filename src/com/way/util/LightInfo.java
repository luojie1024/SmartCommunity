package com.way.util;

import android.util.Log;

public class LightInfo {

	private int light_id=0;
	private String light_name="";
	private boolean light_state=false;
	public int getLight_id() {
		return light_id;
	}
	public void setLight_id(int light_id) {
		this.light_id = light_id;
	}
	public String getLight_name() {
		return light_name;
	}
	public void setLight_name(String light_name) {
		this.light_name = light_name;
	}
	public boolean isLight_state() {
		return light_state;
	}
	public void setLight_state(boolean light_state) {
		this.light_state = light_state;
	}
	
	
	
}
