package astal.projects.calculadorafinanciamento.model;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data // Lombok gera getters, setter e toString
public class SimulationRequest {
    @NotNull
    @Min(50000)
    private Double valorImovel;

    @NotNull
    @Min(5) @Max(70)
    private Double entradaPorcentagem;

    @NotNull
    @DecimalMin("4.0") @DecimalMax("20.0")
    private Double taxaAnual;

    @NotNull
    @Min(5) @Max(50)
    private Integer prazoAnos;

    @NotNull String sistema; // "SAC" ou "PRICE"
}
/***
 Ambos os sistemas são abreviações referente a parcelas,
 SAC: A característica principal é que a amortização é constante em todos os períodos: o valor amortizado
 em cada prestação é fixo.
 Os juros incidem sobre o saldo devedor, que diminui mais rapidamente.
 As prestações começam mais altas e diminuem ao longo do tempo.

 Consequências práticas:
 O saldo devedor cai mais rápido do que no PRICE.
 O total de juros pagos ao final do contrato é menor.
 Exige maior capacidade financeira no início, devido às prestações mais elevadas no começo.

 PRICE: No Price, a principal característica é que as prestações são iguais (fixas) ao longo do tempo do contrato.
 O valor da prestação permanece constante.
 No início do financiamento, a parcela de juros é alta e a amortização é baixa.
 Com o passar do tempo, os juros diminuem e a amortização aumenta.
 O saldo devedor diminui de forma mais lenta no começo.

 Consequências práticas:
 Facilita o planejamento financeiro do cliente, pois a prestação não muda.
 O total de juros pagos ao final do contrato tende a ser maior, comparado ao SAC, quando se trata da mesma taxa e prazo.
 ***/
