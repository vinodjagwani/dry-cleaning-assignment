package com.drycleaning.assignment.domain;


import com.drycleaning.assignment.util.DateTimeUtils;
import lombok.*;
import java.util.Date;

/**
 * Created by Vinod on 2/21/2016.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class DayOpeningTime {

    private Date startDate;

    private Date endDate;

    public DayOpeningTime(String start, String end) {
        this.startDate = DateTimeUtils.getDateFromStringTime(start);
        this.endDate = DateTimeUtils.getDateFromStringTime(end);
    }

}
