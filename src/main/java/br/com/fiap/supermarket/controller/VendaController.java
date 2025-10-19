package br.com.fiap.supermarket.controller;

import br.com.fiap.supermarket.entity.Venda;
import br.com.fiap.supermarket.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        model.addAttribute("venda", new Venda());
        return "venda/venda-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Venda venda) {
        if (venda.getId() != null) {
            vendaService.atualizarParcial(venda.getId(), venda.getCliente().getId(), venda.getValorTotal());
        } else {
            vendaService.registrarVenda(venda.getCliente().getId(), venda.getValorTotal());
        }
        return "redirect:/vendas/listar";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("vendas", vendaService.listarTodas());
        return "venda/venda-listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("venda", vendaService.buscarPorId(id).orElseThrow());
        return "venda/venda-form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        vendaService.excluir(id);
        return "redirect:/vendas/listar";
    }
}
