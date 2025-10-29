package com.pagamentos.projeto_programacao.event;

import com.pagamentos.projeto_programacao.event.DTO.CreateEvent;
import com.pagamentos.projeto_programacao.event.DTO.EditEvent;
import com.pagamentos.projeto_programacao.event.DTO.ResponseEvent;
import com.pagamentos.projeto_programacao.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class ControllerEvent {

    @Autowired
    private ServiceEvent serviceEvent;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<ResponseEvent>>> create(@RequestBody CreateEvent body) {

        Response<List<ResponseEvent>> response = new Response<List<ResponseEvent>>(HttpStatus.CREATED);

        serviceEvent.create(body);

        List<ResponseEvent> list = serviceEvent.findByOrganizer(body.id_organizer());

        response.setData(list);

        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<ResponseEvent>>> edit (@RequestBody EditEvent body) {
        Response<List<ResponseEvent>> response = new Response<List<ResponseEvent>>(HttpStatus.OK);

        ResponseEvent evt = serviceEvent.edit(body);

        List<ResponseEvent> list = serviceEvent.findByOrganizer(evt.idOrganizer());

        response.setData(list);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping()
    public ResponseEntity<Response<List<ResponseEvent>>> listAll () {
        Response<List<ResponseEvent>> response = new Response<List<ResponseEvent>>(HttpStatus.OK);

        List<ResponseEvent> list = serviceEvent.findAll();

        response.setData(list);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = "/{id_evt}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<ResponseEvent>>> listByOrganizer (@PathVariable(value = "id_evt") Long id_evt) {
        Response<List<ResponseEvent>> response = new Response<List<ResponseEvent>>(HttpStatus.OK);

        List<ResponseEvent> list = serviceEvent.findByOrganizer(id_evt);

        response.setData(list);

        return ResponseEntity.status(response.getStatus()).body(response);
    }



}
