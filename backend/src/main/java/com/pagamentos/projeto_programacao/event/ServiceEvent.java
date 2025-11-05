package com.pagamentos.projeto_programacao.event;

import com.pagamentos.projeto_programacao.event.DTO.CreateEvent;
import com.pagamentos.projeto_programacao.event.DTO.EditEvent;
import com.pagamentos.projeto_programacao.event.DTO.ResponseEvent;
import com.pagamentos.projeto_programacao.event.exeption.ExceptionExpiredTimeForEdit;
import com.pagamentos.projeto_programacao.localization.Localization;
import com.pagamentos.projeto_programacao.localization.RepositoryLocalization;
import com.pagamentos.projeto_programacao.users.RepositoryUser;
import com.pagamentos.projeto_programacao.users.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceEvent {
    @Autowired
    private RepositoryEvent repository;

    @Autowired
    RepositoryLocalization repositoryLocalization;

    @Autowired
    RepositoryUser repositoryUser;

    @Transactional
    public ResponseEvent create (CreateEvent body) {
        Event event = new Event();
        event.setDtEnd(body.dt_end());
        event.setName(body.name());
        event.setDtStart(body.dt_start());
        event.setPrice(body.price());

        Localization localization = repositoryLocalization.findById(body.id_localization()).orElseThrow(() -> new EntityNotFoundException("Localização não existe"));

        event.setLocalization(localization);

        User organizer = repositoryUser.findById(body.id_organizer()).orElseThrow(()-> new EntityNotFoundException("Usuario não existe"));

        event.setOrganizer(organizer);

        return new ResponseEvent(repository.save(event));

    }

    @Transactional
    public ResponseEvent edit (EditEvent body) {
        if(body.dt_start().isBefore(LocalDateTime.now())) {
            throw new ExceptionExpiredTimeForEdit("O evento não pode ser iniciado quando o dia de inicio já passou");
        };

        Event event = repository.findById(body.id_event()).orElseThrow(() -> new EntityNotFoundException("Evento não exist"));

        Localization localization = repositoryLocalization.findById(body.id_localization()).orElseThrow(() -> new EntityNotFoundException("Localização não existe"));

        event.setName(body.name());
        event.setDtStart(body.dt_start());
        event.setDtEnd(body.dt_end());
        event.setPrice(body.price());
        event.setLocalization(localization);

        return new ResponseEvent(repository.save(event));
    }

    @Transactional(readOnly = true)
    public List<ResponseEvent> findAll () {
        return repository.findAll().stream().map(evt -> new ResponseEvent(evt)).toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseEvent> findByOrganizer (Long idOrganizer) {
        return repository.findByOrganizerId(idOrganizer).stream().map(evt -> new ResponseEvent(evt)).toList();
    }
}
