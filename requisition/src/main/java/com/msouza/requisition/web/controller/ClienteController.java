package com.msouza.requisition.web.controller;

import com.msouza.requisition.entity.Cliente;
import com.msouza.requisition.proxy.ClienteProxy;
import com.msouza.requisition.util.ScoreUtils;
import com.msouza.requisition.web.dto.ClienteCreateDto;
import com.msouza.requisition.web.dto.ClienteEditDto;
import com.msouza.requisition.web.dto.ClienteResponseDto;
import com.msouza.requisition.web.dto.ScoreResponseDto;
import com.msouza.requisition.web.dto.mapper.ClienteMapper;
import com.msouza.requisition.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes", description = "Contém todas as operações relativas à criação, leitura, edição e exclusão de um cliente.")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/banco/clientes")
public class ClienteController {

    @Autowired
    private ClienteProxy clienteProxy;

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
                    @ApiResponse(responseCode = "401", description = "Você não está autenticado para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso associado não foi encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Cadastro não efetuado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<ClienteResponseDto> create(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody ClienteCreateDto dto) {

        return clienteProxy.create(token, dto);
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
                    @ApiResponse(responseCode = "401", description = "Você não está autenticado para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso com o ID fornecido não pôde ser encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> getById(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {
        return clienteProxy.findByID(token, id);
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
                    @ApiResponse(responseCode = "401", description = "Você não está autenticado para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso a ser atualizado não foi encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Sua requisição é inválida. Verifique.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> update(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long id, @RequestBody ClienteEditDto dto) {
        return clienteProxy.update(token, id, dto);
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
                    @ApiResponse(responseCode = "401", description = "Você não está autenticado para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso a ser excluído não foi encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {
        return clienteProxy.delete(token, id);
    }


    //Documentação SCORE:
    @Operation(summary = "Verificar Score do cliente",
            description = "Recurso para verificação do score de um cliente vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    //Mensagem de Sucesso:
                    @ApiResponse(responseCode = "204", description = "Score processado com sucesso!",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),

                    //Mensagem de Erro:
                    @ApiResponse(responseCode = "401", description = "Você não está autenticado para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso a ser excluído não foi encontrado no servidor.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}/score")
    public ResponseEntity<ScoreResponseDto> score(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {
        ClienteResponseDto clienteResponseDto = clienteProxy.findByID(token, id).getBody();
        log.info(String.valueOf(clienteResponseDto.getSaldoCc()));
        Cliente cliente = ClienteMapper.cliente(clienteResponseDto);

        ScoreResponseDto scoreResponseDto = new ScoreResponseDto();
        scoreResponseDto.setScoreCredito(ScoreUtils.calcularScore(cliente.getSaldoCc()));
        log.info(String.valueOf(scoreResponseDto.getScoreCredito()));
        log.info(String.valueOf(cliente.getSaldoCc()));
        return ResponseEntity.ok(scoreResponseDto);
    }


    //Documentação Emissão de Comprovante
    @Operation(summary = "Emitir Comprovante de Depósito",
            description = "Recurso para emitir comprovante de depósito vinculado a um usuário cadastrado. 1w2e" +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    //Mensagem de Sucesso:
                    @ApiResponse(responseCode = "200", description = "Comprovante emitido com sucesso!",
                            content = @Content(mediaType = "application/pdf", schema = @Schema(type = "file"))),

                    //Mensagem de Erro:
                    @ApiResponse(responseCode = "401", description = "Você não está autenticado para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar este recurso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/comprovante")
    public ResponseEntity<byte[]> comprovante(@RequestHeader(value = "Authorization", required = false) String token) {
        return clienteProxy.getRelatorio(token);
    }

}
