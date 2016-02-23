package com.drycleaning.assignment.service;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.Date;

/**
 * Created by Vinod on 2/19/2016.
 */
public interface BusinessHoursCalculatorService {


    void setOpeningHours(final DayOfWeek dayOfWeek, final String openTime, final String closeTime) throws ParseException;

    void setOpeningHours(final String date, final String openTime, final String closeTime) throws ParseException;

    void setClosed(final DayOfWeek... closingDay);

    void setClosed(final String... closingDate);

    Date calculateDeadline(long timeInterval, String startDateTime);

}
