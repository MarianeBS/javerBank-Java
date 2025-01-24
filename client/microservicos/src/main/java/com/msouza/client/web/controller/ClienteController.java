package com.msouza.client.web.controller;

import com.msouza.client.entity.Cliente;
import com.msouza.client.jwt.JwtUserDetails;
import com.msouza.client.service.ClienteService;
import com.msouza.client.service.JasperService;
import com.msouza.client.service.UsuarioService;
import com.msouza.client.web.dto.ClienteEditDto;
import com.msouza.client.web.dto.ClienteResponseDto;
import com.msouza.client.web.dto.ClienteCreateDto;
import com.msouza.client.web.dto.mapper.ClienteMapper;
import com.msouza.client.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Clientes", description = "Contém todas as operações relativas à criação, leitura, edição e exclusão de um cliente.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Autowired
    private Environment environment;
    @Autowired
    private JasperService jasperService;

    //Documentação CREATE:
    @Operation(summary = "Criar um novo cliente",
            description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    //Mensagem de Sucesso:
                    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso!",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),

                    //Mensagem de Erro:
                    @ApiResponse(responseCode = "422", description = "Cadastro não efetuado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    //Endpoint CREATE:
    @PostMapping
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        var port = environment.getProperty("local.server.port");
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        cliente.setSaldoCc(dto.getValorDeposito());
        clienteService.salvar(cliente);
        ClienteResponseDto clienteResponseDto = ClienteMapper.toDto(cliente);
        clienteResponseDto.setPort(port);

        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(clienteResponseDto);
    }


    //Documentação READ:
    @Operation(summary = "Encontrar informações do cliente",
            description = "Recurso para encontrar cliente vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    //Mensagem de Sucesso:
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso!",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),

                    //Mensagem de Erro:
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso com o ID fornecido não pôde ser encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    //Endpoint READ:
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> findByID(@PathVariable Long id,
                                                       @AuthenticationPrincipal JwtUserDetails userDetails) {
        var port = environment.getProperty("local.server.port");
        Cliente cliente = clienteService.buscarPorId(id);

        if(!cliente.getUsuario().getId().equals(userDetails.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ClienteResponseDto clienteResponseDto = ClienteMapper.toDto(cliente);
        clienteResponseDto.setPort(port);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(clienteResponseDto);
    }


    //Documentação UPDATE:
    @Operation(summary = "Atualizar informações do cliente",
            description = "Recurso para atualização de dados do cliente vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    //Mensagem de Sucesso:
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso!",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),

                    //Mensagem de Erro:
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso a ser atualizado não foi encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Sua requisição é inválida. Verifique.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    //Endpoint UPDATE:
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> update(@PathVariable Long id, @RequestBody @Valid ClienteEditDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        var port = environment.getProperty("local.server.port");
        Cliente cliente = ClienteMapper.toCliente(dto);
        Cliente cli = clienteService.buscarPorId(id);

        if(!cli.getUsuario().getId().equals(userDetails.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Float deposito = Float.valueOf(dto.getValorDeposito());

        cliente.setUltimoDeposito(deposito);
        cliente.setSaldoCc(cli.getSaldoCc()+deposito);
        clienteService.atualizar(cliente, id);
        ClienteResponseDto clienteResponseDto = ClienteMapper.toDto(cliente);
        clienteResponseDto.setPort(port);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(clienteResponseDto);
    }

    //Documentação DELETE:
    @Operation(summary = "Excluir um cliente",
            description = "Recurso para excluir um cliente vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    //Mensagem de Sucesso:
                    @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso! Não há corpo na resposta.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),

                    //Mensagem de Erro:
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso a ser excluído não foi encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    //Endpoint DELETE:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = clienteService.buscarPorId(id);

        if(!cliente.getUsuario().getId().equals(userDetails.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        clienteService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    //Documentação Emissão de Comprovante
    @Operation(summary = "Emitir Comprovante de Depósito",
            description = "Recurso para emitir comprovante de depósito vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    //Mensagem de Sucesso:
                    @ApiResponse(responseCode = "200", description = "Comprovante emitido com sucesso!",
                            content = @Content(mediaType = "application/pdf", schema = @Schema(type = "file"))),

            })

    //Endpoint Emissão de Relatorio
    @GetMapping("/comprovante")
    public ResponseEntity<byte[]> getRelatorio(@AuthenticationPrincipal JwtUserDetails user) throws IOException {
        Long id = user.getId();
        jasperService.addParams("ID_USUARIO", id);

        byte[] bytes = jasperService.gerarPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("comprovante.pdf").build());

        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
