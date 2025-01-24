package com.msouza.requisition.proxy;

import com.msouza.requisition.web.dto.ClienteCreateDto;
import com.msouza.requisition.web.dto.ClienteEditDto;
import com.msouza.requisition.web.dto.ClienteResponseDto;
import com.msouza.requisition.web.exception.FeignErrorDecoder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "clienteProxy", url = "localhost:8000/api/v1/clientes", configuration = FeignErrorDecoder.class)
public interface ClienteProxy {

    @PostMapping
    public ResponseEntity<ClienteResponseDto> create(@RequestHeader("Authorization") String tokenHeader, @RequestBody @Valid ClienteCreateDto dto);

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> findByID(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id);

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> update(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id, @RequestBody @Valid ClienteEditDto dto);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id);

    @GetMapping("/comprovante")
    public ResponseEntity<byte[]> getRelatorio(@RequestHeader("Authorization") String tokenHeader);

}
