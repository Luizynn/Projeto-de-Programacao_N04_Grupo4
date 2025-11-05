package com.pagamentos.projeto_programacao.payment;

import com.pagamentos.projeto_programacao.payment.DTO.CreatePayment;
import com.pagamentos.projeto_programacao.payment.DTO.EditPayment;
import com.pagamentos.projeto_programacao.payment.DTO.ResponsePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class ControllerPayment {

    @Autowired
    private ServicePayment servicePayment;

    @PostMapping
    public ResponseEntity<ResponsePayment> create(@RequestBody CreatePayment body) throws CardValidator.CardValidationException {
        ResponsePayment newPayment = servicePayment.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPayment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponsePayment> editPayment(
            @PathVariable Long id,
            @RequestBody EditPayment body) {

        ResponsePayment updatedPayment = servicePayment.editPayment(id, body);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping
    public ResponseEntity<List<ResponsePayment>> findAll() {
        List<ResponsePayment> allPayments = servicePayment.findAll();
        return ResponseEntity.ok(allPayments);
    }

    @GetMapping("/client/{idClient}")
    public ResponseEntity<List<ResponsePayment>> findByClientId(@PathVariable Long idClient) {
        List<ResponsePayment> clientPayments = servicePayment.findByClientId(idClient);
        return ResponseEntity.ok(clientPayments);
    }
}