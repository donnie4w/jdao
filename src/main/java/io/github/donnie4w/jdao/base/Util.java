/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * date 2013-1-13
 * verion 1.0
 */
public class Util {

	public static Date string2Date(String date, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date ret = sdf.parse(date);
		return ret;
	}

	public static String date2String(Date date, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String ret = sdf.format(date);
		return ret;
	}

	public static Date dateFormat(Date date, String format) throws ParseException {
		return string2Date(date2String(date, format), format);
	}
	

}
