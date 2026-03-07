package com.br.vidya.integration.receita.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReceitaResponse(
        String status,
        String cnpj,
        String tipo,
        String abertura,
        String nome,
        String fantasia,
        String porte,
        @JsonProperty("natureza_juridica") String naturezaJuridica,
        String situacao,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String municipio,
        String uf,
        String cep,
        String telefone,
        String email,
        @JsonProperty("capital_social") String capitalSocial,
        @JsonProperty("atividade_principal") List<Atividade> atividadePrincipal,
        @JsonProperty("atividades_secundarias") List<Atividade> atividadesSecundarias
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Atividade(String code, String text) {
    }
}
