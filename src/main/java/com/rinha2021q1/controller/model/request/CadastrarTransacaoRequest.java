package com.rinha2021q1.controller.model.request;

public record CadastrarTransacaoRequest(

        Long valor,
        Character tipo,
        String descricao
) {
}
