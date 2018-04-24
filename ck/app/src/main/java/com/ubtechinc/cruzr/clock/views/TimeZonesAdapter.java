package com.ubtechinc.cruzr.clock.views;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ubtechinc.cruzr.clock.R;
import com.ubtechinc.cruzr.clock.models.TimeZoneInfo;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created on 2017/5/23.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Desc
 */
public class TimeZonesAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<TimeZoneInfo.TimeZone> timezones =new ArrayList<TimeZoneInfo.TimeZone>();

    public TimeZonesAdapter(Context context, ArrayList<TimeZoneInfo.TimeZone> timezones){
        this.context=context;
        this.timezones=timezones;
    }
    @Override
    public int getCount() {
        return timezones.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);

            AbsListView.LayoutParams params= (AbsListView.LayoutParams) convertView.getLayoutParams();
            if(params==null){
                params=new AbsListView.LayoutParams(400,400);
                convertView.setLayoutParams(params);
            }else {
                params.height=400;
                params.width=400;
            }


        }
        String lan= Locale.getDefault().getLanguage();

//        TextView tvTimeZone=(TextView)convertView.findViewById(R.id.tv_time);
//        tvTimeZone.setText(timezones.get(position).getName());


//        if(lan.equals("en"))
//        {
////            AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
//
//        }

        TimeView2 timeView= (TimeView2) convertView.findViewById(R.id.timeView);
        timeView.setNeedSecond(false);
        timeView.setTimeZone(timezones.get(position).getId());
        timeView.setTimeZoneName(timezones.get(position).getName());

        ClockView clockView=(ClockView)convertView.findViewById(R.id.clockView);
        clockView.setTimeZoneId(timezones.get(position).getId());
        clockView.setNeedSecondHand(false);


//        AbsListView.LayoutParams params=new AbsListView.LayoutParams(400,400);
        convertView.setPadding(0,0,0,0);


//        convertView.setLayoutParams(params);
        return convertView;
    }
}
