package com.devjoemar.supportengineerscheduling.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ScheduleRequest {
    private Integer totalEngineers;
    private Integer numberOfDays; // Optional, can be null
    private String startDate; // Optional, can be null
    private String endDate; // Optional, can be null
}
