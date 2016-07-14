package com.sundayfactory.testwizet.searchfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.sundayfactory.testwizet.db.MyappDb;



public abstract class MasterFragment extends Fragment {
	public AdView adView;
	public final String MY_AD_UNIT_ID = "ca-app-pub-9999663550966576/7859670247";
	private MyappDb mMyAppDB;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//adView = new AdView(getActivity(), AdSize.BANNER, MY_AD_UNIT_ID);
		adView = new AdView(getActivity());
		adView.setAdUnitId(MY_AD_UNIT_ID);
		adView.setAdSize(AdSize.BANNER);
		mMyAppDB =new MyappDb(getActivity());
		return super.onCreateView(inflater, container, savedInstanceState);
		
	}
	public void initAdmob(LinearLayout Admobview){
		Admobview.addView(adView);
		AdRequest adRequest = new AdRequest.Builder()
        .build();
		adView.loadAd(adRequest);
		Log.i("park", "AdViewLoad");
	}
	@Override
	public void onDetach() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDetach();
	}
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
	abstract public void RefrashAdatper();
	abstract public void HidekeyBoard();
	public MyappDb getMyAppDB(){
		return mMyAppDB;
	}
}
