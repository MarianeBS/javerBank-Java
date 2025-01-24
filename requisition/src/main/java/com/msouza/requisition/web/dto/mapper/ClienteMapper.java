package com.msouza.requisition.web.dto.mapper;

import com.msouza.requisition.entity.Cliente;
import com.msouza.requisition.web.dto.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {
    public static Cliente cliente(ClienteResponseDto dto){
        return new ModelMapper().map(dto, Cliente.class);
    }

}
