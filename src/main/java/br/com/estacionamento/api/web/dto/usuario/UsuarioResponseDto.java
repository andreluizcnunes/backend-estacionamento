package br.com.estacionamento.api.web.dto.usuario;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioResponseDto {

    private UUID id;
    private String username;
    private String role;
}
