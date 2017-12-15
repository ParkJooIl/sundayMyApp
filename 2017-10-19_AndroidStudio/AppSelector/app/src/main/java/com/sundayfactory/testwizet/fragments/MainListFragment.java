package com.sundayfactory.testwizet.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.sundayfactory.testwizet.R;
import com.sundayfactory.testwizet.core.AppInfo;
import com.sundayfactory.testwizet.fragments.adapters.Adapter_AppInfos;
import com.sundayfactory.testwizet.manager.AppUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainListFragment extends baseFragment {
    private Adapter_AppInfos adapter_appInfos;
    private RecyclerView mListView ;
    private  RecyclerView.LayoutManager _LayoutManager;
    private Button _bUpdate;
    public MainListFragment() {
        // Required empty public constructor
    }

    public static MainListFragment newInstance() {
        MainListFragment fragment = new MainListFragment();
        return fragment;
    }
    public static MainListFragment newInstance(String param1, String param2) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _bUpdate = (Button) view.findViewById(R.id.button);
        _bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AppInfo> infos = AppUtils.NowForeGroundAppCheck(getActivity());
                if(infos.size() != 0){
                    adapter_appInfos.CustomUpdatelist(infos);
                    adapter_appInfos.notifyDataSetChanged();
                    mListView.scrollToPosition(0);
                    _bUpdate.setText("COUNT : "+infos.size());
                }

            }
        });
        adapter_appInfos = new Adapter_AppInfos(getActivity());
        mListView = (RecyclerView)view.findViewById(R.id.listview_applist);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(llm);

        mListView.scrollToPosition(0);
        mListView.setAdapter(adapter_appInfos);
    }



}
