package com.plumgine.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.plumgine.serialization.JsonMappers;

public class Util {

    
    private static SecureRandom secureRandom = new SecureRandom();    

    public static String jsonStringify(Object val)
    {
    	try {
			return JsonMappers.defaultMapper.writeValueAsString(val);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
    }

    public static String jsonStringify(ObjectMapper mapper, Object val)
    {
    	try {
			return mapper.writeValueAsString(val);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
    }
    
    public static <T> T jsonParse(Class<T> valueType, String content) {
    	T val;
		try {
			val = JsonMappers.defaultMapper.readValue(content, valueType);
			return val;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}     	
    }

    public static <T> T jsonParseCollection(TypeReference<T> valueType, String content) {
    	try {
    		return JsonMappers.defaultMapper.readValue(content, valueType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}     	

    }
    
    public static String getNextSecureGuid()
    {
    	return new BigInteger(130, secureRandom).toString(32);
    }

    public static Calendar[] getCals()
    {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar[] cals = {cal1, cal2}; 
    	return cals;
    }

    public static boolean isSameDate(Date date1, Date date2)
    {
    	Calendar[] cals = getCals();
    	cals[0].setTime(date1);
    	cals[1].setTime(date2);
    	return cals[0].get(Calendar.YEAR)==cals[1].get(Calendar.YEAR)
    			&& cals[0].get(Calendar.MONTH)==cals[1].get(Calendar.MONTH)
    			&& cals[0].get(Calendar.DAY_OF_YEAR)==cals[1].get(Calendar.DAY_OF_YEAR);
    	
    }
    
    public static boolean isNextDay(Date baseDate, Date nextDate)
    {
    	Calendar[] cals = getCals();
    	cals[0].setTime(baseDate);
    	cals[1].setTime(nextDate);
    	cals[0].add(Calendar.DAY_OF_YEAR, 1);
    	return cals[0].get(Calendar.YEAR)==cals[1].get(Calendar.YEAR)
    			&& cals[0].get(Calendar.MONTH)==cals[1].get(Calendar.MONTH)
    			&& cals[0].get(Calendar.DAY_OF_YEAR)==cals[1].get(Calendar.DAY_OF_YEAR);
    }
    
    public static boolean isSameWeek(Date date1, Date date2)
    {
    	Calendar[] cals = getCals();
    	cals[0].setTime(date1);
    	cals[1].setTime(date2);
    	return cals[0].get(Calendar.YEAR)==cals[1].get(Calendar.YEAR)
    			&& cals[0].get(Calendar.WEEK_OF_YEAR)==cals[1].get(Calendar.WEEK_OF_YEAR);
    }

    public static int parseIntOrZero(String str) {
    	if (str == null) {
    		return 0;
    	}
    	try {
    		return Integer.parseInt(str);
    	} catch (Exception e) {
    		return 0;
    	}
    }

    public static Calendar clearTodayAsCalendar() {
    	Calendar today = Calendar.getInstance();
    	Calendar src = Calendar.getInstance();

    	today.clear();
    	today.set(Calendar.YEAR, src.get(Calendar.YEAR));
    	today.set(Calendar.MONTH, src.get(Calendar.MONTH));
    	today.set(Calendar.DAY_OF_MONTH, src.get(Calendar.DAY_OF_MONTH));

    	return today;
    }

    public static Date tomorrowDateNoTime() {
    	return inNDaysDateNoTime(1);
    }

    public static Date tomorrowDateEndOfDay() {
    	Calendar out = clearTodayAsCalendar();
    	out.add(Calendar.DAY_OF_YEAR, 2);
    	out.add(Calendar.MILLISECOND, -1);
    	return out.getTime();
    }

    public static Date todayDateNoTime() {
    	Calendar out = clearTodayAsCalendar();
    	return out.getTime();
    }
    
    public static Date todayDateEndOfDay() {
    	Calendar out = clearTodayAsCalendar();
    	out.add(Calendar.DAY_OF_YEAR, 1);
    	out.add(Calendar.MILLISECOND, -1);
    	return out.getTime();
    }
    
    public static Date inNDaysDateNoTime(int days) {
    	Calendar out = clearTodayAsCalendar();
    	out.add(Calendar.DAY_OF_MONTH, days);
    	return out.getTime();
    }

    public static Date inNDaysDateEndOfDay(int days) {
    	Calendar out = clearTodayAsCalendar();
    	out.add(Calendar.DAY_OF_MONTH, days);
    	out.add(Calendar.DAY_OF_YEAR, 1);
    	out.add(Calendar.MILLISECOND, -1);
    	return out.getTime();
    }

    public static String inNDaysDateEndOfDayForUI(int days) {
    	Calendar out = clearTodayAsCalendar();
    	out.add(Calendar.DAY_OF_MONTH, days);
    	out.add(Calendar.DAY_OF_YEAR, 1);
    	out.add(Calendar.MILLISECOND, -1);

    	// commented out, since code samples has no messages
    	//String dateFormat = Messages.get("views.java.simpledateformat");
    	String dateFormat = "dd.MM.yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(out.getTime());
    }

	public static Date parseDateOrTomorrow(String val, String pattern) {
		if (val == null) {
			return tomorrowDateNoTime();
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(val);
		} catch (Exception e) {
			return tomorrowDateNoTime();
		}
	}
	
	public static Date parseDateOrEmpty(String val, String pattern) {
		if (val == null) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(val);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> boolean inList(T value, T... params) {
		return Arrays.asList(params).contains(value);		
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

	public static boolean equalsNullSafe(Object o1, Object o2) {
		if (o1 != null) {
			return o1.equals(o2);
		}
		if (o1 == null && o2 == null) {
			return true;
		}
		return false;
	}
}
