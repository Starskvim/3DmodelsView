package com.example.ModelView.controllers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
class ModelRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getStats() throws Exception {
        MvcResult result = mockMvc.perform(get("http://localhost:8189/3Dmodel/stats"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'message':'ok'}"))
                .andReturn();

        JSONObject jsonObject = new JSONObject(result.toString());
        System.out.println(jsonObject);
    }
}