package com.msouza.requisition.proxy;

import com.msouza.requisition.web.dto.UsuarioLoginDto;
import com.msouza.requisition.web.exception.FeignErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authProxy", url = "localhost:8000/api/v1/auth", configuration = FeignErrorDecoder.class)
public interface AutenticacaoProxy {

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto);

}
