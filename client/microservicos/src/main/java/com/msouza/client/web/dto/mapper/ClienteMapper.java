package com.msouza.client.web.dto.mapper;

import com.msouza.client.entity.Cliente;
import com.msouza.client.web.dto.ClienteCreateDto;
import com.msouza.client.web.dto.ClienteEditDto;
import com.msouza.client.web.dto.ClienteResponseDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto){
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente){
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

    public static Cliente toCliente(ClienteEditDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }
}
