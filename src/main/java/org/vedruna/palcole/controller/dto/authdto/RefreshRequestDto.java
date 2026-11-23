package org.vedruna.palcole.controller.dto.authdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Solicitud para renovar el token de acceso utilizando un Refresh Token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequestDto {

  /** El Refresh Token que se desea canjear por un nuevo Access Token. */
  @Schema(
      description = "Token de refresco válido obtenido previamente en el inicio de sesión",
      example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4gZXhhbXBsZQ==")
  @NotBlank
  String refreshToken;
}
