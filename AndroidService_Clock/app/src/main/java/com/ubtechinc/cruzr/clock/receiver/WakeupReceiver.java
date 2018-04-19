package com.ubtechinc.cruzr.clock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ubtechinc.cruzr.clock.ClockApplication;
import com.ubtechinc.cruzr.clock.constant.ClockConstant;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.sdk.status.SystemStatus;
import com.ubtechinc.cruzr.sdk.status.SystemStatusApi;

import static com.ubtechinc.cruzr.clock.constant.ClockConstant.STATE_MACHINE_CODE;

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
              stopClockTask();
          }
    }

    private boolean checkWakeupPermission(){
           boolean isInterrupt = SystemStatusApi.get().queryPermissionsWithStatus(SystemStatus.Permission.INTERRUPT, STATE_MACHINE_CODE);
       return isInterrupt;
    }

    private void stopClockTask(){
//        SpeechRobotApi.get().speechStopTTS();
        ClockApplication.closeAllUI();
        //通知状态机游览结束
        SystemStatusApi.get().removeAppStatus(STATE_MACHINE_CODE);
    }
}
