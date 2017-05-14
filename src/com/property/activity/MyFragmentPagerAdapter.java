package com.property.activity;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragmentList;

	public MyFragmentPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if (position < fragmentList.size()) {
			fragment = fragmentList.get(position);
		} else {
			fragment = fragmentList.get(0);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
