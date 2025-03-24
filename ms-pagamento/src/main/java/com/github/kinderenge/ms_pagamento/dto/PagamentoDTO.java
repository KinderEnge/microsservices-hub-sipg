package com.github.kinderenge.ms_pagamento.dto;


import com.github.kinderenge.ms_pagamento.entity.Pagamento;
import com.github.kinderenge.ms_pagamento.entity.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PagamentoDTO {

    private Long id;
    @NotNull(message = "Campo requerido")
    @Positive(message = "O valor do pagamento dever ser um número positivo")
    private BigDecimal valor;
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;
    @Size(min = 16, max = 19, message = "O número deve ter entre 16 e 19")
    private String numeroDoCarto;
    @Size(min = 5, max = 5, message = "Validade deve ter 5 caracteres")
    private String validade;
    @Size(min = 3, max = 3, message = "Código de segurança deve ter 3 caracteres")
    private String codigoDeSeguranaca;

    @Enumerated(EnumType.STRING)
    private Status status;
    @NotNull(message = "Pedido Id é obrigatório")
    private Long pedidoId;
    @NotNull(message = "Forma de pagamento Id é obrigatório")
    private Long formaDePagamentoId;

    public PagamentoDTO(Pagamento entity) {
        id = entity.getId();
        valor = entity.getValor();
        nome = entity.getNome();
        numeroDoCarto = entity.getNumeroDoCartao();
        validade = entity.getValidade();
        codigoDeSeguranaca = entity.getCodigoDeSeguranca();
        status = entity.getStatus();
        pedidoId = entity.getPedidoId();
        formaDePagamentoId = entity.getFormaDePagamentoId();
    }
}
