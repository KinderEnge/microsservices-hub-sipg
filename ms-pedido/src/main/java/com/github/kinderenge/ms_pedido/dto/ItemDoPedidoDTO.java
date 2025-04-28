package com.github.kinderenge.ms_pedido.dto;

import com.github.kinderenge.ms_pedido.entities.ItemDoPedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemDoPedidoDTO {
    private Long id;

    @NotNull(message = "Quantidade requerida")
    @Positive(message = "A quanidade deve ser um valor positivo")
    private Integer quantidade;
    @NotBlank(message = "Desrição é requerida")
    private String descricao;
    @NotNull(message = "Valor unitário requeirdo")
    @Positive(message = "Valor unitário deve ser um número positivo")
    private BigDecimal valorUnitario;

    public ItemDoPedidoDTO(ItemDoPedido entity) {
        id = entity.getId();
        quantidade = entity.getQuantidade();
        descricao = entity.getDescricao();
        valorUnitario = entity.getValorUnitario();
    }
}
