package com.github.kinderenge.ms_pagamento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kinderenge.ms_pagamento.dto.PagamentoDTO;
import com.github.kinderenge.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PagamentoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private Long existingId;
    private Long nonExistingId;
    private PagamentoDTO pagamentoDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 50L;
        pagamentoDTO = Factory.createPagamentoDTO();
    }

    @Test
    public void getAllShouldReturnListAllPagamentos() throws Exception {

        mockMvc.perform(get("/pagamentos").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("[0].id").value(1L)).andExpect(jsonPath("[0].nome").isString()).andExpect(jsonPath("[0].nome").value("Amadeus Mozart")).andExpect(jsonPath("[5].status").value("CONFIRMADO"));
    }

    @Test
    public void getByIdShouldReturnPagaemntoDTOWhenIdExists() throws Exception {
        mockMvc.perform(get("/pagamentos/{id}", existingId).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(jsonPath("id").value(1)).andExpect(jsonPath("nome").isString()).andExpect(jsonPath("nome").value("Amadeus Mozart")).andExpect(jsonPath("status").value("CRIADO"));
    }

    @Test
    public void getByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception{
        mockMvc.perform(get("/pagamentos/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createShouldReturnPagamentoDTO() throws Exception{
        pagamentoDTO = Factory.createNewPagamentoDTO();

        String jsonRequestBody = objectMapper.writeValueAsString(pagamentoDTO);

        mockMvc.perform(post("/pagamentos").content(jsonRequestBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated()).andExpect(header().exists("Location")).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.valor").exists()).andExpect(jsonPath("$.nome").value(pagamentoDTO.getNome())).andExpect(jsonPath("$.status").value("CRIADO"));
    }

    @Test
    public void createShouldPersistPagamentoWithRequiredFields()throws Exception{
        pagamentoDTO = Factory.createNewPagamentoDTOWithRequiredFiels();
        String jsonRequestBody = objectMapper.writeValueAsString(pagamentoDTO);
        mockMvc.perform(post("/pagamentos").content(jsonRequestBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated()).andExpect(header().exists("Location")).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.valor").exists()).andExpect(jsonPath("$.valor").value(pagamentoDTO.getValor())).andExpect(jsonPath("$.status").value("CRIADO")).andExpect(jsonPath("$.nome").isEmpty()).andExpect(jsonPath("$.validade").isEmpty());
    }

    @Test
    @DisplayName("create deve lançar Exception quando dados inválidos")
    public void createShouldThrowExceptionWhenInvalidData() throws Exception{
        pagamentoDTO = Factory.createNewPagamentoDTOWithInvalidData();
        String jsonRequestBody = objectMapper.writeValueAsString(pagamentoDTO);
        mockMvc.perform(post("/pagamentos").content(jsonRequestBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateShouldUpdateAndReturnPagamentoDTOWhenIdExists() throws Exception{
        String jsonRequestBody = objectMapper.writeValueAsString(pagamentoDTO);
        mockMvc.perform(put("/pagamentos/{id}", existingId).content(jsonRequestBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.valor").exists()).andExpect(jsonPath("$.valor").value(pagamentoDTO.getValor())).andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.status").value("CRIADO")).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist()throws Exception{
        String jsonRequestBody = objectMapper.writeValueAsString(pagamentoDTO);
        mockMvc.perform(put("/pagamentos/{id}", nonExistingId).content(jsonRequestBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldThrowExceptionWhenInvalidData() throws Exception{
        pagamentoDTO = Factory.createNewPagamentoDTOWithInvalidData();
        String jsonRequestBody = objectMapper.writeValueAsString(pagamentoDTO);
        mockMvc.perform(post("/pagamentos/{id}", existingId).content(jsonRequestBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteSHouldReturnNoCOntentWhenIdExists() throws Exception{
        mockMvc.perform(delete("/pagamentos/{id}", existingId).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWHenIdDoesNotExist() throws Exception{
        mockMvc.perform(delete("/pagamento/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }

}
