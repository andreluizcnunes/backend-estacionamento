package br.com.estacionamento.api.web.doc;

import br.com.estacionamento.api.web.dto.usuario.UsuarioCreateDto;
import br.com.estacionamento.api.web.dto.usuario.UsuarioPasswordDto;
import br.com.estacionamento.api.web.dto.usuario.UsuarioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Usuarios",
        description = "Contém todas as operações para gerenciar os usuários do sistema."
)
public interface UsuarioDoc {

    @Operation(
            summary = "Cria um novo usuário",
            description = "Recurso para criar um novo usuário no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário criado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Usuário e-mail já cadastrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Recurso não processado por dados de entrada invalidos.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto);


    @Operation(
            security = @SecurityRequirement(name = "security"),
            summary = "Recuperar um usuário por ID",
            description = "Requisição exige um Bear Token válido. Acesso restrito a ADMIN | CLIENTE",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recurso recuperado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão para acessar este recurso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Recurso não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<UsuarioResponseDto> getbayId(@PathVariable UUID id);


    @Operation(
            security = @SecurityRequirement(name = "security"),
            summary = "Atualizar a senha.",
            description = "Requisição exige um Bear Token válido. Acesso restrito a ADMIN | CLIENTE",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Senha atualizada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Senha não confere",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),

                    @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão para acessar este recurso.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Recurso não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "422",
                            description = "Campos invalidos ou mal formatados.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )

            }
    )
    public ResponseEntity<Void> updatePassword(@PathVariable UUID id, @Valid @RequestBody UsuarioPasswordDto dto);

    @Operation(
            security = @SecurityRequirement(name = "security"),
            summary = "Lista todos os usuários.",
            description = "Requisição exige um Bear Token válido. Acesso restrito a ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista todos os usuários cadastrados no sistema.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão para acessar este recurso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nenhum usuário encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<List<UsuarioResponseDto>> getAll();
}
