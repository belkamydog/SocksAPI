package com.api.socks.controller;

import com.api.socks.models.Socks;
import com.api.socks.service.RepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(SocksController.class)
public class SocksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RepositoryService shopService;

    @Test
    public void addSocksTest() throws Exception {
        Socks s = new Socks(Socks.SocksColors.white, 100, 2);
        MvcResult result = mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON).content(s.toString()))
                .andExpect(status().is(201))
                .andReturn();
        Assertions.assertEquals("successfully added", result.getResponse().getContentAsString());
    }

    @Test
    public void removeSocksTest() throws Exception {
        Socks income = new Socks(Socks.SocksColors.white, 100, 2);
        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON).content(income.toString()));
        Socks outcome = new Socks(Socks.SocksColors.white, 100, 1);
        MvcResult result = mockMvc.perform(post("/api/socks/outcome")
                 .contentType(MediaType.APPLICATION_JSON).content(outcome.toString()))
                .andExpect(status().is(200))
                .andReturn();
        income.setCount(1);
        Assertions.assertEquals("successfully extract", result.getResponse().getContentAsString());
    }

    @Test
    public void updateSocksTest() throws Exception {
        Socks last = new Socks(Socks.SocksColors.white, 100, 2);
        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON).content(last.toString()));
        Socks updated = new Socks(Socks.SocksColors.white, 99, 1);
        MvcResult result = mockMvc.perform(put("/api/socks/0")
                        .contentType(MediaType.APPLICATION_JSON).content(updated.toString()))
                .andExpect(status().is(200))
                .andReturn();
        last.setCotton(99);
        last.setCount(1);
        Assertions.assertEquals("successfully updated", result.getResponse().getContentAsString());
    }

    @Test
    public void getCountWhenEmptyTest() throws Exception{
        MvcResult result = mockMvc.perform(get("/api/socks")).andReturn();
        Assertions.assertEquals(0, Integer.parseInt(result.getResponse().getContentAsString()));
    }

    @Test
    public void getCountTest() throws Exception{
        Socks s = new Socks(Socks.SocksColors.white, 44, 2);
        Mockito.when(shopService.getSocksById(1)).thenReturn(s);
        mockMvc.perform(get("/api/socks"))
                .andExpect(status().is(200)).andReturn();
    }

    @Test
    public void testGetCountWithFilter() throws Exception{
        mockMvc.perform(get("/api/socks?cotton=gt:10&color=white"))
                .andExpect(status().is(200));
    }

}
