package com.ubtechinc.cruzr.calendar.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

import com.ubtechinc.cruzr.calendar.CalendarApplication;
import com.ubtechinc.cruzr.calendar.R;
import com.ubtechinc.cruzr.calendar.borderText.BorderText;
import com.ubtechinc.cruzr.calendar.dao.ScheduleDAO;
import com.ubtechinc.cruzr.calendar.calendarModel.LunarCalendar;

/**
 * 日历显示activity
 * @author jack_peng
 *
 */
public class CalendarActivity extends Activity implements OnGestureListener,CalendarView.IObserveToday{
	private static final String TAG = "CalendarActivity";
	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	
	private ScheduleDAO dao = null;

	private TextView tvCurYearMonth;
	private TextView tvCurDay;
	private TextView tvCurWeek;
	private TextView tvCurLunar;

	private ImageView mIvBack;
	private ImageView mIvToday;

	private ImageView mIvLastMonth;
	private ImageView mIvNextMonth;

	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	
    	dao = new ScheduleDAO(this);

	}

	/**
	 * 当前日期控件
	 */
	private void findCurDateView() {
		tvCurYearMonth=(TextView) findViewById(R.id.tv_year_month);
		tvCurDay=(TextView)findViewById(R.id.tv_day);
		tvCurWeek=(TextView)findViewById(R.id.tv_week);
		tvCurLunar=(TextView)findViewById(R.id.tv_lunar);

		if(Locale.getDefault().getLanguage().equals("en")){
			tvCurLunar.setVisibility(View.GONE);
		}
	}

	/**
	 * 导航栏的返回和今天按钮
	 */
	private void findNavView(){
        mIvBack=(ImageView) findViewById(R.id.iv_back);
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CalendarActivity.this.finish();
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

	/**
	 * 上，下一个月的箭头控件
	 */
	private void findTopArrowView(){
		mIvLastMonth=(ImageView) findViewById(R.id.iv_last_month);
		mIvNextMonth=(ImageView) findViewById(R.id.iv_next_month);

		mIvLastMonth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                   jumpToLastMonth();
			}
		});

		mIvNextMonth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                   jumpToNextMonth();
			}
		});
	}

	/**
	 * 跳转下个月
	 */
	private void jumpToNextMonth(){
		int gvFlag=0;
		addGridView();   //添加一个gridView
		jumpMonth++;     //下一个月

		calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		calV.setIObserveToday(this);

		gridView.setAdapter(calV);
		addTextToTopTextView(topText);
		gvFlag++;
		Log.e(TAG, "jumpToNextMonth: left->gvFlag:"+gvFlag);
		flipper.addView(gridView, gvFlag);

		Animation animIn1=AnimationUtils.loadAnimation(this,R.anim.push_left_in);
		Animation animOut1=AnimationUtils.loadAnimation(this,R.anim.push_left_out);

		this.flipper.setInAnimation(animIn1);
		this.flipper.setOutAnimation(animOut1);
		this.flipper.showNext();
		flipper.removeViewAt(0);
	}

	/**
	 * 跳转上个月
	 */
	private void jumpToLastMonth(){
		int gvFlag=0;
		//向右滑动
		addGridView();   //添加一个gridView
		jumpMonth--;     //上一个月

		calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		calV.setIObserveToday(this);

		gridView.setAdapter(calV);
		gvFlag++;
		Log.e(TAG, "onFling: right->gvFlag:"+gvFlag);
		addTextToTopTextView(topText);
		flipper.addView(gridView,gvFlag);

		Animation animIn2=AnimationUtils.loadAnimation(this,R.anim.push_right_in);
		Animation animOut2=AnimationUtils.loadAnimation(this,R.anim.push_right_out);
		this.flipper.setInAnimation(animIn2);
		this.flipper.setOutAnimation(animOut2);
		this.flipper.showPrevious();
		flipper.removeViewAt(0);
	}


    /**
     * 跳转到今天
     */
	private void jump2Today(){
        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gvFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();   //添加一个gridView
        	year_c = Integer.parseInt(currentDate.split("-")[0]);
        	month_c = Integer.parseInt(currentDate.split("-")[1]);
        	day_c = Integer.parseInt(currentDate.split("-")[2]);
        	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		    calV.setIObserveToday(this);


	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView,gvFlag);
	        if(xMonth == 0 && xYear == 0){
	        	//nothing to do
	        }else if((xYear == 0 && xMonth >0) || xYear >0){
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
				this.flipper.showNext();
	        }else{
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
				this.flipper.showPrevious();
	        }
			flipper.removeViewAt(0);

		setContentCurView();
	}

	/**
	 * 设置当前日期
	 */
	private void setContentCurView(){
		StringBuffer textDate = new StringBuffer();

		if(Locale.getDefault().getLanguage().equals("en")){
			textDate.append(getString(getResources().getIdentifier("month_"+month_c,"string",getPackageName()))).append(" ").append(year_c).append("\t");
		}else if(Locale.getDefault().getLanguage().equals("zh")){
			textDate.append(year_c).append("年").append(month_c).append("月").append("\t");
		}

		tvCurYearMonth.setGravity(Gravity.CENTER_HORIZONTAL);
		tvCurYearMonth.setTextColor(Color.parseColor("#438AFF"));
		tvCurYearMonth.setTextSize(25);
		tvCurYearMonth.setText(textDate);


		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0,40,0,0);
		tvCurDay.setLayoutParams(lp);
		tvCurDay.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
		tvCurDay.setTextColor(Color.parseColor("#FFFFFF"));
		tvCurDay.setTextSize(140);
		tvCurDay.setPadding(-1,-1,-1,-1);
		tvCurDay.setText(day_c+"");


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
		String curLunarDay=lc.getLunarDate(year_c,month_c,day_c,true);
		curLunarDay=curLunarDay.contains("月")?"初一":curLunarDay;
		String curLunarMonth=lc.getLunarMonth();
		tvCurLunar.setText(curLunarMonth+curLunarDay);


	}
	private void setContentCurView(int year,int month,int day,String week){
		StringBuffer textDate = new StringBuffer();

		if(Locale.getDefault().getLanguage().equals("en")){
//			textDate.append(getString(getResources().getIdentifier("month_"+month_c,"string",getPackageName()))).append(" ").append(year_c).append("\t");
			textDate.append(getString(getResources().getIdentifier("month_"+month,"string",getPackageName()))).append(" ").append(year).append("\t");
		}else if(Locale.getDefault().getLanguage().equals("zh")){
//			textDate.append(year_c).append("年").append(month_c).append("月").append("\t");
			textDate.append(year).append("年").append(month).append("月").append("\t");
		}

		tvCurYearMonth.setGravity(Gravity.CENTER_HORIZONTAL);
		tvCurYearMonth.setTextColor(Color.parseColor("#438AFF"));
		tvCurYearMonth.setTextSize(25);
		tvCurYearMonth.setText(textDate);

		 LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		 lp.setMargins(0,40,0,0);
		 tvCurDay.setLayoutParams(lp);
		 tvCurDay.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
		 tvCurDay.setTextColor(Color.parseColor("#FFFFFF"));
		 tvCurDay.setTextSize(140);
		 tvCurDay.setPadding(0,0,0,0);
		 tvCurDay.setText(day+"");

		tvCurWeek.setGravity(Gravity.CENTER_HORIZONTAL);
		tvCurWeek.setTextColor(Color.parseColor("#65B9FF"));
		tvCurWeek.setTextSize(35);
		tvCurWeek.setText(week+"");

		LunarCalendar lc=new LunarCalendar();
		tvCurLunar.setGravity(Gravity.CENTER_HORIZONTAL);
		tvCurLunar.setTextColor(Color.parseColor("#438AFF"));
		tvCurLunar.setTextSize(25);
		String curLunarDay=lc.getLunarDate(year,month,day,true);
		curLunarDay=curLunarDay.contains("月")?"初一":curLunarDay;
		String curLunarMonth=lc.getLunarMonth();
		tvCurLunar.setText(curLunarMonth+curLunarDay);

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_whole_small);
		setContentView(R.layout.main);

		CalendarApplication.addAct(this);

		findNavView();

		gestureDetector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();


        calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		calV.setIObserveToday(this);
        
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView,0);
        
		topText = (BorderText) findViewById(R.id.toptext);
		addTextToTopTextView(topText);

		findCurDateView();
		setContentCurView();

		findTopArrowView();
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 120) {
            //像左滑动
			addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			calV.setIObserveToday(this);

	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
			Log.e(TAG, "onFling: left->gvFlag:"+gvFlag);
			flipper.addView(gridView, gvFlag);


			Animation animIn1=AnimationUtils.loadAnimation(this,R.anim.push_left_in);
			Animation animOut1=AnimationUtils.loadAnimation(this,R.anim.push_left_out);

			this.flipper.setInAnimation(animIn1);
			this.flipper.setOutAnimation(animOut1);
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
			addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			calV.setIObserveToday(this);

	        gridView.setAdapter(calV);
	        gvFlag++;
			Log.e(TAG, "onFling: right->gvFlag:"+gvFlag);
	        addTextToTopTextView(topText);
	        flipper.addView(gridView,gvFlag);


			Animation animIn2=AnimationUtils.loadAnimation(this,R.anim.push_right_in);
			Animation animOut2=AnimationUtils.loadAnimation(this,R.anim.push_right_out);
			this.flipper.setInAnimation(animIn2);
			this.flipper.setOutAnimation(animOut2);
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();


		if(Locale.getDefault().getLanguage().equals("en")){
			textDate.append(getString(getResources().getIdentifier("month_"+calV.getShowMonth(),"string",getPackageName()))).append(" ").append(calV.getShowYear()).append("\t");
		}else if(Locale.getDefault().getLanguage().equals("zh")){
			textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
		}

		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setGravity(Gravity.CENTER);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//添加gridview
	private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				1176, LayoutParams.WRAP_CONTENT);
		params.setMargins(-1,-1,-1,-1);
		//取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}
		gridView.setGravity(Gravity.CENTER_HORIZONTAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(0);

		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
//				  if(startPosition <= position  && position <= endPosition){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                  
	                  //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
	                  String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	                  if(scheduleIDs != null && scheduleIDs.length > 0){
	                	  //跳转到显示这一天的所有日程信息界面
		  				  Intent intent = new Intent();
		  				  intent.setClass(CalendarActivity.this, ScheduleInfoView.class);
		                  intent.putExtra("scheduleID", scheduleIDs);
		  				  startActivity(intent);  
		  				  
	                  }else{
						  week=getResources().getStringArray(R.array.calendar_week_full)[position%7];
						 
		                  ArrayList<String> scheduleDate = new ArrayList<String>();
		                  scheduleDate.add(scheduleYear);
		                  scheduleDate.add(scheduleMonth);
		                  scheduleDate.add(scheduleDay);
		                  scheduleDate.add(week);

		                  int rst=calV.getLastOrNextMonth(position);
						  int month;
						  if(rst==-1){
							  month=Integer.parseInt(scheduleMonth)-1;
						  }else if(rst==1){
							  month=Integer.parseInt(scheduleMonth)+1;
						  }else {
							  month=Integer.parseInt(scheduleMonth);
						  }

						  calV.setSelection(position);
						  calV.notifyDataSetChanged();

		                  setContentCurView(Integer.parseInt(scheduleYear),month,Integer.parseInt(scheduleDay),week);
	                  }
//				  }
			}
		});
		gridView.setLayoutParams(params);
	}

    private String lan=Locale.getDefault().getLanguage();

	@Override
	public void withToday() {
		Log.e(TAG, "withToday: lan="+lan);
		mIvToday.setBackgroundResource(getResources().getIdentifier("today_"+lan,"drawable",getPackageName()));
		mIvToday.setVisibility(View.VISIBLE);
	}

	@Override
	public void withoutToday() {
		Log.e(TAG, "withoutToday: ");
		mIvToday.setVisibility(View.GONE);
	}
}