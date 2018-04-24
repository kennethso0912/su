package com.ubtechinc.cruzr.clock.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;

import com.ubtechinc.cruzr.clock.R;
import com.ubtechinc.cruzr.clock.utils.DensityUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created on 2017/5/17.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Desc
 */
public class TimeView extends View{
    private String timeZoneId="Asia/Shanghai";
    private float mBigTextSize;
    private float mSmallTextSize;
    private Paint mTimePaint;
    private Paint mNoonPaint;

    private int mBigTextColor;
    private int mSmallTextColor;

    public TimeView(Context context) {
//        super(context);
        this(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);

    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimeView, defStyleAttr, 0);

        mBigTextSize = ta.getDimension(R.styleable.TimeView_bigTextSize, DensityUtils.sp2px(context, 14));
        mSmallTextSize = ta.getDimension(R.styleable.TimeView_smallTextSize, DensityUtils.sp2px(context, 14));

        mBigTextColor=ta.getColor(R.styleable.TimeView_bigTextColor, Color.parseColor("#65b9ff"));
        mSmallTextColor=ta.getColor(R.styleable.TimeView_smallTextColor, Color.parseColor("#65b9ff"));



        ta.recycle();

        mTimePaint=new Paint();
        mTimePaint.setTextSize(mBigTextSize);

        mNoonPaint=new Paint();
        mNoonPaint.setTextSize(mSmallTextSize);



    }
//    public TimeView(Context context,String timeZone){
//        super(context);
//        this.timeZoneId=timeZone;
//    }

    public void setTimeZone(String timeZoneId){
        this.timeZoneId=timeZoneId;
//        invalidate();
    }

    private boolean isNeedSecond=true;
    public void setNeedSecond(boolean isNeedSecond){
        this.isNeedSecond=isNeedSecond;
    }

    private boolean is24=DateFormat.is24HourFormat(this.getContext());

    private TimeZone tz=TimeZone.getTimeZone(this.timeZoneId);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        TimeZone tz=TimeZone.getTimeZone(this.timeZoneId);
        SimpleDateFormat sdf;
        String strFormatHour= is24?"HH":"hh";

//        if(this.isNeedSecond) {
//            sdf = new SimpleDateFormat(strFormatHour+":mm:ss");
//        }else{
        StringBuffer builder=new StringBuffer(strFormatHour);
        builder.append(":mm:a");
            sdf=new SimpleDateFormat(builder.toString());
//        }
        Date now = new Date();
        sdf.setTimeZone(tz);
        String time=sdf.format(now);
//        Paint paint=new Paint();
        mTimePaint.setColor(mBigTextColor);
        mTimePaint.setTextSize(mBigTextSize);
        mTimePaint.setTextAlign(Paint.Align.CENTER);

        String strHourMinute=time.substring(0,time.lastIndexOf(":"));
        canvas.drawText(strHourMinute,getWidth()/2,getHeight()/2,mTimePaint);


        Rect mTextRect = new Rect();
        mTimePaint.getTextBounds(strHourMinute, 0, strHourMinute.length(), mTextRect);
        int textLargeWidth = mTextRect.width();

        mNoonPaint.setColor(mSmallTextColor);
        mNoonPaint.setTextSize(mSmallTextSize);

//        mNoonPaint.setTextAlign(Paint.Align.CENTER);
//
        String noon=time.substring(time.lastIndexOf(":")+1);

        if(!DateFormat.is24HourFormat(this.getContext())) {
//            canvas.drawText(noon, getWidth() / 2 + textLargeWidth / 3 * 2, getHeight() / 2 - 10, mNoonPaint);
//            canvas.drawText(noon, getWidth() / 2 + textLargeWidth / 3 * 2, mBigTextSize/2+getPaddingTop(), mNoonPaint);
            canvas.drawText(noon, getWidth() / 2 + textLargeWidth / 3 * 2-2, mBigTextSize/2+getPaddingTop(), mNoonPaint);
        }



        /**
        if(!DateFormat.is24HourFormat(this.getContext())){
            int apm=Calendar.getInstance().get(Calendar.AM_PM);
            String noon= (apm==0)?"AM":"PM";

            Rect mTextRect = new Rect();
            mTimePaint.getTextBounds(time, 0, time.length(), mTextRect);
            int textLargeWidth = mTextRect.width();
            mNoonPaint.setColor(Color.BLUE);
            mNoonPaint.setTextSize(mSmallTextSize);
            canvas.drawText(noon,getWidth()/2+50,getHeight()/2-10,mNoonPaint);
        }*/

//        invalidate();
        postInvalidateDelayed(60000);
    }
}
