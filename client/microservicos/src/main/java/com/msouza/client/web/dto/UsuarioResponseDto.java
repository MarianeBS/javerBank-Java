package com.msouza.client.web.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String role;
}