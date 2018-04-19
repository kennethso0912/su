package com.ubtechinc.cruzr.clock;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ubtechinc.cruzr.sdk.face.CruzrFaceApi;
import com.ubtechinc.cruzr.sdk.face.CruzrFaceCallBackImpl;
import com.ubtechinc.cruzr.sdk.face.FaceInfo;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.sdk.status.SystemStatusApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.SpeechTtsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ubtechinc.cruzr.clock.constant.ClockConstant.STATE_MACHINE_CODE;

/**
 * Created on 2017/5/24.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Desc
 */
public class DialogActivity extends Activity {
    private TextView mTvWeek,mTvDate,mTvTime,mTvNoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dialog);

        ClockApplication.addAct(this);
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

        mTvNoon=(TextView) findViewById(R.id.tv_noon);
    }

    private void setContentView(){
        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d-EEEE-HH:mm:a");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d-EEEE");
        String currentDate = sdf.format(date);  //当期日期
        int year_c = Integer.parseInt(currentDate.split("-")[0]);
        int month_c = Integer.parseInt(currentDate.split("-")[1]);
        int day_c = Integer.parseInt(currentDate.split("-")[2]);
        String week_c=currentDate.split("-")[3];

//        String time=currentDate.split("-")[4];
        mTvWeek.setText(week_c);
//        mTvDate.setText(year_c+"年"+month_c+"月"+day_c+"日");
        mTvDate.setText(getString(R.string.show,year_c+"",month_c+"",day_c+""));

        String format;
        if(DateFormat.is24HourFormat(this)){
             format="HH:mm:a";
        }else{
            format="hh:mm:a";
        }
        sdf=new SimpleDateFormat(format);
        String time=sdf.format(new Date());



        String tts;
        if(!DateFormat.is24HourFormat(this)) {
//            tts = "现在是" + time.split(":")[2] + time.split(":")[0] + "点" + time.split(":")[1] + "分";

            tts=getString(R.string.answer_12,time.split(":")[2],time.split(":")[0],time.split(":")[1]);

            mTvTime.setText(time.split(":")[0]+":"+time.split(":")[1]);
            mTvTime.setTextSize(80);
//            String timeAndNoon="<b color='white' size='150px'>"+time.split(":")[0]+":"+time.split(":")[1]+"</b><font color='#65b9ff' size='12px'>"+time.split(":")[2]+"</font>";
//            mTvTime.setText(Html.fromHtml(timeAndNoon));
            mTvTime.setPadding(0,20,0,0);
            mTvNoon.setVisibility(View.VISIBLE);
            mTvNoon.setText(time.split(":")[2]);
        }else{
//            tts = "现在是" +  time.split(":")[0] + "点" + time.split(":")[1] + "分";
            tts=getString(R.string.answer_24,time.split(":")[0],time.split(":")[1]);

            mTvTime.setText(time.substring(0,time.lastIndexOf(":")));

        }


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
