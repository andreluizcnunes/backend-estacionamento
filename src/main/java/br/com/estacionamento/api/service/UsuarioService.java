package br.com.estacionamento.api.service;

import br.com.estacionamento.api.entity.Usuario;
import br.com.estacionamento.api.exception.EntityNotFoundException;
import br.com.estacionamento.api.exception.PasswordMismatchException;
import br.com.estacionamento.api.exception.UsernameUniqueViolationException;
import br.com.estacionamento.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {

        if(usuarioRepository.existsByUsername(usuario.getUsername())){
            throw new UsernameUniqueViolationException(
                    String.format("Username '%s' já está cadastrado.", usuario.getUsername())
            );
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Usuário com id '%s' não encontrado.", id)
                )
        );
    }

    @Transactional
    public Usuario editarSenha(UUID id, String currentPassword, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordMismatchException("As senhas não conferem.");
        }

        Usuario user = buscarPorId(id);
        if (!user.getPassword().equals(currentPassword)) {
            throw new PasswordMismatchException("currentPassword incorreta.");
        }

        user.setPassword(newPassword);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}
