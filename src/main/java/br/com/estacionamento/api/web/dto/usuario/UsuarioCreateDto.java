package br.com.estacionamento.api.web.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCreateDto {

    @NotBlank(message = "O campo 'username' é obrigatório.")
    @Email(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Formato do e-mail está invalido."
    )
    private String username;

    @NotBlank(message = "O campo 'password' é obrigatório.")
    @Size(min = 6, max = 6, message = "O campo 'password' deve ter 6 caracteres.")
    private String password;
}
