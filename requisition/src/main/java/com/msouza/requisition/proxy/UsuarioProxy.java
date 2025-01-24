package com.msouza.requisition.proxy;

import com.msouza.requisition.web.dto.UsuarioCreateDto;
import com.msouza.requisition.web.dto.UsuarioResponseDto;
import com.msouza.requisition.web.dto.UsuarioSenhaDto;
import com.msouza.requisition.web.exception.FeignErrorDecoder;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usuariosProxy", url = "localhost:8000/api/v1/usuarios", configuration = FeignErrorDecoder.class)
public interface UsuarioProxy {

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto);

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id);

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto);
}
