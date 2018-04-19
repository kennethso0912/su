package com.ubtechinc.cruzr.calendar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ubtechinc.cruzr.calendar.CalendarApplication;
import com.ubtechinc.cruzr.calendar.constant.CalendarConstant;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.sdk.status.SystemStatus;
import com.ubtechinc.cruzr.sdk.status.SystemStatusApi;

import static com.ubtechinc.cruzr.calendar.constant.CalendarConstant.STATE_MACHINE_CODE;

/**
 * Created on 2018/1/10.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Desc
 */
public class WakeupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
          if(checkWakeupPermission()){
              stopTask();
          }
    }

    private boolean checkWakeupPermission(){
           boolean isInterrupt = SystemStatusApi.get().queryPermissionsWithStatus(SystemStatus.Permission.INTERRUPT, CalendarConstant.STATE_MACHINE_CODE);
       return isInterrupt;
    }

    private void stopTask(){
//        SpeechRobotApi.get().speechStopTTS();
        CalendarApplication.closeAllUI();
        //通知状态机游览结束
        SystemStatusApi.get().removeAppStatus(STATE_MACHINE_CODE);
    }
}
