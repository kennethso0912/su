/*
 * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 */

package com.ubtechinc.cruzr.calendar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.ubtechinc.cruzr.calendar.service.ServiceProxy;
import com.ubtechinc.cruzr.sdk.ros.RosRobotApi;
import com.ubtechinc.cruzr.sdk.status.SystemStatusApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.InitListener;

import java.util.ArrayList;


/**
 * Created on 2017/5/25.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Description
 */

public class CalendarApplication extends Application {
	private static final String TAG = "AppApplication";
	private static Context mContext = null;

	public static Context getContext() {
		return mContext;
	}

	private static final String BUGLY_APP_ID = "226b902911";

	private static ArrayList<Activity> actList=new ArrayList<>();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG,"onCreate()->");

		//初始化注册状态机服务
		SystemStatusApi.get().init(this);

		RosRobotApi.get().initializ(this,mRosInitListener);
        //启动游览相关服务
		startService(new Intent(this,ServiceProxy.class));

		/* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
		CrashReport.initCrashReport(getApplicationContext(), BUGLY_APP_ID, true);



	}

	public static void addAct(Activity act){
		actList.add(act);
	}

	public static void  closeAllUI(){
		for (Activity act:actList) {
			act.finish();
		}
		actList.clear();
	}

	private static InitListener mRosInitListener=new InitListener() {
		@Override
		public void onInit() {
			Log.e(TAG, "onInit: RosUtil 初始化成功->");
		}

		@Override
		public void onReConnect() {
			super.onReConnect();
		}
	};
}
