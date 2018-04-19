/*
 * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 */

package com.ubtechinc.cruzr.calendar.service;


import android.content.Intent;
import android.util.Log;

import com.ubtechinc.cruzr.calendar.CalendarApplication;
import com.ubtechinc.cruzr.calendar.R;
import com.ubtechinc.cruzr.calendar.activity.DialogActivity;
import com.ubtechinc.cruzr.sdk.face.CruzrFaceApi;
import com.ubtechinc.cruzr.sdk.ros.RosRobotApi;
import com.ubtechinc.cruzr.sdk.speech.ISpeechContext;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.sdk.status.SystemStatusApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.InitListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.RemoteStatusListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.RemoteWarnListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ubtechinc.cruzr.calendar.constant.CalendarConstant.STATE_MACHINE_CODE;

/**
 * Created on 2017/2/24.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Des
 */

public class ServiceProxy extends ServiceBindable {
	private static final String TAG = "ServiceProxy";
	private SpeechRobotApi mSpeechRobotApi;
	private static final int APP_ID = 19;
	private static final int EMERGENCY_STOP_KEY = 0x7fffffff;
	private static final int EMERGENCY_STOP_VALUE =0;

	@Override
	protected void onStartOnce() {
		regDispatcher();
//		observeSystemStatus();
//		observeEmergencyStop();
	}



	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	public void regDispatcher() {
		mSpeechRobotApi= SpeechRobotApi.get().initializ(this, APP_ID,mInitListener);
		mSpeechRobotApi.registerSpeech(new ISpeechContext() {
			@Override
			public void onStart() {
				Log.i(TAG,"日历指令分发进入前台->");
			}

			@Override
			public void onStop() {
				Log.i(TAG,"日历指令分发退出后台->");
			}

			@Override
			public void onResult(String s) {
				Log.i(TAG,"日历指令->"+s);
//				CruzrFaceApi.initCruzrFace(ServiceProxy.this);
//				CruzrFaceApi.setCruzrFace(null,"clean",false,false);
//
//				SystemStatusApi.get().setAppStatus(STATE_MACHINE_CODE,true);
//
//				Intent intent=new Intent(ServiceProxy.this, DialogActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				ServiceProxy.this.startActivity(intent);

				sendTimeToVoiceGuider(getCalendarTts());
			}

			@Override
			public void onPause() {
				Log.i(TAG,"日历暂停->onPause()->out");

			}

			@Override
			public void onResume() {
				Log.i(TAG,"日历恢复->onResume()");
			}
		});
	}

	private InitListener mInitListener=new InitListener() {
		@Override
		public void onInit() {
			Log.e(TAG, "onInit: 语音初始化成功->");
		}
	};

	private void observeSystemStatus(){
		SystemStatusApi.get().registerStatusCallback(new RemoteStatusListener() {
			@Override
			public void onResult(List<Integer> list, boolean b, int i) {

			}

			@Override
			public void onStatusPause(int i, int i1,String sponsor) {//
				stopTask();
			}

			@Override
			public void onStatusFree(int i) {

			}
		});
	}

	private void stopTask(){
		SpeechRobotApi.get().speechStopTTS();
		CalendarApplication.closeAllUI();
		//通知状态机游览结束
		SystemStatusApi.get().removeAppStatus(STATE_MACHINE_CODE);
	}

	/**
	 * 监听急停按钮被按下
	 */
	private void observeEmergencyStop(){
		RosRobotApi.get().registerWarnCallback(new RemoteWarnListener() {
			@Override
			public void onResult(String s, int key, int values, int data) {
				//value 0:normal !0:emergency
				if(key==EMERGENCY_STOP_KEY&&values!=EMERGENCY_STOP_VALUE){
					Log.e(TAG, "onResult: receive the emergency signal by warn interface->");
					stopTask();
				}
			}
		});
	}

	private String getCalendarTts(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d-EEEE-HH:mm");
		String currentDate = sdf.format(date);  //当期日期

		int year_c = Integer.parseInt(currentDate.split("-")[0]);
		int month_c = Integer.parseInt(currentDate.split("-")[1]);
		int day_c = Integer.parseInt(currentDate.split("-")[2]);
		String week_c=currentDate.split("-")[3];

		String lan= Locale.getDefault().getLanguage().toString();
		String month_c_str=lan.equals("zh")?month_c+"":getString(getResources().getIdentifier("month_"+month_c,"string",getPackageName()));
		String tts=getString(R.string.answer,year_c+"",month_c_str,day_c+"",week_c);

		return tts;
	}

	private static final String VOICE_GUIDE_MSG_SHOW ="com.ubtechinc.cruzr.voiceguide_msg_show";
	private static final String MSG_KEY = "msgKey";
	private static final String TTS_KEY = "ttsKey";
	private void sendTimeToVoiceGuider(String tts){
		Log.e(TAG, "sendTimeToVoiceGuider: tts="+tts);
		Intent intent=new Intent(VOICE_GUIDE_MSG_SHOW);
		intent.putExtra(MSG_KEY,tts);
		intent.putExtra(TTS_KEY,true);
		sendBroadcast(intent);
	}
}
