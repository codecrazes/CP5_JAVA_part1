package br.com.fiap.supermarket.controller;

import br.com.fiap.supermarket.entity.Produto;
import br.com.fiap.supermarket.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/produto-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto) {
        if (produto.getId() != null) {
            produtoService.atualizar(produto.getId(), produto);
        } else {
            produtoService.cadastrar(produto);
        }
        return "redirect:/produtos/listar";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "produto/produto-listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoService.buscarPorId(id).orElseThrow());
        return "produto/produto-form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return "redirect:/produtos/listar";
    }
}
