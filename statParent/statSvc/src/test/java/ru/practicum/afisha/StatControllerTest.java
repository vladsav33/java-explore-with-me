package ru.practicum.afisha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.afisha.controllers.StatController;
import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatCountDto;
import ru.practicum.afisha.dto.StatDto;
import ru.practicum.afisha.services.StatServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StatControllerTest {
    @InjectMocks
    private StatController controller;
    @Mock
    private StatServiceImpl service;
    private StatDto statDto;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void initTest() {
        statDto = StatDto.builder().id(1).app("client").ip("192.168.0.2").timestamp(LocalDateTime.now()).uri("/events").build();
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    @SneakyThrows
    void createStat() {
        when(service.createStat(any(StatDto.class))).thenReturn(statDto);

        String response = mockMvc.perform(post("/hit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(statDto)))
                        .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        verify(service).createStat(any(StatDto.class));
        assertEquals(objectMapper.writeValueAsString(statDto), response);
    }

    @Test
    @SneakyThrows
    void getStat() {
        StatCountDto statCountDto = StatCountDto.builder().app("client").uri("events/1").hits(1).build();

        when(service.getStat(any(GetStatDto.class)))
                .thenReturn(List.of(statCountDto));

        String response = mockMvc.perform(get("/stats" + "?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00" +
                        "&uris=events/1&unique=false")
                        .contentType("application/json"))
                        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(service).getStat(any(GetStatDto.class));
        assertEquals(objectMapper.writeValueAsString(List.of(statCountDto)), response);
    }
}