package com.rinha2021q1.controller.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record ExtratoResponse (

        SaldoResponse saldo,

        @JsonProperty("ultimas_transacoes")
        List<TransacaoResponse> ultimasTransacoes

) {


    public record SaldoResponse(
            Number total,
            @JsonProperty("data_extrato")
            LocalDateTime dataExtrato,
            Number limite
    ) { }

    public record TransacaoResponse(
            Number valor,
            String tipo,
            String descricao,
            @JsonProperty("realizada_em")
            LocalDateTime realizadaEm
    ) { }
}
