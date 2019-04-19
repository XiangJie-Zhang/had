package com.hpe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 *对于路径的一个截取
 */
public class StringTools {
	public static String changePath(String path){
		String substring = path.substring(path.indexOf("//")+2);

		substring = substring.substring(substring.indexOf("/"));
		/*if(substring.indexOf("/")==0){

		}*/
		//System.out.println(substring);
		return substring;
	}
	public static String getCurrentTime(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * 获取到7天前的时间
	 */
	public static String get7DaysAgo(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		c.add(Calendar.DATE, - 7);

		Date monday = c.getTime();

		String preMonday = sdf.format(monday);

		return preMonday;

	}
	/**

	 * 获得指定日期的后一天

	 * @param specifiedDay

	 * @return

	 */

	public static String getSpecifiedDayAfter(String specifiedDay){

		Calendar c = Calendar.getInstance();

		Date date=null;

		try {

			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);

		} catch (ParseException e) {

			e.printStackTrace();

		}

		c.setTime(date);

		int day=c.get(Calendar.DATE);

		c.set(Calendar.DATE,day+1);

		String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

		return dayAfter;

	}


	/**

	 * 获得指定日期的前一天

	 * @param specifiedDay

	 * @return

	 */

	public static String getSpecifiedDayBefore(){

		Calendar c = Calendar.getInstance();

		Date date=null;

		try {
			String specifiedDay =  new SimpleDateFormat("yy-MM-dd").format(new Date());

			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);

		} catch (ParseException e) {

			e.printStackTrace();

		}

		c.setTime(date);

		int day=c.get(Calendar.DATE);

		c.set(Calendar.DATE,day-1);

		String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

		return dayAfter;

	}


	/**
	 * 获取到7天的时间
	 */
	public static List<String> get7Days(){
		List<String> list = new ArrayList<>();
		String curTime = get7DaysAgo();
		String eTime = getSpecifiedDayBefore();
		while (!curTime.equals(StringTools.getSpecifiedDayAfter(eTime))){
			list.add(curTime);
			curTime  = getSpecifiedDayAfter(curTime);
		}
		return list;

	}


}
