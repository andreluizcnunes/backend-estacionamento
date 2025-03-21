package br.com.estacionamento.api.web.controller;

import br.com.estacionamento.api.entity.Usuario;
import br.com.estacionamento.api.service.UsuarioService;
import br.com.estacionamento.api.web.doc.UsuarioDoc;
import br.com.estacionamento.api.web.dto.usuario.UsuarioCreateDto;
import br.com.estacionamento.api.web.dto.mapper.UsuarioMapper;
import br.com.estacionamento.api.web.dto.usuario.UsuarioPasswordDto;
import br.com.estacionamento.api.web.dto.usuario.UsuarioResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioDoc {

    private final UsuarioService usuarioService;

    @Override
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto){
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> getbayId(@PathVariable UUID id){
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @Override
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND #id == authentication.principal.id")
    public ResponseEntity<Void> updatePassword(@PathVariable UUID id, @Valid @RequestBody UsuarioPasswordDto dto){
        Usuario user = usuarioService.editarSenha(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }
}
