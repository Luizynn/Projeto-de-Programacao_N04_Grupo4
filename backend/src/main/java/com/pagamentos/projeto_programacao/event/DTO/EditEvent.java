package com.pagamentos.projeto_programacao.event.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EditEvent(Long id_event, Long id_localization, String name, LocalDateTime dt_start, LocalDateTime dt_end,
                        BigDecimal price) {
}
