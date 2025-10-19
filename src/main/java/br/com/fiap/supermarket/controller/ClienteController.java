package br.com.fiap.supermarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import br.com.fiap.supermarket.entity.Cliente;
import br.com.fiap.supermarket.service.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/cliente-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Cliente cliente) {
        if (cliente.getId() != null) {
            clienteService.atualizar(cliente.getId(), cliente);
        } else {
            clienteService.cadastrar(cliente);
        }
        return "redirect:/clientes/listar";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "cliente/cliente-listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id).orElseThrow());
        return "cliente/cliente-form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return "redirect:/clientes/listar";
    }
}
