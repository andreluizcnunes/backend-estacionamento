package br.com.estacionamento.api.web.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioPasswordDto {

    @NotBlank(message = "O campo 'password' é obrigatório.")
    @Size(min = 6, max = 6, message = "O campo 'password' deve ter 6 caracteres.")
    private String currentPassword;

    @NotBlank(message = "O campo 'password' é obrigatório.")
    @Size(min = 6, max = 6, message = "O campo 'password' deve ter 6 caracteres.")
    private String newPassword;

    @NotBlank(message = "O campo 'password' é obrigatório.")
    @Size(min = 6, max = 6, message = "O campo 'password' deve ter 6 caracteres.")
    private String confirmPassword;
}
