package com.ubtechinc.cruzr.calendar;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ubtechinc.cruzr.calendar.activity.CalendarActivity;
import com.ubtechinc.cruzr.calendar.calendarModel.LunarCalendar;
import com.ubtechinc.cruzr.calendar.datepicker.bizs.calendars.DPCManager;
import com.ubtechinc.cruzr.calendar.datepicker.bizs.decors.DPDecor;
import com.ubtechinc.cruzr.calendar.datepicker.cons.DPMode;
import com.ubtechinc.cruzr.calendar.datepicker.views.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



/**
 * Demo应用的主Activity
 * The main activity of demo
 *
 * @author AigeStudio 2015-03-26
 */
public class MainActivity extends Activity {
    private TextView tvCurYearMonth;
    private ImageView tvCurDay;
    private TextView tvCurWeek;
    private TextView tvCurLunar;

    private ImageView mIvBack;
    private ImageView mIvToday;

    private DatePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_new);

//        // 默认多选模式
//        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 7);
//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            }
//        });

        // 自定义背景绘制示例 Example of custom date's background
//        List<String> tmp = new ArrayList<>();
//        tmp.add("2015-7-1");
//        tmp.add("2015-7-8");
//        tmp.add("2015-7-16");
//        DPCManager.getInstance().setDecorBG(tmp);
//
//        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 7);
//        picker.setDPDecor(new DPDecor() {
//            @Override
//            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
//                paint.setColor(Color.RED);
//                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
//            }
//        });
//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            }
//        });

        // 自定义前景装饰物绘制示例 Example of custom date's foreground decor
        List<String> tmpTL = new ArrayList<String>();
        tmpTL.add("2015-10-5");
        tmpTL.add("2015-10-6");
        tmpTL.add("2015-10-7");
        tmpTL.add("2015-10-8");
        tmpTL.add("2015-10-9");
        tmpTL.add("2015-10-10");
        tmpTL.add("2015-10-11");
        DPCManager.getInstance().setDecorTL(tmpTL);

        List<String> tmpTR = new ArrayList<String>();
        tmpTR.add("2015-10-10");
        tmpTR.add("2015-10-11");
        tmpTR.add("2015-10-12");
        tmpTR.add("2015-10-13");
        tmpTR.add("2015-10-14");
        tmpTR.add("2015-10-15");
        tmpTR.add("2015-10-16");
        DPCManager.getInstance().setDecorTR(tmpTR);

        picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 10);
        Calendar c=Calendar.getInstance();
        year_c=c.get(Calendar.YEAR);
        month_c=c.get(Calendar.MONTH)+1;

        day_c=c.get(Calendar.DATE);
        picker.setDate(year_c, month_c);
//        picker.setFestivalDisplay(false);
//        picker.setTodayDisplay(false);
//        picker.setHolidayDisplay(false);
//        picker.setDeferredDisplay(false);
        picker.setMode(DPMode.SINGLE);
        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorTL(Canvas canvas, Rect rect, Paint paint, String data) {
                super.drawDecorTL(canvas, rect, paint, data);
//                switch (data) {
//                    case "2015-10-5":
//                    case "2015-10-7":
//                    case "2015-10-9":
//                    case "2015-10-11":
//                        paint.setColor(Color.GREEN);
//                        canvas.drawRect(rect, paint);
//                        break;
//                    default:
//                        paint.setColor(Color.RED);
//                        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
//                        break;
//                }
                if (data.equals("2015-10-5")||data.equals("2015-10-7")||data.equals("2015-10-9")||data.equals("2015-10-11")) {

                    paint.setColor(Color.GREEN);
                    canvas.drawRect(rect, paint);
                }else{
                        paint.setColor(Color.RED);
                        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);

                }
            }

            @Override
            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint, String data) {
                super.drawDecorTR(canvas, rect, paint, data);
//                switch (data) {
//                    case "2015-10-10":
//                    case "2015-10-11":
//                    case "2015-10-12":
//                        paint.setColor(Color.BLUE);
//                        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
//                        break;
//                    default:
//                        paint.setColor(Color.YELLOW);
//                        canvas.drawRect(rect, paint);
//                        break;
//                }
                if(data.equals("2015-10-10")||data.equals("2015-10-11")||data.equals("2015-10-12")) {
                    paint.setColor(Color.BLUE);
                    canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
                }else{
                        paint.setColor(Color.YELLOW);
                        canvas.drawRect(rect, paint);

                }
            }
        });

        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String date) {
                        Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();

                    }
        });


//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            }
//        });

        // 对话框下的DatePicker示例 Example in dialog
//        Button btnPick = (Button) findViewById(R.id.main_btn);
//        btnPick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
//                dialog.show();
//                DatePicker picker = new DatePicker(MainActivity.this);
//                int year= Calendar.getInstance().get(Calendar.YEAR);
//                int month=Calendar.getInstance().get(Calendar.MONTH)+1;
//                picker.setDate(year, month);
//                picker.setMode(DPMode.SINGLE);
//                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
//                    @Override
//                    public void onDatePicked(String date) {
//                        Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().setContentView(picker, params);
//                dialog.getWindow().setGravity(Gravity.CENTER);
//            }
//        });
//
//        findNavView();
//        findCurDateView();
//        setContentCurView();
    }
    private void findCurDateView() {
        tvCurYearMonth=(TextView) findViewById(R.id.tv_year_month);
        tvCurDay=(ImageView)findViewById(R.id.tv_day);
        tvCurWeek=(TextView)findViewById(R.id.tv_week);
        tvCurLunar=(TextView)findViewById(R.id.tv_lunar);
    }
    int year_c;
    int month_c;
    int day_c;
    private void setContentCurView(){
        StringBuffer textDate = new StringBuffer();

//		draw = getResources().getDrawable(R.drawable.top_day);
//		view.setBackgroundDrawable(draw);

        textDate.append(year_c).append("年").append(month_c).append("月").append("\t");
        tvCurYearMonth.setGravity(Gravity.CENTER_HORIZONTAL);
        tvCurYearMonth.setTextColor(Color.parseColor("#438AFF"));
        tvCurYearMonth.setTextSize(25);
        tvCurYearMonth.setText(textDate);

        /**
         tvCurDay.setGravity(Gravity.CENTER_HORIZONTAL);
         tvCurDay.setTextColor(Color.parseColor("#FFFFFF"));
         tvCurDay.setTextSize(120);
         tvCurDay.setText(day_c+"");*/
        tvCurDay.setImageResource(getResources().getIdentifier("day_"+day_c,"drawable",this.getPackageName()));

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);  //当期日期
        tvCurWeek.setGravity(Gravity.CENTER_HORIZONTAL);
        tvCurWeek.setTextColor(Color.parseColor("#65B9FF"));
        tvCurWeek.setTextSize(35);
        tvCurWeek.setText(week+"");

        LunarCalendar lc=new LunarCalendar();
        tvCurLunar.setGravity(Gravity.CENTER_HORIZONTAL);
        tvCurLunar.setTextColor(Color.parseColor("#438AFF"));
        tvCurLunar.setTextSize(25);
        String curLunarDay=lc.getLunarDate(year_c,month_c,day_c,false);
        curLunarDay=curLunarDay.contains("月")?"初一":curLunarDay;
        String curLunarMonth=lc.getLunarMonth();
        tvCurLunar.setText(curLunarMonth+curLunarDay);


    }

    private void findNavView(){
        mIvBack=(ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
        mIvToday=(ImageView)findViewById(R.id.iv_today);
        mIvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Today();
            }
        });
    }
    private void jump2Today(){
        Calendar c=Calendar.getInstance();
        year_c=c.get(Calendar.YEAR);
        month_c=c.get(Calendar.MONTH)+1;

        day_c=c.get(Calendar.DATE);
        picker.setDate(year_c, month_c);
    }
}
