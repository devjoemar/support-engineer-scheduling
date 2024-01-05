package com.devjoemar.supportengineerscheduling.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devjoemar.supportengineerscheduling.model.Engineer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test the schedule generation with a specific number of days from the current date
    @Test
    public void testScheduleFromCurrentDateForNumberOfDays() {
        final LocalDate startDate = LocalDate.now();
        final int numberOfDays = 60;
        final LocalDate endDate = startDate.plusDays(numberOfDays - 1);

        final Map<LocalDate, List<Engineer>> schedule = scheduleService.generateSchedule(25, startDate, endDate);

        assertEquals(calculateExpectedWorkingDays(startDate, endDate), schedule.size(),
                "Schedule does not cover the expected number of working days.");
        assertNoConsecutiveShifts(schedule);
        assertFairDistribution(schedule, calculateMaxShiftsPerEngineer(numberOfDays, 25));
        assertNonEmptySchedules(schedule);
    }

    // Test the schedule generation with a specific number of days from a given start date
    @Test
    public void testScheduleFromSpecificStartDateForNumberOfDays() {
        final LocalDate startDate = LocalDate.parse("2022-01-01");
        final int numberOfDays = 60;
        final LocalDate endDate = startDate.plusDays(numberOfDays - 1);

        final Map<LocalDate, List<Engineer>> schedule = scheduleService.generateSchedule(25, startDate, endDate);

        assertEquals(calculateExpectedWorkingDays(startDate, endDate), schedule.size(),
                "Schedule does not cover the expected number of working days.");
        assertNoConsecutiveShifts(schedule);
        assertFairDistribution(schedule, calculateMaxShiftsPerEngineer(numberOfDays, 25));
        assertNonEmptySchedules(schedule);
    }

    // Test the schedule generation between a given start date and end date
    @Test
    public void testScheduleBetweenStartAndEndDate() {
        final LocalDate startDate = LocalDate.parse("2022-01-01");
        final LocalDate endDate = LocalDate.parse("2022-01-31");

        final Map<LocalDate, List<Engineer>> schedule = scheduleService.generateSchedule(25, startDate, endDate);

        assertEquals(calculateExpectedWorkingDays(startDate, endDate), schedule.size(),
                "Schedule does not match the date range.");
        assertNoConsecutiveShifts(schedule);
        assertFairDistribution(schedule, calculateMaxShiftsPerEngineer(31, 25));
        assertNonEmptySchedules(schedule);
    }

    // Test the schedule generation with more engineers than days
    @Test
    public void testScheduleWithMoreEngineersThanDays() {
        final LocalDate startDate = LocalDate.parse("2024-01-01");
        final int numberOfDays = 10;
        final int totalEngineers = 15; // More engineers than days
        final LocalDate endDate = startDate.plusDays(numberOfDays - 1);

        final Map<LocalDate, List<Engineer>> schedule = scheduleService.generateSchedule(totalEngineers, startDate, endDate);

        assertEquals(calculateExpectedWorkingDays(startDate, endDate), schedule.size(),
                "Schedule does not cover the expected number of working days.");
        assertNoConsecutiveShifts(schedule);
        assertFairDistribution(schedule, calculateMaxShiftsPerEngineer(numberOfDays, totalEngineers));
        assertNonEmptySchedules(schedule);
    }

    private void assertNoConsecutiveShifts(Map<LocalDate, List<Engineer>> schedule) {
        Set<Engineer> lastDayEngineers = new HashSet<>();
        for (List<Engineer> engineers : schedule.values()) {
            for (Engineer engineer : engineers) {
                assertFalse(lastDayEngineers.contains(engineer),
                        "An engineer is assigned on consecutive days.");
            }
            lastDayEngineers.clear();
            lastDayEngineers.addAll(engineers);
        }
    }

    private void assertFairDistribution(Map<LocalDate, List<Engineer>> schedule, int maxShiftsPerEngineer) {
        Map<Engineer, Long> shiftCounts = schedule.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (Long shifts : shiftCounts.values()) {
            assertTrue(shifts <= maxShiftsPerEngineer,
                    "An engineer is scheduled more than the expected maximum number of shifts.");
        }
    }

    private int calculateExpectedWorkingDays(LocalDate start, LocalDate end) {
        int workingDays = 0;
        LocalDate date = start;
        while (!date.isAfter(end)) {
            if (!isWeekend(date)) {
                workingDays++;
            }
            date = date.plusDays(1);
        }
        return workingDays;
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;
    }

    private void assertNonEmptySchedules(Map<LocalDate, List<Engineer>> schedule) {
        for (Map.Entry<LocalDate, List<Engineer>> entry : schedule.entrySet()) {
            assertFalse(entry.getValue().isEmpty(), "The schedule for " + entry.getKey() + " should not be empty.");
        }
    }

    private int calculateMaxShiftsPerEngineer(int numberOfDays, int totalEngineers) {
        return (int) Math.ceil((double) numberOfDays * 2 / totalEngineers);
    }
}