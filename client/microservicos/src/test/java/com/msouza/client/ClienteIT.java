package com.msouza.client;

import com.msouza.client.web.dto.*;
import com.msouza.client.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCliente_ComDadosValidos_RetornarClienteCriadoComStatus201() {
        ClienteResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Ana Barbosa", "11944643410", true, 1000f))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getSaldoCc()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Ana Barbosa");
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isEqualTo(11944643410L);
        org.assertj.core.api.Assertions.assertThat(responseBody.isCorrentista()).isEqualTo(true);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSaldoCc()).isEqualTo(1000f);
    }

    @Test
    public void createCliente_ComDadosInvalidos_Retornar422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("", "", false, -100f)) // Dados inválidos
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void findByID_ClienteExistenteERelacionadoAoUsuario_Retornar200() {
        ClienteResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "roberta@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getSaldoCc()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Roberta Machado");
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isEqualTo(79074426050L);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSaldoCc()).isEqualTo(1000f);
    }

    @Test
    public void findByID_ClienteNaoRelacionadoAoUsuario_Retornar403() {
        var response = testClient
                .get()
                .uri("/api/v1/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
                .exchange();

        response.expectStatus().isForbidden();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();
    }

    @Test
    public void findByID_ClienteNaoExistente_Retornar404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/clientes/99999") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void update_ClienteExistenteERelacionadoAoUsuario_Retornar200() {
        ClienteEditDto editDto = new ClienteEditDto("Novo Nome", "11999999999", "500");
        ClienteResponseDto responseBody = testClient
                .patch()
                .uri("/api/v1/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "roberta@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Novo Nome");
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isEqualTo(11999999999L);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSaldoCc()).isEqualTo(1500F);
    }


    @Test
    public void update_ClienteNaoRelacionadoAoUsuario_Retornar403() {
        ClienteEditDto editDto = new ClienteEditDto("Novo Nome", "11999999999", "500");
        var response = testClient
                .patch()
                .uri("/api/v1/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange();

        response.expectStatus().isForbidden();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();
    }

    @Test
    public void update_ClienteNaoExistente_Retornar404() {
        ClienteEditDto editDto = new ClienteEditDto("Novo Nome", "11999999999", "500");
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/clientes/99999") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void update_ComDadosInvalidos_Retornar422() {
        ClienteEditDto editDto = new ClienteEditDto("", "", "-500"); // Dados inválidos
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "roberta@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void delete_ClienteExistenteERelacionadoAoUsuario_Retornar204() {
        testClient
                .delete()
                .uri("/api/v1/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "roberta@email.com", "123456"))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty(); // Não há corpo na resposta para status 204

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/clientes/10") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "roberta@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void delete_ClienteNaoRelacionadoAoUsuario_Retornar403() {
        var response = testClient
                .delete()
                .uri("/api/v1/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange();

        response.expectStatus().isForbidden();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();
    }

    @Test
    public void delete_ClienteNaoExistente_Retornar404() {
        ErrorMessage responseBody = testClient
                .delete()
                .uri("/api/v1/clientes/99999") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "roberta@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void getRelatorio_UsuarioAutenticado_Retornar200ComPdf() {
        byte[] responseBody = testClient
                .get()
                .uri("/api/v1/clientes/comprovante")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "roberta@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_PDF)
                .expectHeader().contentDisposition(ContentDisposition.inline().filename("comprovante.pdf").build())
                .expectBody(byte[].class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.length).isGreaterThan(0); // Verifica se o PDF não está vazio
    }

}
