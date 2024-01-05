package com.devjoemar.supportengineerscheduling.controller;

import com.devjoemar.supportengineerscheduling.api.ApiResponse;
import com.devjoemar.supportengineerscheduling.api.ScheduleRequest;
import com.devjoemar.supportengineerscheduling.api.converter.ConvertToScheduleResponse;
import com.devjoemar.supportengineerscheduling.dto.ScheduleResponse;
import com.devjoemar.supportengineerscheduling.model.Engineer;
import com.devjoemar.supportengineerscheduling.service.ScheduleService;
import com.devjoemar.supportengineerscheduling.util.Constant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/schedule")
@AllArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ApiResponse<ScheduleResponse> doGenerateSchedule(@RequestBody ScheduleRequest request) {
        LocalDate startDate = parseDate(request.getStartDate());
        LocalDate endDate = parseDate(request.getEndDate());

        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null && request.getNumberOfDays() != null) {
            endDate = startDate.plusDays(request.getNumberOfDays() - 1);
        }

        if (request.getTotalEngineers() == null || endDate == null) {
            return ApiResponse.badRequest(null, Constant.getResponseHashMap(), Constant.RESPONSE_CODE_PREFIX + "11");
        }

        try {
            Map<LocalDate, List<Engineer>> scheduleMap = scheduleService.generateSchedule(request.getTotalEngineers(), startDate, endDate);
            ScheduleResponse response = ConvertToScheduleResponse.convert(scheduleMap);
            return ApiResponse.ok(response, Constant.getResponseHashMap(), Constant.RESPONSE_CODE_PREFIX + "7");
        } catch (Exception e) {
            log.error("Error for request {}", request, e);
            return ApiResponse.internalServerError(null, Constant.getResponseHashMap(), Constant.GENERIC_RESPONSE_CODE);
        }
    }

    private LocalDate parseDate(String dateString) {
        return dateString != null ? LocalDate.parse(dateString) : null;
    }

}
