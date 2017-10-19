package com.sundayfactory.testwizet.searchfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.sundayfactory.testwizet.R;
import com.sundayfactory.testwizet.core.AppEntry;
import com.sundayfactory.testwizet.core.AppListLoader;
import com.sundayfactory.testwizet.customListiner.L_searchFinishedListiner;
import com.sundayfactory.testwizet.customListiner.indexHolder;
import com.sundayfactory.testwizet.customView.IndexLayout;
import com.sundayfactory.testwizet.utils.AppsUtils;
import com.sundayfactory.testwizet.utils.HangulUtils;
import com.sundayfactory.testwizet.utils.SharedUtils;
import com.sundayfactory.testwizet.utils.ShortCutUtils;

public class fragment_search_grid extends MasterFragment implements LoaderManager.LoaderCallbacks<List<AppEntry>>, L_searchFinishedListiner {
	fragment_search_grid cFragment_search_grid;
	View mMainView;
	AppListAdapter mAdapter;
	GridView mAppListView;
	EditText mEditTextSearch;
	List<indexHolder> indexPositionList = new ArrayList<indexHolder>();
	RelativeLayout.LayoutParams LP;
	/**
	 * 인덱스 레이아웃
	 */
	LinearLayout IndexerMove;
	IndexLayout mLayoutIndexer;
	TextView IndexerMovetextView;

	public fragment_search_grid() {
	}

	public fragment_search_grid getInstance() {
		cFragment_search_grid = new fragment_search_grid();
		return cFragment_search_grid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mMainView = inflater.inflate(R.layout.search_app_grid, container, false);
		mAppListView = (GridView) mMainView.findViewById(R.id.appgridView);
		mEditTextSearch = (EditText) mMainView.findViewById(R.id.search);

		mLayoutIndexer = (IndexLayout) mMainView.findViewById(R.id.Indexer);
		IndexerMovetextView = (TextView) mMainView.findViewById(R.id.IndexMoveText);
		IndexerMove = (LinearLayout) mMainView.findViewById(R.id.IndexLayout);
		LP = (LayoutParams) IndexerMove.getLayoutParams();
		mLayoutIndexer.setOnTouchListener(mOnTouchListener);
		mEditTextSearch.addTextChangedListener(mTextWatcher);
		mAdapter = new AppListAdapter(getActivity(), this);
		mAppListView.setAdapter(mAdapter);

		LinearLayout layout = (LinearLayout) mMainView.findViewById(R.id.Admob);
		initAdmob(layout);
		return mMainView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(0, null, this);
	}

	public static class AppListAdapter extends ArrayAdapter<AppEntry> {
		private final LayoutInflater mInflater;
		private Activity mActivity;
		private List<AppEntry> OriData;
		private List<AppEntry> SearchData;
		private L_searchFinishedListiner l_searchFinishedListiner;
		private HashMap<String, String> Filtermap = new HashMap<String, String>();

		public AppListAdapter(Activity activity, L_searchFinishedListiner finishedListiner) {
			super(activity, R.layout.grid_item_icon_text);
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

			View view;

			if (convertView == null) {
				view = mInflater.inflate(R.layout.grid_item_icon_text, parent, false);
			} else {
				view = convertView;
			}

			final AppEntry item = getItem(position);

			((ImageView) view.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
			TextView appTitle = (TextView) view.findViewById(R.id.text);
			appTitle.setText(Html.fromHtml(item.getHtmlText()));
			appTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, SharedUtils.FontSize.Font_NOMAL);
			ImageButton creHomeButton = (ImageButton) view.findViewById(R.id.imageButton_appstart);
			creHomeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShortCutUtils.createShortcut(mActivity, item.getPackageName(), item.getLabel(), item.getIcontoBitmap());
				}
			});
			ImageButton TrashButton = (ImageButton) view.findViewById(R.id.imageButton_trash);
			TrashButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AppsUtils.AppDelete(mActivity, item.getPackageName());

				}
			});
			view.findViewById(R.id.backGround).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					AppsUtils.AppStart(mActivity, item.getPackageName());

				}
			});

			return view;
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
									Log.i("park", constraint.toString() + "][" + iniName + "][" + iniName.toUpperCase().indexOf(constraint.toString().toUpperCase()) + "][" + constraint.toString().length());
									App.setIndexWordPosition(iniName.toUpperCase().indexOf(constraint.toString().toUpperCase()), constraint.toString().length());
									SearchData.add(App);
								}

							} else {
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

	
	OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int indexSize = indexPositionList.size();
			int layoutH = mLayoutIndexer.getHeight();
			int indexposition = (int) (event.getY() / (layoutH / indexSize));
			if (indexposition >= 0 && indexposition < indexPositionList.size()) {
				mAppListView.setSelection(indexPositionList.get(indexposition).indexPosition);
				IndexerMovetextView.setText(indexPositionList.get(indexposition).index);
				
				LP.topMargin = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
					if (IndexerMove.getVisibility() != View.VISIBLE) {
						IndexerMove.setVisibility(View.VISIBLE);
					}
				} else {
					IndexerMove.setVisibility(View.GONE);
				}
				IndexerMove.setLayoutParams(LP);
			}
			return true;
		}
	};
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
			// TODO Auto-generated method stub
			if (s != null) {
				Log.i("park", "afterTextChanged]" + s.toString());
				mAdapter.getFilter().filter(s.toString());

			}
		}
	};

	@Override
	public Loader<List<AppEntry>> onCreateLoader(int arg0, Bundle arg1) {
		Log.i("park", "Loader");
		return new AppListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<AppEntry>> arg0, List<AppEntry> arg1) {
		Log.i("park", "onLoadFinished + " + arg1.size());
		mAdapter.setData(arg1);
		searchIndexer(arg1);
		mAdapter.notifyDataSetChanged();
		mEditTextSearch.setText(null);
	}

	@Override
	public void onLoaderReset(Loader<List<AppEntry>> arg0) {
		mAdapter.setData(null);
	}

	@Override
	public void onFinished(List<AppEntry> items) {
		searchIndexer(items);
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
			String iniName = HangulUtils.getHangulInitialSound(App.getLabel());
			Index1 = iniName.substring(0, 1).toUpperCase();
			if (!Index2.equalsIgnoreCase(Index1)) {
				Index2 = Index1;
				indexPositionList.add(new indexHolder(a, Index1));
			}
		}
		mLayoutIndexer.setAppList(indexPositionList);
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
}
