package com.rinha2021q1.controller;

import com.rinha2021q1.controller.model.request.CadastrarTransacaoRequest;
import com.rinha2021q1.controller.model.response.CadastrarTransacaoResponse;
import com.rinha2021q1.controller.model.response.ExtratoResponse;
import com.rinha2021q1.exception.ClienteNotFoundException;
import com.rinha2021q1.exception.ParametroInvalidoException;
import com.rinha2021q1.service.ExtratoService;
import com.rinha2021q1.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rinha2021q1.config.Contantes.CREDITO;
import static com.rinha2021q1.config.Contantes.DEBITO;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final TransacaoService transacaoService;
    private final ExtratoService extratoService;

    public ClienteController(TransacaoService transacaoService, ExtratoService extratoService) {
        this.transacaoService = transacaoService;
        this.extratoService = extratoService;
    }

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<CadastrarTransacaoResponse> transacao(@PathVariable Long id,
                                                                         @RequestBody CadastrarTransacaoRequest request) {
        validarRequest(id, request);
        var cadastrar = transacaoService.cadastrar(id, request);
        return ResponseEntity.ok(cadastrar);
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<ExtratoResponse> extrato(@PathVariable Long id) {
        validarRequest(id);
        return ResponseEntity.ok(extratoService.consultarExtrato(id));

    }

    private static void validarRequest(Long id, CadastrarTransacaoRequest request) {
        if (request.valor() < 0) {
            throw new ParametroInvalidoException();
        }

        validarRequest(id);

        if (request.tipo() != CREDITO && request.tipo() != DEBITO) {
            throw new ParametroInvalidoException();
        }

        if (request.descricao() == null || request.descricao().isEmpty() || request.descricao().length() > 10) {
            throw new ParametroInvalidoException();
        }
    }

    private static void validarRequest(Long id) {
        if(id < 0 || id > 5) {
            throw new ClienteNotFoundException();
        }
    }

}
