package com.github.kinderenge.ms_pedido.dto;

import com.github.kinderenge.ms_pedido.entities.ItemDoPedido;
import com.github.kinderenge.ms_pedido.entities.Pedido;
import com.github.kinderenge.ms_pedido.entities.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long id;
    @NotEmpty(message = "Nome requerido")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;
    @NotBlank(message = "CPF rquerido")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 caracteres")
    private String cpf;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private StatusDTO status;
    private BigDecimal valorTotal;
    @NotEmpty(message = "Pedido deve ter pelo meons um item de pedido")
    private List<@Valid ItemDoPedidoDTO> itens = new ArrayList<>();


    public PedidoDTO(Pedido entity){
        id = entity.getId();
        nome = entity.getNome();
        cpf = entity.getCpf();
        data = entity.getData();
        status = new StatusDTO(entity.getStatus());
        valorTotal = entity.getValorTotal();

        for(ItemDoPedido item: entity.getItens()){
            ItemDoPedidoDTO itemDTO = new ItemDoPedidoDTO(item);
            itens.add(itemDTO);
        }
    }

}
