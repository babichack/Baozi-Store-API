package com.baozistore.api.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record PedidoRequest(
        LocalDateTime dataPedido,
        String status,
        @NotNull Long clienteId,
        Set<Long> produtoIds
) {
}
