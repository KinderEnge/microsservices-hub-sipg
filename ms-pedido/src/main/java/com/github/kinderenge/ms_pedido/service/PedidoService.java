package com.github.kinderenge.ms_pedido.service;

import com.github.kinderenge.ms_pedido.dto.ItemDoPedidoDTO;
import com.github.kinderenge.ms_pedido.dto.PedidoDTO;
import com.github.kinderenge.ms_pedido.dto.StatusDTO;
import com.github.kinderenge.ms_pedido.entities.ItemDoPedido;
import com.github.kinderenge.ms_pedido.entities.Pedido;
import com.github.kinderenge.ms_pedido.entities.Status;
import com.github.kinderenge.ms_pedido.repositories.ItemDoPedidoRepository;
import com.github.kinderenge.ms_pedido.repositories.PedidoRepository;
import com.github.kinderenge.ms_pedido.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemDoPedidoRepository itemDoPedidoRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos(){

        return pedidoRepository.findAll().stream().map(PedidoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO findById(Long id){
        Pedido entitty = pedidoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. Id: "+id)
        );
        return new PedidoDTO(entitty);
    }

    @Transactional
    public PedidoDTO savePedido(PedidoDTO dto){
        Pedido entity = new Pedido();
        entity.setData(LocalDate.now());
        entity.setStatus(Status.REALIZADO);
        copyDTOToEntity(dto, entity);
        entity.calcularTotalDoPedido();
        entity = pedidoRepository.save(entity);
        itemDoPedidoRepository.saveAll(entity.getItens());
        return new PedidoDTO(entity);
    }

    @Transactional
    public PedidoDTO updatePedido(Long id, PedidoDTO dto){
        try{
            Pedido entity = pedidoRepository.getReferenceById(id);
            entity.setData(LocalDate.now());
            entity.setStatus(Status.REALIZADO);
            itemDoPedidoRepository.deleteByPedidoId(id);
            copyDTOToEntity(dto, entity);
            entity.calcularTotalDoPedido();
            entity = pedidoRepository.save(entity);
            itemDoPedidoRepository.saveAll(entity.getItens());
            return  new PedidoDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado. Id: "+id);
        }
    }

    @Transactional
    public void deletePedido(Long id){
        if(!pedidoRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não enontrado. Id: "+id);
        }
        pedidoRepository.deleteById(id);
    }

    @Transactional
    public void aprovarPagamentoDoPedido(Long id){
        Pedido pedido = pedidoRepository.getPedidoByIdWithItens(id);
        if(pedido == null){
            throw new ResourceNotFoundException("Pedido id: "+id+" não encontrado.");
        }

        pedido.setStatus(Status.PAGO);
        pedidoRepository.updatePedido(Status.PAGO,pedido);
    }

    @Transactional
    public PedidoDTO upadatePagamentoDoPedido(Long id, StatusDTO statusDTO){
        Pedido pedido = pedidoRepository.getPedidoByIdWithItens(id);
        if(pedido == null){
            throw new ResourceNotFoundException("Pedido id: "+id+" não encontrado.");
        }

        pedido.setStatus(statusDTO.getStatus());
        pedidoRepository.updatePedido(statusDTO.getStatus(),pedido);
        return new PedidoDTO(pedido);
    }

    private void copyDTOToEntity(PedidoDTO dto, Pedido entity) {
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());

        List<ItemDoPedido> itens = new ArrayList<>();

        for(ItemDoPedidoDTO itemDTO : dto.getItens()){
            ItemDoPedido itemDoPedido = new ItemDoPedido();
            itemDoPedido.setQuantidade(itemDTO.getQuantidade());
            itemDoPedido.setDescricao(itemDTO.getDescricao());
            itemDoPedido.setValorUnitario(itemDTO.getValorUnitario());
            itemDoPedido.setPedido(entity);
            itens.add(itemDoPedido);
        }
        entity.setItens(itens);
    }
}
