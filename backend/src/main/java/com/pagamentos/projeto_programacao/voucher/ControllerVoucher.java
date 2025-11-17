package com.pagamentos.projeto_programacao.voucher;

import com.pagamentos.projeto_programacao.response.Response;
import com.pagamentos.projeto_programacao.voucher.DTO.CreateVoucher;
import com.pagamentos.projeto_programacao.voucher.DTO.DTOVoucher;
import com.pagamentos.projeto_programacao.voucher.DTO.ResponseVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<Response<List<ResponseVoucher>>> list () {
        Response<List<ResponseVoucher>> response = new Response<List<ResponseVoucher>>(HttpStatus.OK);

        response.setData(serviceVoucher.listar());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{cupom}")
    public ResponseEntity<Response<ResponseVoucher>> listByCupom (@PathVariable String cupom) {
        Response<ResponseVoucher> response = new Response<ResponseVoucher>(HttpStatus.OK);

        ResponseVoucher voucher = serviceVoucher.listByCupom(cupom);

        response.setData(voucher);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}