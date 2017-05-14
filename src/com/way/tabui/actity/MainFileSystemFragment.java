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
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import com.way.tabui.gokit.R;

/**
 * 文件系统的Fragment
 * 由于与另一个Fragment是对称的，我就只注释此类了，敬请谅解!
 * @author way
 * 
 */
public class MainFileSystemFragment extends Fragment implements OnCheckedChangeListener {
	private FragmentManager mFragmentManager;// Fragment管理类
	private FragmentTransaction mFragmentTransaction;
	private String mCurrentFragmentTag;// 当前显示的二级Fragment标签
	// 底部三个Tab的按钮
	RadioButton mFileListRadioButton;
	RadioButton mToolsRadioButton;
	RadioButton mTransFileRadioButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化FragmentManager，再次提醒需要Activity继承FragmentActivity
		mFragmentManager = getActivity().getSupportFragmentManager();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 默认显示Tab为第一个，其实可以记住一下状态onSaveInstanceState，我为简洁未作处理
		sclcetpage(saveswich);
	}

	
	 public void sclcetpage(int id){
		 switch (id) {
		 case R.id.rb_filelist:
			 onCheckedChanged(mFileListRadioButton, true);
				mFileListRadioButton.setChecked(true);
				mCurrentFragmentTag = getActivity().getResources().getString(
						R.string.tab_filelist);
				break;
			case R.id.rb_tools:
				 onCheckedChanged(mToolsRadioButton, true);
				 mToolsRadioButton.setChecked(true);
					mCurrentFragmentTag = getActivity().getResources().getString(
							R.string.tab_tools);
				
				break;
			case R.id.rb_transferlist_right:
				 onCheckedChanged(mTransFileRadioButton, true);
				 mTransFileRadioButton.setChecked(true);
					mCurrentFragmentTag = getActivity().getResources().getString(
							R.string.tab_transferlist);
				//switchFragmen(getActivity().getString(R.string.tab_transferlist));
				break;
			default:
				break;
		}
		 
	 }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fsystem, container, false);
	}
	@Override
	public void onPause() {
		System.out.println("MainFileSystemFragment---onPause");
		super.onStart();
	}
	@Override
	public void onStop() {
		System.out.println("MainFileSystemFragment---onStop");
		super.onStart();
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// find views和setAction
		mFileListRadioButton = (RadioButton) view
				.findViewById(R.id.rb_filelist);
		mToolsRadioButton = (RadioButton) view.findViewById(R.id.rb_tools);
		mTransFileRadioButton = (RadioButton) view
				.findViewById(R.id.rb_transferlist_right);
		mFileListRadioButton.setOnCheckedChangeListener(this);
		mToolsRadioButton.setOnCheckedChangeListener(this);
		mTransFileRadioButton.setOnCheckedChangeListener(this);
	}

	/**
	 * 初始化FragmentTransaction
	 * @return FragmentTransaction实例
	 */
	protected FragmentTransaction ensureTransaction() {
		if (mFragmentTransaction == null) {
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}
		return mFragmentTransaction;
	}

	/**
	 * 根据tag获取对应的Fragment
	 * @param tag 标签
	 * @return 对应tag的Fragment实例
	 */
	
	private Fragment getFragment(String tag) {
		Fragment f = mFragmentManager.findFragmentByTag(tag);
		if (f == null) {
			// 在这里判断tag,不同则实例化对应的fragment，
			f = SampleFragment.newInstance(tag, MainActivity.PAGE_FILE_SYSTEM);
		
		}
		return f;
	}

	/**
	 * 将对应的fragment添加到容器中
	 * @param layout 父容器
	 * @param fragment实例
	 * @param tag 标签
	 */
	protected void attachFragment(int layout, Fragment f, String tag) {
		if (f != null) {
			if (f.isDetached()) {// 如果当前fragment只是隐藏，则显示出来
				ensureTransaction();
				mFragmentTransaction.attach(f);
			} else if (!f.isAdded()) {// 如果当前fragment没有添加到容器中，则先要添加到容器中
				ensureTransaction();
				mFragmentTransaction.add(layout, f, tag);
			}
		}
	}

	/**
	 * 将对应fragment隐藏
	 * @param 需要隐藏的fragment
	 */
	protected void detachFragment(Fragment f) {
		if (f != null && !f.isDetached()) {
			ensureTransaction();
			mFragmentTransaction.detach(f);
		}
	}

	/**
	 * 保存fragment状态，添加或隐藏fragment最后都需要调用此函数
	 */
	protected void commitTransactions() {
		if (mFragmentTransaction != null && !mFragmentTransaction.isEmpty()) {
			mFragmentTransaction.commit();
			mFragmentTransaction = null;
		}
	}

	/**
	 * 点击不同的Tab切换fragment
	 * @param tag
	 */
	private void switchFragmen(String tag) {
		if (TextUtils.equals(mCurrentFragmentTag, tag))
			return;
		if (mCurrentFragmentTag != null)
			detachFragment(getFragment(mCurrentFragmentTag));
		attachFragment(R.id.sliding_layer_frame_right, getFragment(tag), tag);
		mCurrentFragmentTag = tag;
		commitTransactions();
	}

	int saveswich=R.id.rb_filelist;

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (!isChecked)
			return;
		Log.i("way", "mCurrentFragmentTag = " + mCurrentFragmentTag);
		switch (buttonView.getId()) {
		case R.id.rb_filelist:
			saveswich=R.id.rb_filelist;
			switchFragmen(getActivity().getString(R.string.tab_filelist));
			break;
		case R.id.rb_tools:
			saveswich=R.id.rb_tools;
			switchFragmen(getActivity().getString(R.string.tab_tools));
			break;
		case R.id.rb_transferlist_right:
			saveswich=R.id.rb_transferlist_right;
			switchFragmen(getActivity().getString(R.string.tab_transferlist));
			break;
		default:
			break;
		}
	}
}
