package com.rinha2021q1.repository;

import com.rinha2021q1.controller.model.request.CadastrarTransacaoRequest;
import com.rinha2021q1.exception.ClienteNotFoundException;
import com.rinha2021q1.exception.SalvoInsuficienteException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class RinhaRepository {

    private final JdbcClient jdbcClient;

    public RinhaRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Map<String, Object>> getDadosCliente(Long id) {
        var query =
                """
                SELECT limite, saldo
                FROM clientes
                WHERE
                     id=:id
                """;
        var result = jdbcClient.sql(query)
                .params(Map.of("id", id))
                .query();

        if (result.listOfRows().isEmpty()) {
            throw new ClienteNotFoundException();
        }

        return result.listOfRows();
    }

    public void salvarTransacao(Long id, CadastrarTransacaoRequest request) {
        var query =
                """
                INSERT INTO transacoes
                    (cliente_id, valor, tipo, descricao, realizada_em )
                VALUES
                    (:clienteId, :valor, :tipo, :descricao, :realizada_em)
                """;

       jdbcClient.sql(query)
                .params(Map.of("clienteId", id,
                        "valor", request.valor(),
                        "tipo", request.tipo(),
                        "descricao", request.descricao(),
                        "realizada_em", LocalDateTime.now()))
                .update();
    }

    public GeneratedKeyHolder atualizarSaldo(Long id, Long valor) {
        var generatedKeyHolder = new GeneratedKeyHolder();
        var query =
               """
               UPDATE clientes
               SET saldo = saldo + :valor
               WHERE
                    id=:id AND :valor + limite + saldo >= 0
               """;

        var result = jdbcClient.sql(query)
                .params(Map.of("id", id, "valor", valor))
                .update(generatedKeyHolder, "saldo", "limite");

        if (result < 1) {
            throw new SalvoInsuficienteException();
        }

        return generatedKeyHolder;
    }

    public List<Map<String, Object>> getExtrato(Long id) {

        var query =
            """
            
            SELECT
            *
            FROM clientes c
               LEFT JOIN transacoes t ON t.cliente_id = c.id
            WHERE
                c.id = :clienteId
            ORDER BY realizada_em DESC
            LIMIT 10
            """;

        return jdbcClient.sql(query)
                .params(Map.of("clienteId", id))
                .query()
                .listOfRows();
    }
}
