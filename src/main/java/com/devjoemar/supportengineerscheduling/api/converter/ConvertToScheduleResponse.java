package com.devjoemar.supportengineerscheduling.api.converter;

import com.devjoemar.supportengineerscheduling.dto.ScheduleResponse;
import com.devjoemar.supportengineerscheduling.model.Engineer;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvertToScheduleResponse {

    /**
     * Converts a schedule map to a ScheduleResponse.
     *
     * @param scheduleMap The map of dates to lists of engineers.
     * @return A ScheduleResponse object.
     */
    public static ScheduleResponse convert(Map<LocalDate, List<Engineer>> scheduleMap) {
        final List<ScheduleResponse.DaySchedule> daySchedules = scheduleMap.entrySet().stream()
                .map(ConvertToScheduleResponse::convertEntryToDaySchedule)
                .collect(Collectors.toList());

        return new ScheduleResponse(daySchedules);
    }

    private static ScheduleResponse.DaySchedule convertEntryToDaySchedule(Map.Entry<LocalDate, List<Engineer>> entry) {
        final List<ScheduleResponse.EngineerDTO> engineerDTOs = convertEngineersToDTOs(entry.getValue());
        return new ScheduleResponse.DaySchedule(entry.getKey().toString(), engineerDTOs);
    }

    private static List<ScheduleResponse.EngineerDTO> convertEngineersToDTOs(List<Engineer> engineers) {
        return engineers.stream()
                .map(ConvertToScheduleResponse::convertEngineerToDTO)
                .collect(Collectors.toList());
    }

    private static ScheduleResponse.EngineerDTO convertEngineerToDTO(Engineer engineer) {
        return new ScheduleResponse.EngineerDTO(engineer.getId(), engineer.getName());
    }
}
