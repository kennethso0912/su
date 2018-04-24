/*
 * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 */

package com.ubtechinc.cruzr.clock.service;


import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.ubtechinc.cruzr.clock.ClockApplication;
import com.ubtechinc.cruzr.clock.DialogActivity;
import com.ubtechinc.cruzr.clock.R;
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

import static com.ubtechinc.cruzr.clock.constant.ClockConstant.STATE_MACHINE_CODE;

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
	private static final int APP_ID = 20;
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
				Log.i(TAG,"时钟指令分发进入前台->");
			}

			@Override
			public void onStop() {
				Log.i(TAG,"时钟指令分发退出后台->");
			}

			@Override
			public void onResult(String s) {
				Log.i(TAG,"时钟指令->"+s);
//				CruzrFaceApi.initCruzrFace(ServiceProxy.this);
//				CruzrFaceApi.setCruzrFace(null,"clean",false,false);
//
//				SystemStatusApi.get().setAppStatus(STATE_MACHINE_CODE,true);
//
//				Intent intent =new Intent(ServiceProxy.this, DialogActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				ServiceProxy.this.startActivity(intent);

                sendTimeToVoiceGuider(getTimeTts());
			}

			@Override
			public void onPause() {
				Log.i(TAG,"时钟暂停->onPause()->out");

			}

			@Override
			public void onResume() {
				Log.i(TAG,"时钟恢复->onResume()");
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
		ClockApplication.closeAllUI();
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
				if(key==EMERGENCY_STOP_KEY&&values!=EMERGENCY_STOP_VALUE){
					Log.e(TAG, "onResult: receive the emergency signal by warn interface->");
					stopTask();
				}
			}
		});
	}

	private String getTimeTts(){
		String format;
		if(DateFormat.is24HourFormat(this)){
			format="HH:mm:a";
		}else{
			format="hh:mm:a";
		}

		SimpleDateFormat sdf=new SimpleDateFormat(format);
		String time=sdf.format(new Date());

		String tts;
		if(!DateFormat.is24HourFormat(this)) {
			tts=getString(R.string.answer_12,time.split(":")[2],time.split(":")[0],time.split(":")[1]);
		}else{
			tts=getString(R.string.answer_24,time.split(":")[0],time.split(":")[1]);
		}

		return tts;
	}


	private static final String VOICE_GUIDE_MSG_SHOW ="com.ubtechinc.cruzr.voiceguide_msg_show";
	private static final String MSG_KEY = "msgKey";
	private static final String TTS_KEY = "ttsKey";

	private void sendTimeToVoiceGuider(String tts){
		Intent intent=new Intent(VOICE_GUIDE_MSG_SHOW);
		intent.putExtra(MSG_KEY,tts);
		intent.putExtra(TTS_KEY,true);
		sendBroadcast(intent);
	}
}
