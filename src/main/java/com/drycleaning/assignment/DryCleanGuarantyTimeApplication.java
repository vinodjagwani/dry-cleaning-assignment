package com.drycleaning.assignment;

import com.drycleaning.assignment.service.impl.BusinessHoursCalculatorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.DayOfWeek;

/**
 * Created by Vinod on 2/20/2016.
 */
@Slf4j
@SpringBootApplication
public class DryCleanGuarantyTimeApplication {

    public static void main(String... args) {
        SpringApplication.run(DryCleanGuarantyTimeApplication.class, args);
        callBusinessHoursCalculatorServiceApi();
    }

    private static void callBusinessHoursCalculatorServiceApi() {
        try {
            BusinessHoursCalculatorServiceImpl businessHourCalculator = new BusinessHoursCalculatorServiceImpl("09:00", "15:00");
            businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
            businessHourCalculator.setOpeningHours("2010-12-24", "08:00", "13:00");
            businessHourCalculator.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
            businessHourCalculator.setClosed("2010-12-25");
            log.debug("Deadline for dry cleaning {} ", businessHourCalculator.calculateDeadline(7 * 60 * 60, "2010-12-24 6:45"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
