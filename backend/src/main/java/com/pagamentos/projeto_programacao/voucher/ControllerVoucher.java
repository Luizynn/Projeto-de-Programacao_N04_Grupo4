package com.pagamentos.projeto_programacao.voucher;

import com.pagamentos.projeto_programacao.response.Response;
import com.pagamentos.projeto_programacao.voucher.DTO.CreateVoucher;
import com.pagamentos.projeto_programacao.voucher.DTO.DTOVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher")
public class ControllerVoucher {
    @Autowired
    private ServiceVoucher serviceVoucher;

    @PostMapping
    public ResponseEntity<Response<DTOVoucher>> create (@RequestBody CreateVoucher body) {
        Response response = new Response(HttpStatus.CREATED);

        DTOVoucher res = serviceVoucher.create(body);

        response.setData(res);

        return ResponseEntity.status(response.getStatus()).body(response);

    }

}
