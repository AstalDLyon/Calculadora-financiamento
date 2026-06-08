package astal.projects.calculadorafinanciamento.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationResult {
    private double valorFinanciado;
    private double primeiraParcela;
    private double ultimoParcela;
    private double totalPago;
    private double totalJuros;
    private double percentualJuros;
    private String sistema;
}
