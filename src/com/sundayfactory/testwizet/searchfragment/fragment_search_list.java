package com.sundayfactory.testwizet.searchfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.sundayfactory.testwizet.R;
import com.sundayfactory.testwizet.core.AppEntry;
import com.sundayfactory.testwizet.core.AppListLoader;
import com.sundayfactory.testwizet.customListiner.L_searchFinishedListiner;
import com.sundayfactory.testwizet.customListiner.indexHolder;
import com.sundayfactory.testwizet.customView.IndexLayout;
import com.sundayfactory.testwizet.db.MyappDb;
import com.sundayfactory.testwizet.searchfragment.STTControl.STTListiner;
import com.sundayfactory.testwizet.utils.AppsUtils;
import com.sundayfactory.testwizet.utils.HangulUtils;
import com.sundayfactory.testwizet.utils.ImageLazeLoader;
import com.sundayfactory.testwizet.utils.SharedUtils;
import com.sundayfactory.testwizet.utils.ShortCutUtils;

public class fragment_search_list extends MasterFragment implements LoaderManager.LoaderCallbacks<List<AppEntry>>, L_searchFinishedListiner {
	fragment_search_list cFragment_search;
	View mMainView;
	AppListAdapter mAdapter;
	ListView mAppListView;
	EditText mEditTextSearch;
	STTControl mControl;
	STTListiner mListiner = new STTListiner() {
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onResult(ArrayList<String> mResult) {
			if(mResult.size() >= 1){
				mEditTextSearch.setText(mResult.get(0));
				
			}
			
			
		}
		
		@Override
		public void onReadly() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onEnd() {
			// TODO Auto-generated method stub
			
		}
	};
	List<indexHolder> indexPositionList = new ArrayList<indexHolder>();
	/**
	 * 인덱스 레이아웃 
	 */
	LinearLayout IndexerMove;
	IndexLayout mLayoutIndexer;
	TextView IndexerMovetextView;

	private static ImageLazeLoader mImageLazeLoader;
	private Handler mHandler = new Handler();
	
	public fragment_search_list() {
	}

	public fragment_search_list getInstance() {
		cFragment_search = new fragment_search_list();
		return cFragment_search;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mMainView = inflater.inflate(R.layout.search_app, container, false);
		mAppListView = (ListView) mMainView.findViewById(R.id.appList);
		mEditTextSearch = (EditText) mMainView.findViewById(R.id.search);
		mEditTextSearch.setEnabled(false);
		mLayoutIndexer = (IndexLayout) mMainView.findViewById(R.id.Indexer);
		IndexerMovetextView = (TextView) mMainView.findViewById(R.id.IndexMoveText);
		IndexerMove = (LinearLayout) mMainView.findViewById(R.id.IndexLayout);
		mImageLazeLoader = new ImageLazeLoader(getActivity());
		mMainView.findViewById(R.id.imageButton_reset).setOnClickListener( new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditTextSearch.setText("");
			}
		});
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(mMainView.getVisibility() != View.GONE){
					mMainView.findViewById(R.id.LinearLayout_progress).setVisibility(View.GONE);	
				}
			}
		}, 2000);
		mLayoutIndexer.setOnTouchListener(mOnTouchListener);
		mEditTextSearch.addTextChangedListener(mTextWatcher);
		mEditTextSearch.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(mEditTextSearch.getText() != null){
						mAdapter.getFilter().filter(mEditTextSearch.getText().toString());
						return true;
							
					}
				}
				
				return false;
			}
		});
		mAdapter = new AppListAdapter(getActivity(), this);
		mAppListView.setAdapter(mAdapter);

		LinearLayout layout = (LinearLayout) mMainView.findViewById(R.id.Admob);
		initAdmob(layout);
		mControl = new STTControl(getActivity(), mListiner);
		mMainView.findViewById(R.id.imageButton_mic).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mControl.isStart){
				mControl.endSTT();		
				}
				mControl.startSTT();
				
				
			}
		});
		return mMainView;
	}

	TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Log.i("park", "onTextChanged]" + s.toString());

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			Log.i("park", "beforeTextChanged]" + s.toString());

		}

		@Override
		public void afterTextChanged(Editable s) {
			
			if (s != null) {
				Log.i("park", "afterTextChanged]" + s.toString());
				mAdapter.getFilter().filter(s.toString());

			}
		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(0, null, this);
	}

	private static class AppListAdapter extends ArrayAdapter<AppEntry> {
		private final LayoutInflater mInflater;
		private Activity mActivity;
		private List<AppEntry> OriData;
		private List<AppEntry> SearchData;
		private L_searchFinishedListiner l_searchFinishedListiner;
		private HashMap<String, String> Filtermap = new HashMap<String, String>();
		private AlertDialog mBuilder;
		public AppListAdapter(Activity activity, L_searchFinishedListiner finishedListiner) {
			super(activity, R.layout.list_item_icon_text);
			mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mActivity = activity;
			SearchData = new ArrayList<AppEntry>();
			l_searchFinishedListiner = finishedListiner;
		}

		public void setData(List<AppEntry> data) {
			clear();
			if (data != null) {
				addAll(data);
				OriData = data;
				SearchData.clear();
				SearchData.addAll(OriData);
				notifyDataSetChanged();
			}
		}

		/**
		 * Populate new items in the list.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_icon_text, parent, false);
				mHolder = new ViewHolder();
				mHolder.mImageView = (ImageView)convertView.findViewById(R.id.icon);
				mHolder.mTextView = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			final AppEntry item = getItem(position);
			
			mHolder.mImageView.setTag(item.getPackageName());
			mImageLazeLoader.onLoadImage(mHolder.mImageView, item);
			
			if (SharedUtils.FontSize.FONT_SIZE == SharedUtils.FontSize.Font_NOMAL) {
				mHolder.mTextView.setText(Html.fromHtml(" " + (position + 1) + " . " + item.getHtmlText()));
			} else {
				mHolder.mTextView.setText(Html.fromHtml(item.getHtmlText()));
			}
			mHolder.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, SharedUtils.FontSize.FONT_SIZE);
			convertView.findViewById(R.id.imageButton_appstart).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(mBuilder == null){
						AlertDialog.Builder Builder = new AlertDialog.Builder(mActivity);
						Builder.setMessage(R.string.SoutcoutMake);
						Builder.setPositiveButton(R.string.alert_Yes, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								ShortCutUtils.createShortcut(mActivity, item.getPackageName(), item.getLabel(), item.getIcontoBitmap());
								mBuilder.dismiss();
								mBuilder = null;
							}
						});
						Builder.setNegativeButton(R.string.alert_No, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mBuilder.dismiss();
								mBuilder = null;
							}
						});
						Builder.setCancelable(false);
						mBuilder = Builder.create();
						mBuilder.show();
						
					}
					
				}
			});
			convertView.findViewById(R.id.imageButton_trash).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AppsUtils.AppDelete(mActivity, item.getPackageName());

				}
			});
			convertView.findViewById(R.id.backGround).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						AppsUtils.AppStart(mActivity, item.getPackageName());
					} catch (Exception e) {
					}

				}
			});

			return convertView;
		}
		private class ViewHolder{
			ImageView mImageView;
			TextView mTextView;
			
		}
		@Override
		public int getCount() {
			return SearchData.size();
		}

		@Override
		public AppEntry getItem(int position) {
			return SearchData.get(position);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					Filtermap.clear();
					FilterResults filterResults = new FilterResults();
					SearchData.clear();
					if (constraint != null && constraint.length() > 0) {
						for (AppEntry App : OriData) {
							String iniName = HangulUtils.getHangulInitialSound(App.getLabel(), constraint.toString());
							if (iniName.toUpperCase().indexOf(constraint.toString().toUpperCase()) >= 0) {

								if (!SearchData.contains(App)) {
									App.setIndexWordPosition(iniName.toUpperCase().indexOf(constraint.toString().toUpperCase()), constraint.toString().length());
									SearchData.add(App);
								}

							}else{
								App.setIndexWordPosition(0, 0);
							}
						}

						filterResults.values = SearchData;
						filterResults.count = SearchData.size();
					} else {

						SearchData.addAll(OriData);
						filterResults.values = SearchData;
						filterResults.count = SearchData.size();
					}

					return filterResults;
				}

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
					if (l_searchFinishedListiner != null) {
						l_searchFinishedListiner.onFinished((List<AppEntry>) results.values);
					}

				}
			};
			return filter;
		}

	}

	public void searchIndexer(List<AppEntry> items) {

		String Index1 = "";
		String Index2 = "";
		mLayoutIndexer.removeAllViews();
		if (items == null) {
			return;
		}
		indexPositionList.clear();
		for (int a = 0; a < items.size(); a++) {
			AppEntry App = items.get(a);
			if(App != null){
				String iniName = HangulUtils.getHangulInitialSound(App.getLabel());
				Index1 = iniName.substring(0, 1).toUpperCase();
				if (!Index2.equalsIgnoreCase(Index1)) {
					Index2 = Index1;
					indexPositionList.add(new indexHolder(a, Index1));
				}	
			}
			
		}
		mLayoutIndexer.setAppList(indexPositionList);
	}

	OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int indexSize = indexPositionList.size();
			int layoutH = mLayoutIndexer.getHeight();
			
			int indexposition = (int) (event.getY() / (layoutH / indexSize));
			
			
			if (indexposition >= 0 && indexposition < indexPositionList.size()) {
				mAppListView.setSelection(indexPositionList.get(indexposition).indexPosition);
				IndexerMovetextView.setText(indexPositionList.get(indexposition).index);
				RelativeLayout.LayoutParams LP = (LayoutParams) IndexerMove.getLayoutParams();
				LP.topMargin = (int) event.getY();
				
				if(LP.topMargin > (layoutH - IndexerMovetextView.getHeight())){
					LP.topMargin = (layoutH - IndexerMovetextView.getHeight());
				};
				
				if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE ){
					IndexerMove.setVisibility(View.VISIBLE);
				}else{
					IndexerMove.setVisibility(View.GONE);
				}
				IndexerMove.setLayoutParams(LP);
			}
			return true;
		}
	};
	
	/**
	 * Helper class to look for interesting changes to the installed apps so
	 * that the loader can be updated.
	 */
	public static class PackageIntentReceiver extends BroadcastReceiver {
		final AppListLoader mLoader;
		public static final String ACTION_ONCONTENT_CHANGED = "ACTION_ONCONTENT_CHANGED";
		public PackageIntentReceiver(AppListLoader loader) {
			mLoader = loader;
			IntentFilter filter = new IntentFilter(ACTION_ONCONTENT_CHANGED);
			mLoader.getContext().registerReceiver(this, filter);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ACTION_ONCONTENT_CHANGED)){
				mLoader.onContentChanged();
				
			}
		}
	}
	/**
	 * Helper for determining if the configuration has changed in an interesting
	 * way so we need to rebuild the app list.
	 */
	public static class InterestingConfigChanges {
		final Configuration mLastConfiguration = new Configuration();
		int mLastDensity;

		public boolean applyNewConfig(Resources res) {
			int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
			boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
			if (densityChanged || (configChanges & (ActivityInfo.CONFIG_LOCALE | ActivityInfo.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
				mLastDensity = res.getDisplayMetrics().densityDpi;
				return true;
			}
			return false;
		}
	}

	@Override
	public Loader<List<AppEntry>> onCreateLoader(int arg0, Bundle arg1) {
		Log.i("park", "Loader");
		return new AppListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<AppEntry>> arg0, List<AppEntry> data) {
		Log.i("park", "onLoadFinished" + data.size());
		mAdapter.setData(data);
		searchIndexer(data);
		mAdapter.notifyDataSetChanged();
		// The list should now be shown.
		//mEditTextSearch.setText(null);
		mMainView.findViewById(R.id.LinearLayout_progress).setVisibility(View.GONE);
		mEditTextSearch.setText("");
		mEditTextSearch.setEnabled(true);
		mEditTextSearch.requestFocus();
		
	
	}

	@Override
	public void onLoaderReset(Loader<List<AppEntry>> arg0) {
		Log.i("park", "onLoaderReset");
		mAdapter.setData(null);
	}

	@Override
	public void onFinished(List<AppEntry> items) {
		searchIndexer(items);
	}

	

	@Override
	public void RefrashAdatper() {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
			HidekeyBoard();
		}

	}

	@Override
	public void HidekeyBoard() {
		if (mEditTextSearch != null) {
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEditTextSearch.getWindowToken(), 0);

		}

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageLazeLoader.onDestory();
	
	}
}
