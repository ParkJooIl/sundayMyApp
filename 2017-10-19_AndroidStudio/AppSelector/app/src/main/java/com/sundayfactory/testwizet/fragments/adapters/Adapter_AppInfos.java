package com.sundayfactory.testwizet.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sundayfactory.testwizet.R;
import com.sundayfactory.testwizet.core.AppInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jipark on 2017-12-15.
 */

public class Adapter_AppInfos extends RecyclerView.Adapter<Adapter_AppInfos.ViewHolders> {
    private List<AppInfo> Infos = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    private Activity mActivity;

    public Adapter_AppInfos(FragmentActivity activity) {
        this.mActivity = mActivity;
    }


    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appinfos , parent , false);
        return new ViewHolders(v);
    }

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        AppInfo item = Infos.get(position);
        holder.tv_Title.setText(item.Package);
        holder.tv_lastTimeStamp.setText(dateFormat.format(new Date(item.lastTimeStamp)));
        holder.tv_lastTimeUses.setText(dateFormat.format(new Date(item.lastTimeUses)));
        holder.tv_TotalTimeInForeground.setText(dateFormat.format(new Date(item.TotalTimeInForeground)));
        try {
            Drawable icon = mActivity.getPackageManager().getApplicationIcon(item.Package);
            holder.iv_Icon.setImageDrawable(icon);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void CustomUpdatelist(List<AppInfo> infos){
        Infos.clear();;
        Infos.addAll(infos);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return Infos.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder{
        public TextView tv_Title;
        public TextView tv_lastTimeStamp ;
        public TextView tv_lastTimeUses;
        public TextView tv_TotalTimeInForeground;
        public ImageView iv_Icon;
        public ViewHolders(View itemView) {
            super(itemView);
            tv_Title = (TextView)itemView.findViewById(R.id.textView);
            tv_lastTimeStamp = (TextView)itemView.findViewById(R.id.textView2);
            tv_lastTimeUses = (TextView)itemView.findViewById(R.id.textView3);
            tv_TotalTimeInForeground = (TextView)itemView.findViewById(R.id.textView4);
            iv_Icon = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }
}
