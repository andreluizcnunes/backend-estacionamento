package br.com.estacionamento.api;

import br.com.estacionamento.api.web.dto.usuario.UsuarioCreateDto;
import br.com.estacionamento.api.web.dto.usuario.UsuarioPasswordDto;
import br.com.estacionamento.api.web.dto.usuario.UsuarioResponseDto;
import br.com.estacionamento.api.web.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsuario_WithUsernameAndPasswordValids_returnStatus201() {
        UsuarioResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_WithUsernameInvalid_ReturnErrorMessageStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_WithPasswordInvalid_ReturnErrorMessageStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", "123"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", "12345689"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_WithUsernameExist_ReturnErrorMessageStatus409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("ana@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void findUsuario_WithIdExisting_ReturnUsuarioWithStatus200() {

        UsuarioResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa552")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(UUID.fromString(String.valueOf(responseBody.getId())))
                .isEqualTo(UUID.fromString("4004ab6e-b3c5-4c87-95d2-382ed57fa552"));
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void findUsuario_WithIdNonexistent_ReturnErrorMessageStatus404() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa559")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void updatePassword_WithValidData_ReturnStatus204() {
        testClient
                .patch()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa552")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("123456", "123458", "123458"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void updatePassword_WithIdNonexistent_ReturnErrorMessageStatus404() {

        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa559")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void updatePassword_WithInvalidFields_ReturnErrorMessageStatus422() {

        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa552")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa552")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("12345", "12345", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa552")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("12345678", "12345678", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void updatePassword_WithInvalidFields_ReturnErrorMessageStatus400() {

        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa552")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("123456", "123456", "123457"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/4004ab6e-b3c5-4c87-95d2-382ed57fa552")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("123452", "123456", "123456"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    }

    @Test
    public void findAllUsuarios_ReturnStatus200() {
        List<UsuarioResponseDto> responseBody =  testClient
                .get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody).isNotEmpty();
        Assertions.assertThat(responseBody).hasSize(3);
        Assertions.assertThat(responseBody.size()).isGreaterThan(0);

        UsuarioResponseDto usuarioAna = responseBody.stream()
                .filter(u -> u.getUsername().equals("ana@email.com"))
                .findFirst()
                .orElse(null);

        Assertions.assertThat(usuarioAna).isNotNull();
        Assertions.assertThat(usuarioAna.getUsername()).isEqualTo("ana@email.com");
        Assertions.assertThat(usuarioAna.getRole()).isEqualTo("ADMIN");
    }

}
