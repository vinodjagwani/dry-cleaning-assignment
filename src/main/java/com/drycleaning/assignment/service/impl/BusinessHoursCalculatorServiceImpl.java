package com.drycleaning.assignment.service.impl;

import com.drycleaning.assignment.domain.DayOpeningTime;
import com.drycleaning.assignment.service.BusinessHoursCalculatorService;
import com.drycleaning.assignment.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.util.Assert.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vinod on 2/20/2016.
 */
@Slf4j
public class BusinessHoursCalculatorServiceImpl implements BusinessHoursCalculatorService {


    private Map<DayOfWeek, DayOpeningTime> normalWorkingDayMap;

    private Map<String, DayOpeningTime> customizedDateMap;

    private Set<String> customizedDateClosed;

    public BusinessHoursCalculatorServiceImpl(String defaultOpeningTime, String defaultClosingTime) throws ParseException {
        this.normalWorkingDayMap = new HashMap<>();
        this.customizedDateMap = new HashMap<>();
        this.customizedDateClosed = new HashSet<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            normalWorkingDayMap.put(dayOfWeek, new DayOpeningTime(defaultOpeningTime, defaultClosingTime));
        }
    }

    public void setOpeningHours(final DayOfWeek dayOfWeek, final String openTime, final String closeTime) throws ParseException {
        notNull(dayOfWeek);
        notNull(openTime);
        notNull(closeTime);
        log.info("Opening hours on {} from {} to {}", dayOfWeek.name(), openTime, closeTime);
        normalWorkingDayMap.put(dayOfWeek, new DayOpeningTime(openTime, closeTime));
    }

    public void setOpeningHours(final String date, final String openTime, final String closeTime) throws ParseException {
        notNull(date);
        notNull(openTime);
        notNull(closeTime);
        log.info("Opening hours on {} from {} to {}", date, openTime, closeTime);
        customizedDateMap.put(date, new DayOpeningTime(openTime, closeTime));
    }

    public void setClosed(final DayOfWeek... closingDays) {
        log.debug("DryCleaning shop closed on {}", Arrays.asList(closingDays));
        for (DayOfWeek closingDay : closingDays) {
            normalWorkingDayMap.remove(closingDay);
        }
    }

    public void setClosed(final String... closingDates) {
        log.debug("DryCleaning shop closed on {}", Arrays.asList(closingDates));
            customizedDateClosed.addAll(Arrays.asList(closingDates));
    }

    public Date calculateDeadline(long timeInterval, String clothArrivalDateStr)  {
        notNull(timeInterval);
        notNull(clothArrivalDateStr);
        log.debug("Calculate deadline for time interval {}  on {}", timeInterval, clothArrivalDateStr);
        Date clothArrivalDate = DateTimeUtils.getDateFromString(clothArrivalDateStr);
        clothArrivalDate = DateTimeUtils.getNextWorkingDay(clothArrivalDate, normalWorkingDayMap, customizedDateClosed);
        Date openingHours = DateTimeUtils.getOpeningTime(clothArrivalDate, normalWorkingDayMap, customizedDateMap);
        Date closingHours = DateTimeUtils.getClosingTime(clothArrivalDate, normalWorkingDayMap, customizedDateMap);
        if (clothArrivalDate.before(openingHours)) {
            clothArrivalDate = openingHours;
        }
        if (clothArrivalDate.after(closingHours)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(clothArrivalDate);
            calendar.add(Calendar.DAY_OF_MONTH, BigDecimal.ONE.intValue());
            Date nextWorkingDay = DateTimeUtils.getNextWorkingDay(calendar.getTime(), normalWorkingDayMap, customizedDateClosed);
            clothArrivalDate = DateTimeUtils.getOpeningTime(nextWorkingDay, normalWorkingDayMap, customizedDateMap);
        }
        return getGuarantyTimeForInterval(timeInterval, closingHours, clothArrivalDate);
    }


    private Date getGuarantyTimeForInterval(long timeInterval, Date closingHours, Date clothArrivalDate) {
        log.debug("Calculate Guaranty time for interval {} on {} with closing  {}", timeInterval, clothArrivalDate, closingHours);
        long diffInHours = closingHours.getTime() - clothArrivalDate.getTime();
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInHours);
        diffInSeconds = diffInSeconds < BigDecimal.ZERO.intValue() ? -diffInSeconds : diffInSeconds;
        if (diffInSeconds < timeInterval) {
            long actualDiff = diffInSeconds - timeInterval;
            actualDiff = actualDiff < BigDecimal.ZERO.intValue() ? -actualDiff : actualDiff;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(clothArrivalDate);
            calendar.add(Calendar.DAY_OF_MONTH, BigDecimal.ONE.intValue());
            Date nextWorkingDay = DateTimeUtils.getNextWorkingDay(calendar.getTime(), normalWorkingDayMap, customizedDateClosed);
            Date openingTime = DateTimeUtils.getOpeningTime(nextWorkingDay, normalWorkingDayMap, customizedDateMap);
            Date closingTime = DateTimeUtils.getClosingTime(nextWorkingDay, normalWorkingDayMap, customizedDateMap);
            while (true) {
                long businessHoursInSeconds = TimeUnit.MILLISECONDS.toSeconds(closingTime.getTime() - openingTime.getTime());
                if (actualDiff > businessHoursInSeconds) {
                    actualDiff = actualDiff - businessHoursInSeconds;
                    calendar.add(Calendar.DAY_OF_MONTH, BigDecimal.ONE.intValue());
                    nextWorkingDay = DateTimeUtils.getNextWorkingDay(calendar.getTime(), normalWorkingDayMap, customizedDateClosed);
                    openingTime = DateTimeUtils.getOpeningTime(nextWorkingDay, normalWorkingDayMap, customizedDateMap);
                    closingTime = DateTimeUtils.getClosingTime(nextWorkingDay, normalWorkingDayMap, customizedDateMap);
                } else {
                    timeInterval = actualDiff;
                    clothArrivalDate = openingTime;
                    break;
                }
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(clothArrivalDate);
        calendar.add(Calendar.SECOND, (int) timeInterval);
        return calendar.getTime();
    }

}
