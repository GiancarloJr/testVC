package com.br.vidya.integration.receita.service;

import com.br.vidya.integration.receita.dto.ReceitaResponse;
import com.br.vidya.integration.receita.gateway.ReceitaGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaServiceTest {

    @Mock
    private ReceitaGateway receitaGateway;

    @InjectMocks
    private ReceitaService receitaService;

    @Test
    void findByCnpj_shouldReturnReceitaResponse_whenCnpjIsFormatted() {
        ReceitaResponse expected = mock(ReceitaResponse.class);
        when(receitaGateway.get(ReceitaResponse.class, "Receita API Error", "12345678000195"))
                .thenReturn(expected);

        ReceitaResponse result = receitaService.findByCnpj("12.345.678/0001-95");

        assertThat(result).isEqualTo(expected);
        verify(receitaGateway).get(ReceitaResponse.class, "Receita API Error", "12345678000195");
    }

    @Test
    void findByCnpj_shouldSanitizeCnpj_beforeCallingGateway() {
        ReceitaResponse expected = mock(ReceitaResponse.class);
        when(receitaGateway.get(ReceitaResponse.class, "Receita API Error", "12345678000195"))
                .thenReturn(expected);

        receitaService.findByCnpj("12345678000195");

        verify(receitaGateway).get(ReceitaResponse.class, "Receita API Error", "12345678000195");
    }
}
