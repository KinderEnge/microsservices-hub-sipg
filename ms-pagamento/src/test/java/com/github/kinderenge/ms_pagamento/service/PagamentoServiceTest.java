package com.github.kinderenge.ms_pagamento.service;


import com.github.kinderenge.ms_pagamento.repository.PagamentoRepository;
import com.github.kinderenge.ms_pagamento.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

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

    
}
