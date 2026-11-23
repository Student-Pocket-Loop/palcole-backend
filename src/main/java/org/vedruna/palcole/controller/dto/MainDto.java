package org.vedruna.palcole.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Respuesta estándar con un mensaje informativo")
@Data
@AllArgsConstructor
public class MainDto {

  @Schema(
      description = "Mensaje informativo devuelto por el servidor",
      example = "Servicio funcionando correctamente")
  String message;
}
