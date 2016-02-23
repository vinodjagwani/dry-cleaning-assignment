package com.drycleaning.assignment.util;

import com.drycleaning.assignment.domain.DayOpeningTime;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

/**
 * Created by Vinod on 2/20/2016.
 */
@Slf4j
public class DateTimeUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");


    public static Date getDateFromString(String dateString) {
        Date date = null;
        try {
            date = DATE_TIME_FORMAT.parse(dateString);
        } catch (ParseException e) {
            log.error("Parsing failed : {}", e);
        }
        return date;
    }

    public static Date getDateFromStringTime(String dateString) {
        Date date = null;
        try {
            date = TIME_FORMAT.parse(dateString);
        } catch (ParseException e) {
            log.error("Parsing failed : {}", e);
        }
        return date;
    }

    public static String getStringFromDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String getStringFromCalendar(Calendar calendar) {
        return DATE_FORMAT.format(calendar.getTime());
    }

    public static Date getNextWorkingDay(Date date, Map<DayOfWeek, DayOpeningTime> normalWorkingDayMap, Set<String> customizedDateClosed) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        while (isWorkingDay(calendar.getTime(), normalWorkingDayMap, customizedDateClosed)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTime();
    }

    private static boolean isWorkingDay(Date desiredDropOffDateTime, Map<DayOfWeek, DayOpeningTime> normalWorkingDayMap, Set<String> customizedDateClosed) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(desiredDropOffDateTime);
        DateTime dt = DateTime.parse(DateTimeUtils.getStringFromCalendar(calendar));
        DayOfWeek dayOfWeek = DayOfWeek.of(dt.getDayOfWeek());
        String date = getStringFromDate(desiredDropOffDateTime);
        if (customizedDateClosed.contains(date) || !normalWorkingDayMap.containsKey(dayOfWeek)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static Date getOpeningTime(Date date, Map<DayOfWeek, DayOpeningTime> normalWorkingDayMap, Map<String, DayOpeningTime> customizedDateMap) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar startTimeCalendar = Calendar.getInstance();
        Date startTime;
        if (customizedDateMap.containsKey(getStringFromDate(date))) {
            startTime = customizedDateMap.get(getStringFromDate(date)).getStartDate();
            startTimeCalendar.setTime(startTime);
            calendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE));
        } else {
            DateTime dt = DateTime.parse(DateTimeUtils.getStringFromCalendar(calendar));
            DayOfWeek dayOfWeek = DayOfWeek.of(dt.getDayOfWeek());
            startTime = normalWorkingDayMap.get(dayOfWeek).getStartDate();
            startTimeCalendar.setTime(startTime);
            calendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE));
        }
        return calendar.getTime();
    }

    public static Date getClosingTime(Date date, Map<DayOfWeek, DayOpeningTime> normalWorkingDayMap, Map<String, DayOpeningTime> customizedDateMap) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date endTime;
        Calendar endTimeCalendar = Calendar.getInstance();
        if (customizedDateMap.containsKey(getStringFromDate(date))) {
            endTime = customizedDateMap.get(getStringFromDate(date)).getEndDate();
        } else {
            DateTime dt = DateTime.parse(DateTimeUtils.getStringFromCalendar(endTimeCalendar));
            DayOfWeek dayOfWeek = DayOfWeek.of(dt.getDayOfWeek());
            endTime = normalWorkingDayMap.get(dayOfWeek).getEndDate();
        }
        endTimeCalendar.setTime(endTime);
        calendar.set(Calendar.HOUR_OF_DAY, endTimeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, endTimeCalendar.get(Calendar.MINUTE));
        return calendar.getTime();
    }

}
