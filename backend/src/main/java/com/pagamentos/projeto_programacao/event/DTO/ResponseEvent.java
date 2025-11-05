package com.pagamentos.projeto_programacao.event.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseEvent(
        Long id,
        String name,
        LocalDateTime dtStart,
        LocalDateTime dtEnd,
        BigDecimal price,
        Long idOrganizer,
        String nameOrganizer,
        String localizationAddress,
        String localizationNeighborhood
) {
    public ResponseEvent (com.pagamentos.projeto_programacao.event.Event event) {
        this (
                event.getId(),
                event.getName(),
                event.getDtStart(),
                event.getDtEnd(),
                event.getPrice(),
                event.getOrganizer().getId(), // Pega só o ID do proxy (funciona)
                event.getOrganizer().getName(), // Pega o nome (força o proxy a carregar)
                event.getLocalization().getAddress(),
                event.getLocalization().getNeighborhood()
        );
    }
}
