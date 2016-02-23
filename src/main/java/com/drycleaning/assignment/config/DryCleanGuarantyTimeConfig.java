package com.drycleaning.assignment.config;

import com.drycleaning.assignment.service.BusinessHoursCalculatorService;
import com.drycleaning.assignment.service.impl.BusinessHoursCalculatorServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Vinod on 2/21/2016.
 */
@Configuration
@EnableAutoConfiguration
public class DryCleanGuarantyTimeConfig {


    @Value("${default.openTime}")
    private String defaultOpeningTime;

    @Value("${default.closeTime}")
    private String defaultClosingTime;


    @Bean
    public BusinessHoursCalculatorService businessHoursCalculatorService() throws Exception{
        return new BusinessHoursCalculatorServiceImpl(defaultOpeningTime, defaultClosingTime);

    }


}
