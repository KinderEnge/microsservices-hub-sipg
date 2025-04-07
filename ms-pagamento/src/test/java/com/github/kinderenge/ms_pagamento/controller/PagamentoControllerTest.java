package com.github.kinderenge.ms_pagamento.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kinderenge.ms_pagamento.dto.PagamentoDTO;
import com.github.kinderenge.ms_pagamento.service.PagamentoService;
import com.github.kinderenge.ms_pagamento.service.exceptions.ResourceNotFoundException;
import com.github.kinderenge.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest
public class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagamentoService service;
    private PagamentoDTO dto;
    private Long existingId;
    private Long nonExistingId;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        nonExistingId = 100L;

        dto = Factory.createPagamentoDTO();

        List<PagamentoDTO> list = List.of(dto);

        Mockito.when(service.getAll()).thenReturn(list);

        Mockito.when(service.getById(existingId)).thenReturn(dto);

        Mockito.when(service.getById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.createPagamento(any())).thenReturn(dto);
    }

    @Test
    public void getAllShouldReturnListPagamentoDTO()throws Exception{
        ResultActions result = mockMvc.perform(get("/pagamentos").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdExists() throws Exception{
       ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void getByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist()throws Exception{
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void createPagamentoShouldReturnPagamentoDTOCreated()throws Exception{
        PagamentoDTO newPagamentoDTO = Factory.createNewPagamentoDTO();

        String jsonRequestBody = objectMapper.writeValueAsString(newPagamentoDTO);

        mockMvc.perform(post("/pagamentos").content(jsonRequestBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated()).andExpect(header().exists("Location")).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.valor").exists()).andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.pedidoId").exists()).andExpect(jsonPath("$.formaDePagamentoId").exists());

    }


}
