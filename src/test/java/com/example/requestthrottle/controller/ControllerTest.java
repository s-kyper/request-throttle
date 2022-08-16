package com.example.requestthrottle.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Test
    void request() {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        String ip1 = "192.168.0.2";
        String ip2 = "205.51.25.23";

        IntStream.range(0, 10).parallel().forEach(index -> {
            executeRequest(mockMvc, ip1, status().isOk());
            executeRequest(mockMvc, ip2, status().isOk());
        });

        executeRequest(mockMvc, ip1, status().isBadGateway());
        executeRequest(mockMvc, ip2, status().isBadGateway());
    }

    private void executeRequest(MockMvc mockMvc, String ipAddress, ResultMatcher expectedResultMatcher) {
        try {
            mockMvc.perform(post("/")
                            .with(request -> {
                                request.setRemoteAddr(ipAddress);
                                return request;
                            }))
                    .andExpect(expectedResultMatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}