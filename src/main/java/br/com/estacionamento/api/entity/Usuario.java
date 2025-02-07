package br.com.estacionamento.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 25, nullable = false)
    private Role role;

    // atributos para auditoria
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "modified_by")
    private String modifiedBy;

    public enum Role{
        ROLE_ADMIN, ROLE_CLIENTE
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                '}';
    }
}
