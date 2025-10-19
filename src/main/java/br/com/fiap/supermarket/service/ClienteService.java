package br.com.fiap.supermarket.service;

import br.com.fiap.supermarket.entity.Cliente;
import br.com.fiap.supermarket.repository.ClienteRepository;
import br.com.fiap.supermarket.service.tcp.RSAUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            descriptografar(cliente);
        }
        return clientes;
    }

    public Optional<Cliente> buscarPorId(Long id) {
        Optional<Cliente> opt = clienteRepository.findById(id);
        opt.ifPresent(this::descriptografar);
        return opt;
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("Cliente com este CPF já está cadastrado.");
        }
        criptografar(cliente);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    if (clienteAtualizado.getCpf() != null && !clienteAtualizado.getCpf().equals(cliente.getCpf())
                            && clienteRepository.existsByCpf(clienteAtualizado.getCpf())) {
                        throw new RuntimeException("Já existe um cliente com este CPF.");
                    }

                    if (clienteAtualizado.getNome() != null) cliente.setNome(clienteAtualizado.getNome());
                    if (clienteAtualizado.getTelefone() != null) cliente.setTelefone(clienteAtualizado.getTelefone());
                    if (clienteAtualizado.getEndereco() != null) cliente.setEndereco(clienteAtualizado.getEndereco());
                    if (clienteAtualizado.getCpf() != null) cliente.setCpf(clienteAtualizado.getCpf());

                    criptografar(cliente);
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    @Transactional
    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }

    private void criptografar(Cliente cliente) {
        try {
            List<Integer> cpfCript = RSAUtil.encrypt(cliente.getCpf(), RSAUtil.getPublicE(), RSAUtil.getPublicN());
            List<Integer> enderecoCript = RSAUtil.encrypt(cliente.getEndereco(), RSAUtil.getPublicE(), RSAUtil.getPublicN());

            cliente.setCpf(Base64.getEncoder().encodeToString(intListToBytes(cpfCript)));
            cliente.setEndereco(Base64.getEncoder().encodeToString(intListToBytes(enderecoCript)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criptografar dados do cliente");
        }
    }

    private void descriptografar(Cliente cliente) {
        try {
            byte[] cpfBytes = Base64.getDecoder().decode(cliente.getCpf());
            byte[] enderecoBytes = Base64.getDecoder().decode(cliente.getEndereco());

            List<Integer> cpfCript = bytesToIntList(cpfBytes);
            List<Integer> enderecoCript = bytesToIntList(enderecoBytes);

            cliente.setCpf(RSAUtil.decrypt(cpfCript, RSAUtil.getPrivateD(), RSAUtil.getPrivateN()));
            cliente.setEndereco(RSAUtil.decrypt(enderecoCript, RSAUtil.getPrivateD(), RSAUtil.getPrivateN()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao descriptografar dados do cliente");
        }
    }

    private byte[] intListToBytes(List<Integer> list) {
        byte[] bytes = new byte[list.size() * 2];
        for (int i = 0; i < list.size(); i++) {
            int val = list.get(i);
            bytes[i*2]     = (byte) ((val >> 8) & 0xFF); 
            bytes[i*2 + 1] = (byte) (val & 0xFF);  
        }
        return bytes;
    }

    private List<Integer> bytesToIntList(byte[] bytes) {
        List<Integer> list = new java.util.ArrayList<>();
        for (int i = 0; i < bytes.length; i += 2) {
            int hi = (bytes[i] & 0xFF);
            int lo = (bytes[i+1] & 0xFF);
            list.add((hi << 8) | lo);
        }
        return list;
    }
}
