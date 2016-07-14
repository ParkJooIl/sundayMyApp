package com.sundayfactory.testwizet;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.sundayfactory.testwizet.searchfragment.MasterFragment;
import com.sundayfactory.testwizet.searchfragment.fragment_search_list;
import com.sundayfactory.testwizet.searchfragment.fragment_search_grid;
import com.sundayfactory.testwizet.utils.AppsUtils;
import com.sundayfactory.testwizet.utils.SharedUtils;

public class MainActivity extends Activity {
	// FrameLayout mFrameLayout;
	FragmentTransaction fFragmentTransaction;
	fragment_search_list ffragment_search;
	//fragment_search_grid ffragment_search_grid;
	MasterFragment masterFragment;
	RelativeLayout optionLayout;
	Switch mSwitchchanger;
	RadioGroup mradioGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedUtils.ListMode.LIST_MODE = SharedUtils.getInt(this, SharedUtils.ListMode.KEY_LISTMODE, SharedUtils.ListMode.LISTMODE_LIST);
		
		initOption();
		
		

		getActionBar().hide();
		ffragment_search = new fragment_search_list();
		//ffragment_search_grid = new fragment_search_grid();

		
		ChangeFragments(SharedUtils.ListMode.LIST_MODE);
		
		
		
		if (!SharedUtils.getBoolean(this, "HomeIcon")) {
			SharedUtils.setBoolean(this, "HomeIcon", true);
			AppsUtils.addShortcut(this, this.getClass().getName());
		}
		
		
	}
	/**
	 * 옵션메뉴 설정
	 */
	private void initOption(){
		optionLayout = (RelativeLayout) findViewById(R.id.optionlayout);
		
		mSwitchchanger = (Switch) findViewById(R.id.switchchanger);
		mSwitchchanger.setChecked((SharedUtils.ListMode.LIST_MODE == SharedUtils.ListMode.LISTMODE_LIST)?true:false );
		mSwitchchanger.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				ChangeFragments((arg1)?SharedUtils.ListMode.LISTMODE_LIST : SharedUtils.ListMode.LISTMODE_GRID);
				SharedUtils.setInt(MainActivity.this, SharedUtils.ListMode.KEY_LISTMODE, (arg1)?SharedUtils.ListMode.LISTMODE_LIST : SharedUtils.ListMode.LISTMODE_GRID);
			}
		});
		findViewById(R.id.optionlayout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(findViewById(R.id.optionlayout).getVisibility() == View.VISIBLE){
					findViewById(R.id.optionlayout).setVisibility(View.GONE);
				}
			}
		});
		mradioGroup = (RadioGroup)findViewById(R.id.radioGroup_size);
		SharedUtils.FontSize.FONT_SIZE = SharedUtils.getInt(this, SharedUtils.FontSize.KEY, SharedUtils.FontSize.Font_NOMAL);
		switch (SharedUtils.FontSize.FONT_SIZE) {
		case SharedUtils.FontSize.Font_NOMAL:
			mradioGroup.check(R.id.radio0);
			break;
		case SharedUtils.FontSize.Font_Large:
			mradioGroup.check(R.id.radio1);
			break;
		case SharedUtils.FontSize.Font_BigLarge:
			mradioGroup.check(R.id.radio2);
			break;

		}
		mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					SharedUtils.setInt(MainActivity.this, SharedUtils.FontSize.KEY, SharedUtils.FontSize.Font_NOMAL);
					SharedUtils.FontSize.FONT_SIZE = SharedUtils.FontSize.Font_NOMAL;
					break;
				case R.id.radio1:
					SharedUtils.setInt(MainActivity.this, SharedUtils.FontSize.KEY, SharedUtils.FontSize.Font_Large);
					SharedUtils.FontSize.FONT_SIZE = SharedUtils.FontSize.Font_Large;

					break;
				case R.id.radio2:
					SharedUtils.setInt(MainActivity.this, SharedUtils.FontSize.KEY, SharedUtils.FontSize.Font_BigLarge);
					SharedUtils.FontSize.FONT_SIZE = SharedUtils.FontSize.Font_BigLarge;

					break;

				}
				masterFragment.RefrashAdatper();
			}
		});
		
	}

	private void showFragment(Fragment mFragment, String Tag) {
		ffragment_search = new fragment_search_list();
		fFragmentTransaction = getFragmentManager().beginTransaction();
		fFragmentTransaction.replace(R.id.changefragments, mFragment, Tag);
		fFragmentTransaction.commit();
		masterFragment = (MasterFragment) mFragment;

	}

	private void ChangeFragments(int isMode) {
		switch (isMode) {
		case SharedUtils.ListMode.LISTMODE_LIST:
			showFragment(ffragment_search, "list");

			break;
		case SharedUtils.ListMode.LISTMODE_GRID:
			//showFragment(ffragment_search_grid, "grid");
			break;
		}
	}

	private void initActionFragmentsTab() {
		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		bar.addTab(bar.newTab().setText("List").setTabListener(new TabListener<fragment_search_list>(this, "simple", fragment_search_list.class)));
		bar.hide();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.getItem(0).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}

	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;

		public TabListener(Activity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(mActivity, "Reselected! " + mTag, Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * public void processingAppStatus() { ActivityManager am =
	 * (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
	 * List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(10);
	 * for (int a = 0; a < taskInfo.size(); a++) { Log.i("park", " " + a + " = "
	 * + taskInfo.get(a).topActivity.getPackageName());
	 * 
	 * } }
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (optionLayout.getVisibility() == View.VISIBLE) {
				optionLayout.setVisibility(View.GONE);
			} else {
				optionLayout.setVisibility(View.VISIBLE);
				masterFragment.HidekeyBoard();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	public void onBackPressed() {
		if(optionLayout.getVisibility() == View.VISIBLE){
			optionLayout.setVisibility(View.GONE);
		}else{
			super.onBackPressed();
		}
	};
}
