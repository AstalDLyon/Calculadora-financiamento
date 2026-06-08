package astal.projects.calculadorafinanciamento.controller;

import astal.projects.calculadorafinanciamento.model.SimulationRequest;
import astal.projects.calculadorafinanciamento.model.SimulationResult;
import astal.projects.calculadorafinanciamento.service.FinanciamentoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FinanciamentoController {

    private final FinanciamentoService service;

    public FinanciamentoController(FinanciamentoService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("req", new SimulationRequest());
        return "index";
    }

    @PostMapping("/")
    public String calcular(
            @Valid @ModelAttribute("req") SimulationRequest req,
            BindingResult erros,
            Model model) {
        if (erros.hasErrors()) {
            return "index";
        }
        SimulationResult result = service.calcular(req);
        model.addAttribute("result", result);
        return "index";
    }
}
