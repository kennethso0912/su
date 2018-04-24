package com.ubtechinc.cruzr.calendar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;


import com.ubtechinc.cruzr.calendar.CalendarApplication;
import com.ubtechinc.cruzr.calendar.R;
import com.ubtechinc.cruzr.sdk.face.CruzrFaceApi;
import com.ubtechinc.cruzr.sdk.face.CruzrFaceCallBackImpl;
import com.ubtechinc.cruzr.sdk.face.FaceInfo;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.sdk.status.SystemStatusApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.SpeechTtsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.ubtechinc.cruzr.calendar.constant.CalendarConstant.STATE_MACHINE_CODE;

/**
 * Created on 2017/5/24.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Desc
 */
public class DialogActivity extends Activity {
    private TextView mTvWeek,mTvDate,mTvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dialog);

        CalendarApplication.addAct(this);

        findView();

//        setContentView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView();
    }

    private void findView(){
        mTvWeek=(TextView) findViewById(R.id.tv_week);
        mTvDate=(TextView) findViewById(R.id.tv_date);
        mTvTime=(TextView) findViewById(R.id.tv_time);
    }

    private void setContentView(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d-EEEE-HH:mm");
        String currentDate = sdf.format(date);  //当期日期
        int year_c = Integer.parseInt(currentDate.split("-")[0]);
        int month_c = Integer.parseInt(currentDate.split("-")[1]);
        int day_c = Integer.parseInt(currentDate.split("-")[2]);
        String week_c=currentDate.split("-")[3];
        String time=currentDate.split("-")[4];

        mTvWeek.setText(week_c);
        mTvDate.setText(getString(R.string.show,year_c+"",month_c+"",day_c+""));
        mTvTime.setText(time);


        String lan= Locale.getDefault().getLanguage().toString();
        String month_c_str=lan.equals("zh")?month_c+"":getString(getResources().getIdentifier("month_"+month_c,"string",getPackageName()));
        String tts=getString(R.string.answer,year_c+"",month_c_str,day_c+"",week_c);

        SpeechRobotApi.get().speechStartTTS(tts, new SpeechTtsListener() {
            @Override
            public void onAbort() {
                  handleDisappear();
            }

            @Override
            public void onEnd() {
                  handleDisappear();

            }
        });
    }


    private void handleDisappear(){
//        CruzrFaceApi.getCurrentFaceId(new CruzrFaceCallBackImpl() {
//            @Override
//            public void onCruzrFaceListCallBack(ArrayList<FaceInfo> arrayList) {
//
//            }
//
//            @Override
//            public void onCruzrFaceSetCallBack(int i) {
//
//            }
//
//            @Override
//            public void onCurrentFaceIdCallBack(String s) {
//                if(!TextUtils.isEmpty(s)) {
//                    CruzrFaceApi.setCruzrFace(null, "face_public", false, false);
//                }
//                DialogActivity.this.finish();
//            }
//        });

        //通知状态机游览结束
        SystemStatusApi.get().removeAppStatus(STATE_MACHINE_CODE);

    }
}
