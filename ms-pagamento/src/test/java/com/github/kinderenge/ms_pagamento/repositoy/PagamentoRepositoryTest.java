package com.github.kinderenge.ms_pagamento.repositoy;


import com.github.kinderenge.ms_pagamento.entity.Pagamento;
import com.github.kinderenge.ms_pagamento.repository.PagamentoRepository;
import com.github.kinderenge.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository repository;

    //declarando variáveis
    //declarando as variáveis
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalPagamento;

    @BeforeEach
    void setup()throws Exception{
        //Arrange
        existingId = 1L;
        nonExistingId = 100L;
        //verificar quantos registros tem no seed do DB
        countTotalPagamento =   3L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        // Act
        repository.deleteById(existingId);
        //Assert
        Optional<Pagamento> result = repository.findById(existingId);
        //testa se existe um obj dentro do optional
        //verifica se o objeto não está presnte no repositório.
        //se result.isPresent() for false,
        //significa que o objeto foi deletado com sucesso.
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    //Fornece uma descrição legível do teste, que será exibida no relatório de teste
    @DisplayName("Dado parâmetros válidos e Id nulo quando chamar Criar Pagamento então deve instanciar um Pagamento.")
    public void givenValidParamsAndIsNull_whenCallCreatePagamento_thenInstantiateAPagaemnto(){
        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);
        pagamento = repository.save(pagamento);
        Assertions.assertNotNull(pagamento.getId());
        Assertions.assertEquals(countTotalPagamento + 1, pagamento.getId());
    }

    @Test
    @DisplayName("given an existing ID when calling findById then it should return a non empty Optional")
    public void givenAExistingId_whenCallFindById_thenRetutrnNonEmptyOptional(){
        Optional<Pagamento>result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("given a non-existing ID when calling findById then it should return an empty Optional")
    public void givenANonExistingId_whenCallFindById_thenRetutrnAnEmptyOptional(){
        Optional<Pagamento>result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
        //ou
        Assertions.assertTrue(result.isEmpty());
    }

}
