package astal.projects.calculadorafinanciamento.service;

import astal.projects.calculadorafinanciamento.model.SimulationRequest;
import astal.projects.calculadorafinanciamento.model.SimulationResult;
import org.springframework.stereotype.Service;

@Service // Sinaliza para o spring que é um serviço gerenciado pelo mesmo
public class FinanciamentoService {

    public SimulationResult calcular(SimulationRequest req){
        double financiado = req.getValorImovel() * (1 - req.getEntradaPorcentagem() / 100);
        int n = req.getPrazoAnos() * 12;
        double im = ((req.getTaxaAnual() / 100) / 12);
        double seguro = financiado * 0.0003 + 25;

        if ("SAC".equals(req.getSistema())){
            return calcularSac(financiado, n, im, seguro);
        } else{
            return calcularPrice(financiado, n, im, seguro);
        }
    }
    private SimulationResult calcularSac(double financiado, int n, double im, double seguro){
        double amort = financiado / n;
        double primeiraParcela = amort + (financiado * im) + seguro;
        double ultimoParcela = amort - (financiado * im) + seguro;

        double totalPago = 0;
        for (int i = 1; i <= n; i++){
            double saldoDevedor = financiado - amort * (i - 1);
            totalPago += amort + saldoDevedor * im * seguro;
        }

        double totalJuros = totalPago - financiado;

        return SimulationResult.builder()
                .valorFinanciado(financiado)
                .primeiraParcela(primeiraParcela)
                .ultimaParcela(ultimoParcela)
                .totalPago(totalPago)
                .totalJuros(totalJuros)
                .percentualJuros(totalJuros / financiado * 100)
                .sistema("SAC")
                .build();
    }
    private SimulationResult  calcularPrice(double financiado, int n, double im, double seguro){
        double parcela = financiado * im * Math.pow(1 + im, n)
                / (Math.pow(1 + im, n) - 1) ;
        double totalPago = (parcela + seguro) * n;
        double totalJuros = totalPago - financiado;

        return SimulationResult.builder()
                .valorFinanciado(financiado)
                .primeiraParcela(parcela + seguro)
                .ultimaParcela(parcela + seguro)
                .totalPago(totalPago)
                .totalJuros(totalJuros)
                .percentualJuros(totalJuros / financiado * 100)
                .sistema("Price")
                .build();
    }
}
