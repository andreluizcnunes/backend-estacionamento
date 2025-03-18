package br.com.estacionamento.api.jwt;

import br.com.estacionamento.api.entity.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import java.util.UUID;

public class JwtUserDetails extends User {

    private final Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(
                usuario.getUsername(),
                usuario.getPassword(),
                AuthorityUtils.createAuthorityList(
                        (usuario.getRole().name())
                )
        );

        this.usuario = usuario;
    }

    public UUID getId() {
        return this.usuario.getId();
    }

    public String getRole() {
        return this.usuario.getRole().name();
    }
}
