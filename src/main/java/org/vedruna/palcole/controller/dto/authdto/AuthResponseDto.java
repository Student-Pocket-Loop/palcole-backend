package org.vedruna.palcole.controller.dto.authdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Respuesta de autenticación con los tokens JWT generados")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

  @Schema(
      description = "Tipo de esquema de autenticación",
      example = "Bearer",
      accessMode = Schema.AccessMode.READ_ONLY)
  @JsonProperty("token_type")
  final String tokenType = "Bearer";

  @Schema(
      description = "Token JWT de acceso para autorizar peticiones a endpoints protegidos",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  @JsonProperty("access_token")
  String accessToken;

  @Schema(description = "Tiempo de expiración del token de acceso en segundos", example = "3600")
  @JsonProperty("expires_in")
  Long expiresIn;

  @Schema(
      description =
          "Token de refresco utilizado para obtener un nuevo access_token sin volver a iniciar sesión",
      example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4gZXhhbXBsZQ==")
  @JsonProperty("refresh_token")
  String refreshToken;

  @Schema(description = "Permisos o alcance concedido al token", example = "read write")
  @JsonProperty("scope")
  String scope;
}
