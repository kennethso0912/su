package com.ubtechinc.cruzr.clock.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;

import com.ubtechinc.cruzr.clock.R;
import com.ubtechinc.cruzr.clock.utils.DensityUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created on 2017/5/17.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Desc
 */
public class TimeView2 extends View{
    private String timeZoneId="Asia/Shanghai";
    private float mBigTextSize;
    private float mSmallTextSize;
    private Paint mTimePaint;
    private Paint mNoonPaint;

    private int mBigTextColor;
    private int mSmallTextColor;

    private String timeZoneName;

    public TimeView2(Context context) {
//        super(context);
        this(context, null);
    }

    public TimeView2(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);

    }

    public TimeView2(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setTimeZoneName(String timeZoneName){
        this.timeZoneName=timeZoneName;
//        invalidate();
    }

    private boolean isNeedSecond=true;
    public void setNeedSecond(boolean isNeedSecond){
        this.isNeedSecond=isNeedSecond;
    }


    private TimeZone tz=TimeZone.getTimeZone(this.timeZoneId);
    private boolean is24=DateFormat.is24HourFormat(this.getContext());

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        SimpleDateFormat sdf;
        String strFormatHour= is24?"HH":"hh";

//        if(this.isNeedSecond) {
//            sdf = new SimpleDateFormat(strFormatHour+":mm:ss");
//        }else{
            sdf=new SimpleDateFormat(strFormatHour+":mm:a");
//        }
        Date now = new Date();
        sdf.setTimeZone(tz);
        String time=sdf.format(now);
//        Paint paint=new Paint();
        mTimePaint.setColor(mBigTextColor);
        mTimePaint.setTextSize(mBigTextSize);
        mTimePaint.setTextAlign(Paint.Align.CENTER);

        String strHourMinute=time.substring(0,time.lastIndexOf(":"));
//        canvas.drawText(strHourMinute,getWidth()/2,getHeight()/2,mTimePaint);


        Rect mTextRect = new Rect();
        mTimePaint.getTextBounds(strHourMinute, 0, strHourMinute.length(), mTextRect);
        int textLargeWidth = mTextRect.width();

        /**
        mNoonPaint.setColor(mSmallTextColor);
        mNoonPaint.setTextSize(mSmallTextSize);*/
        mTimePaint.setColor(mBigTextColor);
        mTimePaint.setTextSize(mSmallTextSize);

//        mNoonPaint.setTextAlign(Paint.Align.CENTER);
//
        String noon=time.substring(time.lastIndexOf(":")+1);

        if(!DateFormat.is24HourFormat(this.getContext())) {
//            canvas.drawText(noon, getWidth() / 2 + textLargeWidth / 3 * 2, getHeight() / 2 - 10, mNoonPaint);
//            canvas.drawText(noon, getWidth() / 2 + textLargeWidth / 3 * 2, mBigTextSize/2+getPaddingTop(), mNoonPaint);

//            canvas.drawText(noon, getWidth() / 2 + textLargeWidth / 3 * 2-2, mBigTextSize/2+getPaddingTop(), mTimePaint);

//            canvas.drawText(noon, 50, mBigTextSize/2+getPaddingTop(), mTimePaint);
        }else{
            noon="";
        }


        int lineWidth=0;
        String lan= Locale.getDefault().getLanguage();
        if(lan.equals("en"))
        {
             lineWidth=120;
//             noon=noon!=""?noon.toLowerCase():noon;
             noon=noon!=""?noon.toLowerCase():"  ";
        }else if(lan.equals("zh")){
             lineWidth=140;
        }

        TextPaint tp = new TextPaint();
        tp.setColor(mBigTextColor);
        tp.setStyle(Paint.Style.FILL);
        tp.setTextSize(mBigTextSize);
        String message = "paintafdafsdfs";
        StaticLayout myStaticLayout = new StaticLayout(strHourMinute+noon+timeZoneName, tp, lineWidth, Layout.Alignment.ALIGN_CENTER, 1.5f, 2.0f, false);
        myStaticLayout.draw(canvas);
        canvas.restore();

//        canvas.drawText("abcde",getWidth()/2,mBigTextSize+getPaddingTop(),mTimePaint);


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
