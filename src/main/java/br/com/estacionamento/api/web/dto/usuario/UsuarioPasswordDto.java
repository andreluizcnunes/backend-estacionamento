package br.com.estacionamento.api.web.dto.usuario;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioPasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
