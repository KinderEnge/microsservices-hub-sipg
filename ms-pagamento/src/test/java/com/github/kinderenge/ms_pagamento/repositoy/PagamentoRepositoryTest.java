package com.github.kinderenge.ms_pagamento.repositoy;


import com.github.kinderenge.ms_pagamento.entity.Pagamento;
import com.github.kinderenge.ms_pagamento.repository.PagamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository repository;
    
    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        //Arrange
        Long existingId =1L;
        // Act
        repository.deleteById(existingId);
        //Assert
        Optional<Pagamento> result = repository.findById(existingId);
        //testa se existe um obj dentro do optional
        Assertions.assertFalse(result.isPresent());
    }

}
