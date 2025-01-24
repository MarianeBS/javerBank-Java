package com.msouza.requisition.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteEditDto {
    @Size(min = 5, max = 100)
    private String nome;
    @Size(min = 11, max = 11)
    private String telefone;

    private String valorDeposito;
}
