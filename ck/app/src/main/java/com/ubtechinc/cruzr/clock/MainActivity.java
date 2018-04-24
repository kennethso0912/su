package com.ubtechinc.cruzr.clock;

import android.app.Activity;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ubtechinc.cruzr.clock.models.TimeZoneInfo;
import com.ubtechinc.cruzr.clock.utils.ParserUtil;
import com.ubtechinc.cruzr.clock.views.ClockView;
import com.ubtechinc.cruzr.clock.views.TimeView;
import com.ubtechinc.cruzr.clock.views.TimeZonesAdapter;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends Activity {
    private static final String TAG="MainActivity";
    private GridView mGvTimeZone;
    private TextView mCurTime,mCurZone,mCurDay;
    private ImageView mIvBack;

    private TimeView mTvCurTime;

    private ClockView mCurClockView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_canvas);

        ClockApplication.addAct(this);

        mGvTimeZone=(GridView) findViewById(R.id.gv_timezones);

        String lan=Locale.getDefault().getLanguage();
        Log.e(TAG, "onCreate: lan="+lan);
        String path="";
        if(lan.equals("zh")){
            String place=Locale.getDefault().getCountry();
            if(place.equalsIgnoreCase("CN")) {
                path = ParserUtil.FILE_PATH;
            }
            else if(place.equalsIgnoreCase("TW")){
                path = ParserUtil.FILE_PATH_TW;
            }
        }else if(lan.equals("en")){
            path=ParserUtil.FILE_PATH_EN;
        }
        TimeZoneInfo info=new Gson().fromJson(ParserUtil.parseFile(this,path), TimeZoneInfo.class);
        TimeZonesAdapter adapter=new TimeZonesAdapter(this,info.timezones);

        mGvTimeZone.setAdapter(adapter);

//        mCurTime=(TextView)findViewById(R.id.tv_curTime);
        mCurZone=(TextView)findViewById(R.id.tv_curZone);
        mCurDay=(TextView)findViewById(R.id.tv_curDay);

        Calendar calendar = Calendar.getInstance();
        String curZoneName= calendar.getTimeZone().getDisplayName();
        mCurZone.setText(curZoneName);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String currentDate = sdf.format(date);  //当期日期
        int year_c = Integer.parseInt(currentDate.split("-")[0]);
        int month_c = Integer.parseInt(currentDate.split("-")[1]);
        int day_c = Integer.parseInt(currentDate.split("-")[2]);


        StringBuffer textDate = new StringBuffer();

        if(Locale.getDefault().getLanguage().equals("en")){
            textDate.append(day_c+" ").append(getString(getResources().getIdentifier("month_"+month_c,"string",getPackageName()))).append(" ").append(year_c).append("\t");
        }else if(Locale.getDefault().getLanguage().equals("zh")){
            textDate.append(year_c).append("年").append(month_c).append("月").append(day_c).append("日");
        }

        mCurDay.setText(textDate);

        mIvBack=(ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                clean();
//                System.exit(0);
            }
        });

        mTvCurTime=(TimeView) findViewById(R.id.tv_time_cur);
        mTvCurTime.setTimeZone(calendar.getTimeZone().getID());

        mCurClockView=(ClockView) findViewById(R.id.cur_cv);
        mCurClockView.setTimeZoneId(calendar.getTimeZone().getID());

    }

    private void clean(){
        mGvTimeZone=null;
        mTvCurTime=null;
        mCurZone=null;
        mCurDay=null;
        mIvBack=null;
    }


}
