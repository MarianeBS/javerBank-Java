package com.msouza.client.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteResponseDto {

    private String nome;
    private Long telefone;
    private boolean correntista;
    private Float saldoCc;

    private String port;

}
