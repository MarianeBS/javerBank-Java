package com.msouza.client.service;

import com.msouza.client.entity.Cliente;
import com.msouza.client.exception.EntityNotFoundException;
import com.msouza.client.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        clienteRepository.save(cliente);
        return cliente;
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente id=%s não encontrado no sistema", id))
        );
    }

    @Transactional
    public Cliente atualizar(Cliente cliente, Long id) {
        // Buscar o cliente existente no banco
        Cliente clienteExistente = buscarPorId(id);

        // Atualizar o nome, se fornecido e não for vazio ou em branco
        if (cliente.getNome() != null && !cliente.getNome().isBlank()) {
            clienteExistente.setNome(cliente.getNome());
        }

        // Atualizar o telefone, se fornecido e não for nulo
        if (cliente.getTelefone() != null) {
            clienteExistente.setTelefone(cliente.getTelefone());
        }

        if (cliente.getUltimoDeposito() != null) {
            clienteExistente.setUltimoDeposito(cliente.getUltimoDeposito());
        }

        clienteExistente.setSaldoCc(cliente.getSaldoCc());

        // O cliente é automaticamente salvo devido à anotação @Transactional
        return clienteExistente;
    }

    @Transactional
    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }


}