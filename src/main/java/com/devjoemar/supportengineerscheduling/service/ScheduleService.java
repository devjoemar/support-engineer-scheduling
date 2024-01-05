package com.devjoemar.supportengineerscheduling.service;

import com.devjoemar.supportengineerscheduling.model.Engineer;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ScheduleService {

    public Map<LocalDate, List<Engineer>> generateSchedule(int numEngineers, LocalDate startDate, LocalDate endDate) {

        endDate = endDate != null ? endDate : startDate.plusDays(numEngineers - 1);

        final int daysInSchedule = (int) ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));

        final Set<Engineer> availableEngineers = new HashSet<>(initializeEngineers(numEngineers));
        Map<Engineer, Integer> shiftsCompleted = new HashMap<>();
        Set<Engineer> lastDayEngineers = new HashSet<>();

        Map<LocalDate, List<Engineer>> schedule = new LinkedHashMap<>();
        LocalDate currentDate = startDate;

        for (int i = 0; i < daysInSchedule; i++) {
            if (!isWeekend(currentDate)) {
                final List<Engineer> engineersForDay = selectEngineersForDay(availableEngineers,
                        shiftsCompleted, lastDayEngineers, daysInSchedule, numEngineers);
                schedule.put(currentDate, engineersForDay);
                lastDayEngineers.clear();
                lastDayEngineers.addAll(engineersForDay);
            }
            currentDate = currentDate.plusDays(1);
        }

        return schedule;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private List<Engineer> initializeEngineers(int numEngineers) {
        List<Engineer> engineers = new ArrayList<>();
        for (int i = 0; i < numEngineers; i++) {
            engineers.add(new Engineer((long) i, "Engineer " + (i)));
        }
        return engineers;
    }

    private List<Engineer> selectEngineersForDay(Set<Engineer> availableEngineers,
                                                 Map<Engineer, Integer> shiftsCompleted,
                                                 Set<Engineer> lastDayEngineers,
                                                 int daysInSchedule,
                                                 int numEngineers) {
        final List<Engineer> eligibleEngineers = new ArrayList<>(availableEngineers);
        eligibleEngineers.removeAll(lastDayEngineers);
        // Adjust the shift limit based on what the total number of shifts and engineers
        int maxShiftsPerEngineer = (int) Math.ceil((double) daysInSchedule * 2 / numEngineers);

        eligibleEngineers.removeIf(e -> shiftsCompleted.getOrDefault(e, 0) >= maxShiftsPerEngineer);

        Collections.shuffle(eligibleEngineers);
        final List<Engineer> selectedEngineers = new ArrayList<>();

        for (int i = 0; i < 2 && !eligibleEngineers.isEmpty(); i++) {
            Engineer selected = eligibleEngineers.remove(0);
            selectedEngineers.add(selected);
            shiftsCompleted.put(selected, shiftsCompleted.getOrDefault(selected, 0) + 1);
        }

        return selectedEngineers;
    }
}
