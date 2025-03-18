package br.com.estacionamento.api.service;

import br.com.estacionamento.api.entity.Usuario;
import br.com.estacionamento.api.exception.EntityNotFoundException;
import br.com.estacionamento.api.exception.PasswordMismatchException;
import br.com.estacionamento.api.exception.UsernameUniqueViolationException;
import br.com.estacionamento.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {

        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' já está cadastrado.", usuario.getUsername()));
        }

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
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordMismatchException("currentPassword incorreta.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Usuário com username '%s' não encontrado.", username)
                )
        );
    }

    @Transactional(readOnly = true)
    public Usuario.Role findByRoleByUsername(String username) {
        return usuarioRepository.findByRoleByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}
