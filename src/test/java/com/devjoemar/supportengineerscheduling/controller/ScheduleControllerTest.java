package com.devjoemar.supportengineerscheduling.controller;

import com.devjoemar.supportengineerscheduling.model.Engineer;
import com.devjoemar.supportengineerscheduling.service.ScheduleService;
import com.devjoemar.supportengineerscheduling.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class ScheduleControllerTest {

    private static final int TOTAL_ENGINEERS = 10;
    private static final int NUMBER_OF_DAYS = 10;
    private static final String START_DATE = "2024-01-01";
    private static final String END_DATE = "2024-01-10";

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    public void whenValidPayload_thenReturnsSchedule() throws Exception {
        final String payload = String.format("{\"totalEngineers\":%d,\"numberOfDays\":%d}", TOTAL_ENGINEERS, NUMBER_OF_DAYS);
        Map<LocalDate, List<Engineer>> mockSchedule = createMockSchedule(NUMBER_OF_DAYS, TOTAL_ENGINEERS);

        when(scheduleService.generateSchedule(any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockSchedule);

        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Constant.RESULT_OK))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.schedules").isArray())
                .andExpect(jsonPath("$.data.schedules[0].date").isNotEmpty())
                .andExpect(jsonPath("$.data.schedules[0].engineers").isArray());
    }

    @Test
    public void whenPayloadMissingTotalEngineers_thenBadRequest() throws Exception {
        final String payload = "{\"numberOfDays\":10}";

        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Constant.RESULT_NOT_OK))
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.value()));
    }

    private Map<LocalDate, List<Engineer>> createMockSchedule(int days, int engineerCount) {
        final Map<LocalDate, List<Engineer>> schedule = new LinkedHashMap<>();
        final LocalDate startDate = LocalDate.parse(START_DATE);
        for (int day = 0; day < days; day++) {
            List<Engineer> engineers = new ArrayList<>();
            for (int engineerNumber = 0; engineerNumber < engineerCount; engineerNumber++) {
                engineers.add(new Engineer((long) engineerNumber, "Engineer " + engineerNumber));
            }
            schedule.put(startDate.plusDays(day), engineers);
        }
        return schedule;
    }
}
