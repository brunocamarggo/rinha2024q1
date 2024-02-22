package com.rinha2021q1.service;

import com.rinha2021q1.controller.model.request.CadastrarTransacaoRequest;
import com.rinha2021q1.controller.model.response.CadastrarTransacaoResponse;
import com.rinha2021q1.exception.ParametroInvalidoException;
import com.rinha2021q1.repository.RinhaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rinha2021q1.config.Contantes.*;

@Service
@Transactional
public class TransacaoService {

    private final RinhaRepository rinhaRepository;


    public TransacaoService(RinhaRepository rinhaRepository) {
        this.rinhaRepository = rinhaRepository;
    }

    public CadastrarTransacaoResponse cadastrar(Long id, CadastrarTransacaoRequest request) {
        return switch (request.tipo()) {
            case CREDITO -> credito(id, request);
            case DEBITO -> debito(id, request);
            default -> throw new ParametroInvalidoException();
        };
    }

    private CadastrarTransacaoResponse credito(Long id, CadastrarTransacaoRequest request) {
        return realizar(id, request, request.valor());

    }

    private CadastrarTransacaoResponse debito(Long id, CadastrarTransacaoRequest request) {
        var valorNegado = - request.valor();
        return realizar(id, request, valorNegado);
    }

    private CadastrarTransacaoResponse realizar(Long id, CadastrarTransacaoRequest request, Long valor) {
        var dados = rinhaRepository.atualizarSaldo(id, valor);
        rinhaRepository.salvarTransacao(id, request);
        return new CadastrarTransacaoResponse(
                (Number) dados.getKeyList().getFirst().get(LIMITE_KEY),
                (Number) dados.getKeyList().getFirst().get(SALDO_KEY));
    }
}
