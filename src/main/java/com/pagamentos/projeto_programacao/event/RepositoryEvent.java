package com.pagamentos.projeto_programacao.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryEvent extends JpaRepository<Event,Long> {
    List<Event> findByOrganizerId(Long organizerId);
}
