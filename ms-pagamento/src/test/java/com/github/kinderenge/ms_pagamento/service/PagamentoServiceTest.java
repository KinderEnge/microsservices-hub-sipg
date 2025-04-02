package com.github.kinderenge.ms_pagamento.service;


import com.github.kinderenge.ms_pagamento.dto.PagamentoDTO;
import com.github.kinderenge.ms_pagamento.entity.Pagamento;
import com.github.kinderenge.ms_pagamento.repository.PagamentoRepository;
import com.github.kinderenge.ms_pagamento.service.exceptions.ResourceNotFoundException;
import com.github.kinderenge.ms_pagamento.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTest {

    //Referenciando PagamentoService
    //@Autowired - sem injeção de dependência
    // Mock - injetado
    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    private long existingId;
    private long nonExistingId;

    private Pagamento pagamento;
    private PagamentoDTO dto;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        nonExistingId = 10L;

        //precisa simular o comportamento do object mockado
        //delete - quando id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        //delete - quando id não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        //delete - primeiro caso - deleta
        //não faça nada (void) quando . . .
        Mockito.doNothing().when(repository).deleteById(existingId);
        //próximos testes
        pagamento = Factory.createPagamento();
        dto = new PagamentoDTO(pagamento);
        //simulação dos comportamentos
        //getById (findById)
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pagamento));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        //cretaePagamento (insert)
        Mockito.when(repository.save(any())).thenReturn(pagamento);
        //updatePagamento (update) - primeiro caso - id existe
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(pagamento);
        //updatePagaemnto (updaye) - segundo caso - id não existe
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("delete Should do nothing when Id exists")
    public void deleteShouldDoNothingWhenIdExists(){
        //em PagamentoService, o método delete é do tipo void
        Assertions.assertDoesNotThrow(()->{
            service.deletePagamento(existingId);
        });

    }

    @Test
    @DisplayName("delete Should throw ResourceNotFoundException when Id doesn't exist")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        //em PagamentoService, o método delete é do tipo void
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.deletePagamento(nonExistingId);
        });
    }

    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdExists(){
        dto = service.getById(existingId);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    public void getByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.getById(nonExistingId);
        });
    }

    @Test
    public void createPagamentoShouldReturnPagamentoDTOWhenPagamentoIsCreated(){
        dto = service.createPagamento(dto);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), pagamento.getId());
    }

    @Test
    public void updatePagamentoShouldReturnPagamentoDTOWhenIdExists(){
        dto = service.updatePagamento(pagamento.getId(), dto);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    public void updatePagamentoShoulThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            service.updatePagamento(nonExistingId, dto);
        });
    }

}
