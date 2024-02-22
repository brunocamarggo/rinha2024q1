package com.rinha2021q1.service;

import com.rinha2021q1.controller.model.response.ExtratoResponse;
import com.rinha2021q1.controller.model.response.ExtratoResponse.TransacaoResponse;
import com.rinha2021q1.repository.RinhaRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rinha2021q1.config.Contantes.*;

@Service
public class ExtratoService {

    private final RinhaRepository rinhaRepository;


    public ExtratoService(RinhaRepository rinhaRepository) {
        this.rinhaRepository = rinhaRepository;
    }

    public ExtratoResponse consultarExtrato(Long id) {
        var extrato = rinhaRepository.getExtrato(id);
        var saldo = (Number) extrato.getFirst().get(SALDO_KEY);
        var limite = (Number) extrato.getFirst().get(LIMITE_KEY);

        List<TransacaoResponse> transacoes = new ArrayList<>();
        if (extrato.getFirst().get(VALOR_KEY) != null) {
            transacoes = extrato
                    .stream()
                    .map(ExtratoService::map)
                    .toList();
        }
        var saldoResponse = new ExtratoResponse.SaldoResponse(saldo, LocalDateTime.now(), limite);
        return new ExtratoResponse(saldoResponse, transacoes);
    }

    private static TransacaoResponse map(Map<String, Object> transacao) {
        return new TransacaoResponse(
                (Number) transacao.get(VALOR_KEY),
                transacao.get(TIPO_KEY).toString(),
                transacao.get(DESCRICAO_KEY).toString(),
                ((Timestamp) transacao.get(REALIZADA_EM_KEY)).toLocalDateTime());
    }
}
