package com.way.tabui.actity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import com.way.tabui.gokit.R;

public class MainPersonalFragment extends Fragment implements
		OnCheckedChangeListener {
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private String mCurrentFragmentTag;
	RadioButton mFeedListRadioButton;
	RadioButton mResouseRadioButton;
	RadioButton mTransFileRadioButton;
	int saveswich=R.id.rb_feedlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getActivity().getSupportFragmentManager();
	}

	@Override
	public void onResume() {
		super.onResume();
//		onCheckedChanged(mFeedListRadioButton, true);
//		mFeedListRadioButton.setChecked(true);
		sclcetpage(saveswich);
	}
	
	public void sclcetpage(int id){
		 switch (id) {
		 case R.id.rb_feedlist:
			 onCheckedChanged(mFeedListRadioButton, true);
			    mFeedListRadioButton.setChecked(true);
				mCurrentFragmentTag = getActivity().getResources().getString(
						R.string.tab_feedlist);
				break;
			case R.id.rb_resource:
				 onCheckedChanged(mResouseRadioButton, true);
				 mResouseRadioButton.setChecked(true);
					mCurrentFragmentTag = getActivity().getResources().getString(
							R.string.resource_launcher_title);
				
				break;
			case R.id.rb_transferlist_left:
				 onCheckedChanged(mTransFileRadioButton, true);
				 mTransFileRadioButton.setChecked(true);
					mCurrentFragmentTag = getActivity().getResources().getString(
							R.string.video_widget_title);
				//switchFragmen(getActivity().getString(R.string.tab_transferlist));
				break;
			default:
				break;
		}
		 
	 }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_personal_page, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mFeedListRadioButton = (RadioButton) view
				.findViewById(R.id.rb_feedlist);
		mResouseRadioButton = (RadioButton) view.findViewById(R.id.rb_resource);
		mTransFileRadioButton = (RadioButton) view
				.findViewById(R.id.rb_transferlist_left);
		mFeedListRadioButton.setOnCheckedChangeListener(this);
		mResouseRadioButton.setOnCheckedChangeListener(this);
		mTransFileRadioButton.setOnCheckedChangeListener(this);
	}

	protected FragmentTransaction ensureTransaction() {
		if (mFragmentTransaction == null) {
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}

		return mFragmentTransaction;
	}

	private Fragment getFragment(String tag) {
		Fragment f = mFragmentManager.findFragmentByTag(tag);
		if (f == null) {
			// 在这里判断tag,不同则实例化对应的fragment，
			f = SampleFragment.newInstance(tag, MainActivity.PAGE_PERSONAL);
		}

		return f;
	}

	protected void attachFragment(int layout, Fragment f, String tag) {
		if (f != null) {
			if (f.isDetached()) {
				ensureTransaction();
				mFragmentTransaction.attach(f);
			} else if (!f.isAdded()) {
				ensureTransaction();
				mFragmentTransaction.add(layout, f, tag);
			}
		}
	}

	protected void detachFragment(Fragment f) {
		if (f != null && !f.isDetached()) {
			ensureTransaction();
			mFragmentTransaction.detach(f);
		}
	}

	protected void commitTransactions() {
		if (mFragmentTransaction != null && !mFragmentTransaction.isEmpty()) {
			mFragmentTransaction.commit();
			mFragmentTransaction = null;
		}
	}

	private void switchFragmen(String tag) {
		if (TextUtils.equals(mCurrentFragmentTag, tag))
			return;
		if (mCurrentFragmentTag != null)
			detachFragment(getFragment(mCurrentFragmentTag));
		attachFragment(R.id.sliding_layer_frame_left, getFragment(tag), tag);
		mCurrentFragmentTag = tag;
		commitTransactions();

	}

	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (!isChecked)
			return;
		Log.i("way", "mCurrentFragmentTag = " + mCurrentFragmentTag);
		switch (buttonView.getId()) {
		case R.id.rb_feedlist:
			saveswich=R.id.rb_feedlist;
			switchFragmen(getActivity().getString(R.string.tab_feedlist));
			break;
		case R.id.rb_resource:
			saveswich=R.id.rb_resource;
			switchFragmen(getActivity().getString(
					R.string.resource_launcher_title));
			break;
		case R.id.rb_transferlist_left:
			saveswich=R.id.rb_transferlist_left;
			switchFragmen(getActivity().getString(R.string.video_widget_title));
			break;
		default:
			break;
		}
	}
}
