package com.drycleaning.assignment.service;

import com.drycleaning.assignment.AbstractDryCleanGuarantyTimeTest;
import com.drycleaning.assignment.util.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.DayOfWeek;
import java.util.Date;

/**
 * Created by Vinod on 2/19/2016.
 */

public class BusinessHoursCalculatorServiceTest extends AbstractDryCleanGuarantyTimeTest {

    @Autowired
    private BusinessHoursCalculatorService businessHoursCalculatorService;


    @Test
    public void testDryCleanSameDay() throws Exception{
        businessHoursCalculatorService.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHoursCalculatorService.setOpeningHours("2016-02-22", "08:00", "13:00");
        businessHoursCalculatorService.setClosed(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY);
        businessHoursCalculatorService.setClosed("2016-02-24");
        Date cleaningTime =  DateTimeUtils.getDateFromString("2016-02-22 12:00");
        Date clothsArrivalTime = businessHoursCalculatorService.calculateDeadline(2*60*60, "2016-02-22 10:00");
        Assert.assertEquals(clothsArrivalTime, cleaningTime);
    }

    @Test
    public void testDryCleanNextWorkingDay() throws Exception{
        businessHoursCalculatorService.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHoursCalculatorService.setOpeningHours("2016-02-22", "08:00", "13:00");
        businessHoursCalculatorService.setClosed(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY);
        businessHoursCalculatorService.setClosed("2016-02-24");
        Date cleaningTime =  DateTimeUtils.getDateFromString("2016-02-23 13:45");
        Date clothsArrivalTime = businessHoursCalculatorService.calculateDeadline(5*60*60, "2016-02-22 12:45");
        Assert.assertEquals(clothsArrivalTime, cleaningTime);
    }

    @Test
    public void testDryCleanAfterWeekend() throws Exception{
        businessHoursCalculatorService.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHoursCalculatorService.setOpeningHours("2016-02-23", "08:00", "13:00");
        businessHoursCalculatorService.setClosed(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        Date cleaningTime =  DateTimeUtils.getDateFromString("2016-02-29 14:00");
        Date clothsArrivalTime = businessHoursCalculatorService.calculateDeadline(8*60*60, "2016-02-26 12:00");
        Assert.assertEquals(clothsArrivalTime, cleaningTime);
    }

    @Test
    public void testDryCleanBeforeWorkingHours() throws Exception{
        businessHoursCalculatorService.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHoursCalculatorService.setOpeningHours("2016-02-26", "08:00", "13:00");
        businessHoursCalculatorService.setClosed(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY);
        Date cleaningTime =  DateTimeUtils.getDateFromString("2016-02-22 11:00");
        Date clothsArrivalTime = businessHoursCalculatorService.calculateDeadline(2*60*60, "2016-02-22 07:45");
        Assert.assertEquals(clothsArrivalTime, cleaningTime);
    }

    @Test
    public void testDryCleanAfterHolidayAndWeekend() throws Exception{
        businessHoursCalculatorService.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHoursCalculatorService.setOpeningHours("2010-12-24", "08:00", "13:00");
        businessHoursCalculatorService.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
        businessHoursCalculatorService.setClosed("2010-12-25");
        Date cleaningTime =  DateTimeUtils.getDateFromString("2010-12-27 11:00");
        Date clothsArrivalTime = businessHoursCalculatorService.calculateDeadline(7*60*60, "2010-12-24 06:45");
        Assert.assertEquals(clothsArrivalTime, cleaningTime);
    }
}
