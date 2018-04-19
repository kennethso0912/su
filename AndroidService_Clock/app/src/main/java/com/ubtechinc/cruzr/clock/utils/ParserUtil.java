/*
 * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 */

package com.ubtechinc.cruzr.clock.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.ubtechinc.cruzr.sdk.face.StringUtils;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created on 2017/2/24.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Des 提供assert目录下json文件格式解析工具类
 */

public class ParserUtil {
	private static final String TAG = "ParserUtil";
	public static final String FILE_PATH ="conf/timezones.js";
	public static final String FILE_PATH_EN="conf/timezones_en.js";
	public static final String FILE_PATH_TW="conf/timezones_tw.js";

	private static final Object sLocaleLock = new Object();
	private static Locale sIs24HourLocale;
	private static boolean sIs24Hour;

	private ParserUtil(){}

    /**
	 * 解析文件为json
	 * @param context
	 * @param path
	 * @return
     */
	public static JsonObject parseFile(Context context, String path) {
		InputStream in = null;
		JsonObject root=null;
		try {
			in = context.openFileInput(path);
		} catch (FileNotFoundException e1) {
			Log.e(TAG, "parseFile: file not found");
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "read config file failed." + path);
		}
		if (null == in) {
			try {
				AssetManager assets = context.getAssets();
				in = assets.open(path);
			} catch (IOException e) {
				Log.e(TAG, "read config file failed.");
			}
		}
		JsonElement rootJson;
		final JsonParser jsonParser = new JsonParser();

		try {
			if (in != null) {
				rootJson = jsonParser.parse(new InputStreamReader(in));
				root = rootJson.getAsJsonObject();
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore.
				}
			}
		}
		return root;
	}



	/**
	 * 根据地图数据路径获取地图名和称
	 * @param path 地图路径
	 * @return  地图名称
	 */
	public static String extractMapNameFromPath(String path){
		String mapName="";
		if (StringUtils.isEmpty(path)) {
            return mapName;
		}
		int index=path.lastIndexOf("/");
		if(index>0){
            mapName=path.substring(index+1);
		}
		return mapName;
	}

	public static float stringToFloat(String s){
		Float f=0f;
		try {
			f = Float.valueOf(s);
		}catch(NumberFormatException ex){
			Log.e(TAG, "stringToFloat: wrong format->");
		}
		return f;
	}

	public static boolean is24HourFormat(Context context) {
		String value = Settings.System.getString(context.getContentResolver(),
				Settings.System.TIME_12_24);

		if (value == null) {
			Locale locale = context.getResources().getConfiguration().locale;

			synchronized (sLocaleLock) {
				if (sIs24HourLocale != null && sIs24HourLocale.equals(locale)) {
					return sIs24Hour;
				}
			}

			java.text.DateFormat natural =
					java.text.DateFormat.getTimeInstance(java.text.DateFormat.LONG, locale);

			if (natural instanceof SimpleDateFormat) {
				SimpleDateFormat sdf = (SimpleDateFormat) natural;
				String pattern = sdf.toPattern();

				if (pattern.indexOf('H') >= 0) {
					value = "24";
				} else {
					value = "12";
				}
			} else {
				value = "12";
			}

			synchronized (sLocaleLock) {
				sIs24HourLocale = locale;
				sIs24Hour = value.equals("24");
			}

			return sIs24Hour;
		}

		return value.equals("24");
	}
}
